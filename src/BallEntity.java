import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class BallEntity extends Entity {
    private double x, y;
    private double angle;

    private int[] controls = {
            KeyEvent.VK_W,
            KeyEvent.VK_A,
            KeyEvent.VK_S,
            KeyEvent.VK_D
    };
    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public Polygon getPolygon() {

        return new Polygon(new int[] {20},new int[] {20},1);
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public boolean intersects(Polygon p) {
        return false;
    }

    @Override
    public void update(KeyHandler keyHandler, long time) {

    }

    @Override
    public int[] getControls() {
        return controls;
    }

    @Override
    public DrawType getType() {
        return DrawType.Circle;
    }
}
