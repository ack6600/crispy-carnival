import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityHandler implements Runnable {
    private KeyHandler keyHandler;
    private ArrayList<Entity> entities;
    private long lastTime = 0;

    public EntityHandler(KeyHandler keyHandler){
        this.keyHandler = keyHandler;
        entities = new ArrayList<>();
    }

    public int addEntity(Entity e){
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

    public Image drawFrame(Image image, String s){
        Graphics graphics = image.getGraphics();
        int entityNum = 0;
        for(Entity entity : entities){
            graphics.drawString(entityNum + ": " + (int) entity.getX() + " " + (int) entity.getY(), 10, 52 + (10 * entityNum));
            graphics.drawLine((int) entity.getX(), (int) entity.getY(), (int) (entity.getX() + (entity.getLength() * Math.cos(Math.toRadians(entity.getAngle())))), (int) (entity.getY() + (entity.getLength() * Math.sin(Math.toRadians(entity.getAngle())))));
            entityNum++;
        }
        graphics.drawString(s,10,42);
        return image;
    }

    @Override
    public void run() {
        long time = System.nanoTime();
        for(Entity e : entities){
            e.update(this.keyHandler, (time - lastTime));
        }
        lastTime = time;
    }
}
