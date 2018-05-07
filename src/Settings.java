import java.awt.*;
import java.awt.event.KeyEvent;

public class Settings {
    public static final int carOneControlsUp = KeyEvent.VK_UP;
    public static final int carOneControlsDown = KeyEvent.VK_DOWN;
    public static final int carOneControlsRight = KeyEvent.VK_RIGHT;
    public static final int carOneControlsLeft = KeyEvent.VK_LEFT;
    public static final int carTwoControlsUp = KeyEvent.VK_W;
    public static final int carTwoControlsDown = KeyEvent.VK_S;
    public static final int carTwoControlsRight = KeyEvent.VK_D;
    public static final int carTwoControlsLeft = KeyEvent.VK_A;
    public static final Color carOneColor = Color.RED;
    public static final Color carTwoColor = Color.BLUE;
    public static final Color ballColor = Color.BLACK;

    //vv this has to be manually written vv
    public static final String[] settingNames = {"Car One: Forward", "Car One: Backwards", "Car One: Turn Right", "Car One: Turn Left", "Car Two: Forward", "Car Two: Backwards", "Car Two: Turn Right", "Car Two: Turn Left",  "Car One Color", "Car Two Color", "Ball Color"};
    public static final String[] settingsKeys = {"c1-up", "c1-down", "c1-right", "c1-left", "c2-up", "c2-down", "c2-right", "c2-left", "c1-color", "c2-color", "ball-color"};
    public static final Object[] allSettings = {carOneControlsUp, carOneControlsDown, carOneControlsRight, carOneControlsLeft, carTwoControlsUp, carTwoControlsDown, carTwoControlsRight, carTwoControlsLeft, carOneColor, carTwoColor,ballColor};
}
