import java.awt.*;
import java.util.ArrayList;

public class EntityHandler implements Runnable {
    private KeyHandler keyHandler;
    private ArrayList<Entity> entities;

    public EntityHandler(KeyHandler keyHandler){
        this.keyHandler = keyHandler;
        entities = new ArrayList<>();
    }

    public int addEntity(Entity e){
        entities.add(e);
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
        for(Entity entity : entities){
            graphics.drawImage(entity.getSprite(), (int) entity.getX(), (int) entity.getY(), null);
        }
        graphics.drawString(s,10,42);
        return image;
    }

    @Override
    public void run() {
        for(Entity e : entities){
            e.update();
        }
    }
}
