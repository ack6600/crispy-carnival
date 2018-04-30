import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    enum DrawType{
        Circle,
        Polygon,
    }
    public abstract double getX();
    public abstract double getY();
    public abstract double getAngle();
//    public abstract double getLength();
    public abstract Polygon getPolygon();
    public abstract Color getColor();
    public abstract boolean intersects(Polygon p);
//    public abstract Rectangle2D getBounds();
    public abstract void update(KeyHandler keyHandler, EntityHandler e, long time);
    public abstract int[] getControls();
    public abstract DrawType getType();
    public abstract void setCollisions(int[] collisions);
    public abstract boolean collides();
}
