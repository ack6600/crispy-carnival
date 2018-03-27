import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CarEntity extends Entity {
    private double x, y;
    private double angle = 0.0;
    private double length = 20.0;
    private double width = 40.0;
    private double speed = 100.0;
    private final double TURN_SPEED = 45.0;
    private int[] controls = {
            KeyEvent.VK_UP,
            KeyEvent.VK_DOWN,
            KeyEvent.VK_RIGHT,
            KeyEvent.VK_LEFT};

    public CarEntity(double x, double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getAngle() {
        return angle;
    }

    @Override
    public double getLength() {
        return this.length;
    }

    @Override
    public Polygon getPolygon() {
        int[] xPoints = {
                (int) this.x,
                (int) (this.x + this.width * Math.cos(Math.toRadians(this.angle))),
                (int) (this.x + (Math.sqrt((this.width * this.width) + (this.length * this.length)) * Math.cos(Math.toRadians(this.angle-(90.0-Math.toDegrees(Math.atan2(this.width,this.length))))))),
                (int) (this.x + this.length * Math.cos(Math.toRadians(this.angle-90.0)))
        };
        int[] yPoints = {
                (int) this.y,
                (int) (this.y + this.width * Math.sin(Math.toRadians(this.angle))),
                (int) (this.y + (Math.sqrt((this.width * this.width) + (this.length * this.length)) * Math.sin(Math.toRadians(this.angle-(90.0-Math.toDegrees(Math.atan2(this.width,this.length))))))),
                (int) (this.y + this.length * Math.sin(Math.toRadians(this.angle-90.0)))

        };
        return new Polygon(xPoints, yPoints, 4);
    }

    @Override
    public Color getColor() {
        return Color.MAGENTA;
    }

    @Override
    public boolean intersects(Polygon p) {
        for(int i = 0; i < p.npoints; i++){
            if(this.getPolygon().contains(p.xpoints[i],p.ypoints[i]) || p.contains(getPolygon().xpoints[i],getPolygon().ypoints[i]))
                return true;
        }
        return false;
    }

    @Override
    public Rectangle2D getBounds() {
        return this.getPolygon().getBounds2D();
    }

    @Override
    public void update(KeyHandler keyHandler, long time) {
        double seconds = ((double) time) / 1000000000.0;
        try {
            boolean up = keyHandler.getKeyPressed(controls[0]);
            boolean down = keyHandler.getKeyPressed(controls[1]);
            boolean right = keyHandler.getKeyPressed(controls[2]);
            boolean left = keyHandler.getKeyPressed(controls[3]);
            if(right)
                angle += TURN_SPEED * seconds;
            if(left)
                angle -= TURN_SPEED * seconds;
            double [] speeds = getSpeeds(this.angle);
            if(up) {
                this.x += speeds[0] * seconds;
                this.y += speeds[1] * seconds;
            }
            if(down){
                this.x -= speeds[0] * seconds;
                this.y -= speeds[1] * seconds;
            }

        } catch (UnregisteredKeyException e) {
            e.printStackTrace();
        }
    }

    private double[] getSpeeds(double angle){
        return new double[] {(speed * Math.cos(Math.toRadians(angle))), (speed * Math.sin(Math.toRadians(angle)))};
    }

    @Override
    public int[] getControls() {
        return controls;
    }
}
