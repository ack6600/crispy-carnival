import java.awt.*;

public class GoalEntity extends Entity {
    private double x,y;
    private boolean vert;
    private double width = 10;
    private double length;
    private int team;

    public GoalEntity(double x, double y, boolean vertical, double length, int team){
        this.x = x;
        this.y = y;
        this.vert = vertical;
        this.length = length;
        this.team = team;
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
                (int) (x + length),
                (int) (x + length),
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
        return Color.ORANGE;
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
    public void update(KeyHandler keyHandler, EntityHandler e, long time) {

    }

    @Override
    public int[] getControls() {
        return null;
    }

    @Override
    public DrawType getType() {
        return DrawType.Polygon;
    }

    @Override
    public void setCollisions(int[] collisions) {
    }

    @Override
    public boolean collides() {
        return false;
    }

    public int getTeam(){
        return team;
    }
}
