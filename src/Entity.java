import java.awt.Image;

public abstract class Entity {
    private double x,y;
    private Image sprite;

    public abstract double getX();
    public abstract double getY();
    public abstract Image getSprite();
    public abstract void update();
}
