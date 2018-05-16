import javax.swing.text.Caret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class BallEntity extends Entity {
    private double x, y;
    private double x0, y0;
    private double xSpeed = 0.0;
    private double ySpeed = 0.0;
    private Color color;
    private double angle;
    private double width = 20;
    private double friction = 20;
    private double speedMult = 1.1;
    private int[] collisions;

    public BallEntity(double x, double y, Color color){
        this.x = this.x0 = x;
        this.y = this.y0 = y;
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
        return angle;
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
        int collides = 0;
        for(int id : collisions){
            Entity e = entityHandler.getEntity(id);
            if(this.intersects(e.getPolygon())){
                if(e.getClass() == CarEntity.class){
                    CarEntity carEntity = (CarEntity) e;
                    xSpeed = (carEntity.isForwards() ? 1 : -1) * carEntity.getSpeeds()[0] * speedMult;
                    ySpeed = (carEntity.isForwards() ? 1 : -1) * carEntity.getSpeeds()[1] * speedMult;
                }else if(e.getClass() == WallEntity.class){
                    WallEntity wallEntity = (WallEntity) e;
                    if(wallEntity.getVertical()) {
                        xSpeed = -xSpeed;
                    }else {
                        ySpeed = -ySpeed;
                    }
                    collides--;
                }else if(e.getClass() == GoalEntity.class){
                    entityHandler.increaseScore(((GoalEntity) e).getTeam());
                    xSpeed = 0;
                    ySpeed = 0;
                    x = x0;
                    y = y0;
                }
                collides++;
            }
        }
        double seconds = ((double) time) / 1000000000.0;
        if(collides == 0){
            if(xSpeed > 0){
                xSpeed = Math.max(xSpeed - (friction * seconds), 0);
            }else if(xSpeed < 0){
                xSpeed = Math.min(xSpeed + (friction * seconds), 0);
            }
            if(ySpeed > 0){
                ySpeed = Math.max(ySpeed - (friction * seconds), 0);
            }else if(ySpeed < 0){
                ySpeed = Math.min(ySpeed + (friction * seconds), 0);
            }
        }
        this.x += xSpeed * seconds;
        this.y += ySpeed * seconds;
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
