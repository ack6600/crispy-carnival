import java.awt.Image;

public abstract class Entity {
    private double x,y;
    private Image sprite;

    public abstract double getX();
    public abstract double getY();
    public abstract double getAngle();
    public abstract double getLength();
    public abstract void update(KeyHandler keyHandler, long time);
    public abstract int[] getControls();
}
