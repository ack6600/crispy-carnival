import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityHandler implements Runnable {
    private KeyHandler keyHandler;
    private ArrayList<Entity> entities;
    private long lastTime = 0;
    private boolean debug = false;
    private League league;

    public EntityHandler(KeyHandler keyHandler, League league){
        this.keyHandler = keyHandler;
        this.league = league;
        keyHandler.registerKey(KeyEvent.VK_ESCAPE);
        entities = new ArrayList<>();
    }

    public synchronized int addEntity(Entity e){
        entities.add(e);
        this.keyHandler.registerKeys(e.getControls());
        return e.hashCode();
    }

    public Entity getEntity(int id){
        for(Entity entity : entities){
            if(id == entity.hashCode())
                return entity;
        }
        return null;
    }

    public synchronized void reset(){
        entities.clear();
    }

    public void setDebug(boolean debug){
        this.debug = debug;
    }

    public synchronized Image drawFrame(Image image, String s, int[] score){
        Graphics graphics = image.getGraphics();
        int entityNum = 0;
        for(Entity entity : entities){
//            Polygon intersect = new Polygon(new int[] {50,50,100,100},new int[] {50,100,100,50},4);
            if(debug)
                graphics.setColor(entity.getColor());
                graphics.drawString(entityNum + ": " + (int) entity.getX() + " " + (int) entity.getY() + " " + entity.getAngle() + "°", 10, 52 + (10 * entityNum));
//            graphics.drawString("Does intersect: " + entity.intersects(intersect), 10, 62);
            graphics.setColor(entity.collides() ? (debug ? Color.GREEN : entity.getColor()) : entity.getColor());
            if(entity.getType() == Entity.DrawType.Polygon) {
                graphics.fillPolygon(entity.getPolygon());
            }
            if(entity.getType() == Entity.DrawType.Circle){
                if(debug) {
                    graphics.drawPolygon(entity.getPolygon());
                }
                graphics.fillOval((int)entity.getX(),(int)entity.getY(),(int) (entity.getPolygon().xpoints[2] - entity.getX()), (int) (entity.getPolygon().ypoints[2] - entity.getY()));
            }
            graphics.setColor(Color.BLACK);
//            graphics.drawPolygon(intersect);
//            graphics.drawRect((int)entity.getBounds().getX(),(int)entity.getBounds().getY(),(int)entity.getBounds().getWidth(), (int) entity.getBounds().getHeight());
            entityNum++;
        }
        graphics.drawString(s,10,42);
        graphics.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        graphics.drawString("Red:" + String.valueOf(score[1]), image.getWidth(null)/2 - 150,48);
        graphics.drawString("Blue:" + String.valueOf(score[0]), image.getWidth(null)/2 + 100,48);
        return image;
    }

    public void increaseScore(int yeet){
        league.increaseScore(yeet);
    }
    @Override
    public void run() {
        long time = System.nanoTime();
        for(Entity e : entities){
            e.update(this.keyHandler, this, (time - lastTime));
        }
        try {
            if(keyHandler.getKeyPressed(KeyEvent.VK_ESCAPE))
                league.exit();
        } catch (UnregisteredKeyException e) {
            KeyHandler.handleKeyError(e.getMessage(),keyHandler);
        }
        lastTime = time;
    }
}
