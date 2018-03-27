import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    private double x,y;
    private Image sprite;

    public abstract double getX();
    public abstract double getY();
    public abstract double getAngle();
    public abstract double getLength();
    public abstract Polygon getPolygon();
    public abstract Color getColor();
    public abstract boolean intersects(Polygon p);
    public abstract Rectangle2D getBounds();
    public abstract void update(KeyHandler keyHandler, long time);
    public abstract int[] getControls();
}
