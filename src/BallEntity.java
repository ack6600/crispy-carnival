import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class BallEntity extends Entity {
    private double x, y;
    private Color color;
    private double angle;
    private double width = 20;
    private int[] collisions;

    public BallEntity(double x, double y, Color color){
        this.x = x;
        this.y = y;
        this.color = color;
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
        return 0;
    }

    @Override
    public Polygon getPolygon() {
        int[] xPoints = {
                (int) x,
                (int) (x + width),
                (int) (x + width),
                (int) x
        };
        int[] yPoints = {
                (int) y,
                (int) y,
                (int) (y + width),
                (int) (y + width)
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
        for(int id : collisions){
            Entity e = entityHandler.getEntity(id);
//            if(this.intersects(e.getPolygon())){
//                this.angle =
//            }
        }
    }

    @Override
    public int[] getControls() {
        return null;
    }

    @Override
    public DrawType getType() {
        return DrawType.Circle;
    }

    @Override
    public void setCollisions(int[] collisions) {
        this.collisions = collisions;
    }

    @Override
    public boolean collides() {
        return false;
    }
}
