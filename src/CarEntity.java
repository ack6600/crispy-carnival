import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CarEntity extends Entity {
    private double x, y;
    private Color color;
    private double angle = 0.0;
    private double length = 20.0;
    private double width = 40.0;
    private double speed = 200.0;
    private final double TURN_SPEED = 200.0;
    private int[] collisions;
    private int[] controls;
    private boolean collides;

    public CarEntity(double x, double y, Color color, int[] controls){
        this.x = x;
        this.y = y;
        this.color = color;
        this.controls = controls;
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
    public Polygon getPolygon() {
        double nX = this.x - (Math.sqrt((this.width * this.width) + (this.length * this.length))/2.0 * Math.cos(Math.toRadians(this.angle-(90.0-Math.toDegrees(Math.atan2(this.width,this.length))))));
        double nY = this.y - (Math.sqrt((this.width * this.width) + (this.length * this.length))/2.0 * Math.sin(Math.toRadians(this.angle-(90.0-Math.toDegrees(Math.atan2(this.width,this.length))))));
        int[] xPoints = {
                (int) nX,
                (int) (nX + this.width * Math.cos(Math.toRadians(this.angle))),
                (int) (nX + (Math.sqrt((this.width * this.width) + (this.length * this.length)) * Math.cos(Math.toRadians(this.angle-(90.0-Math.toDegrees(Math.atan2(this.width,this.length))))))),
                (int) (nX + this.length * Math.cos(Math.toRadians(this.angle-90.0)))
        };
        int[] yPoints = {
                (int) nY,
                (int) (nY + this.width * Math.sin(Math.toRadians(this.angle))),
                (int) (nY + (Math.sqrt((this.width * this.width) + (this.length * this.length)) * Math.sin(Math.toRadians(this.angle-(90.0-Math.toDegrees(Math.atan2(this.width,this.length))))))),
                (int) (nY + this.length * Math.sin(Math.toRadians(this.angle-90.0)))

        };
        return new Polygon(xPoints, yPoints, 4);
    }

    @Override
    public Color getColor() {
        return color;
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
    public void update(KeyHandler keyHandler, EntityHandler entityHandler, long time) {
        double seconds = ((double) time) / 1000000000.0;
        int bumps = 0;
        for(int i : collisions){
            if(this.intersects(entityHandler.getEntity(i).getPolygon()))
                bumps++;
        }
        collides = bumps > 0;
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
            String error = e.getMessage();
            for(int i = 0; i < error.length();i++){
                if(error.charAt(i) == ':')
                    keyHandler.registerKeys(new int[] {Integer.parseInt(error.substring(i+1))});
            }
        }
    }

    private double[] getSpeeds(double angle){
        return new double[] {(speed * Math.cos(Math.toRadians(angle))), (speed * Math.sin(Math.toRadians(angle)))};
    }

    @Override
    public int[] getControls() {
        return controls;
    }

    @Override
    public DrawType getType() {
        return DrawType.Polygon;
    }

    @Override
    public void setCollisions(int[] collisions) {
        this.collisions = collisions;
    }

    @Override
    public boolean collides() {
        return collides;
    }
}
