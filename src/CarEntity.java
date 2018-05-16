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
    private double angle = 90.0;
    private double length = 20.0;
    private double width = 40.0;
    private double speed = 400.0;
    private final double TURN_SPEED = 350.0;
    private int[] collisions;
    private int[] controls;
    private boolean collides;
    private boolean forwards = true;

    public CarEntity(double x, double y, Color color, int[] controls, double startAngle){
        this.x = x;
        this.y = y;
        this.color = color;
        this.controls = controls;
        this.angle = startAngle;
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
        int xBound = 0;
        int yBound = 0;
        for(int i : collisions){
            Entity entity = entityHandler.getEntity(i);
            if(this.intersects(entity.getPolygon())) {
                bumps++;
                if(entity.getClass() == CarEntity.class || entity.getClass() == WallEntity.class) {
                    if(entity.getX() > this.getX()){
                        xBound = -1;
                    }else {
                        xBound = 1;
                    }
                    if(entity.getY() > this.getY()){
                        yBound = -1;
                    }else{
                        yBound = 1;
                    }
                }
            }
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
            double [] speeds = getSpeeds();
            double xInc = speeds[0] * seconds;
            double yInc = speeds[1] * seconds;
            if(up) {
                forwards = true;
                this.x += (xBound == 0) ? xInc : (xBound > 0) ? Math.max(xInc, 0) : Math.min(xInc, 0);
                this.y += (yBound == 0) ? yInc : (yBound > 0) ? Math.max(yInc, 0) : Math.min(yInc, 0);
            }
            if(down){
                forwards = false;
                this.x -= (xBound == 0) ? xInc : (xBound > 0) ? Math.min(xInc, 0) : Math.max(xInc, 0);
                this.y -= (yBound == 0) ? yInc : (yBound > 0) ? Math.min(yInc, 0) : Math.max(yInc, 0);
            }

        } catch (UnregisteredKeyException e) {
            KeyHandler.handleKeyError(e.getMessage(),keyHandler);
        }
    }

    public double[] getSpeeds(){
        return new double[] {(speed * Math.cos(Math.toRadians(angle))), (speed * Math.sin(Math.toRadians(angle)))};
    }

    public boolean isForwards(){
        return forwards;
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
