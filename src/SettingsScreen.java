import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;

public class SettingsScreen implements KeyListener, Runnable {
    private JFrame frame;
    private Runnable back;
    private KeyHandler keyHandler;
    private SettingsObject defaults;
    private String[] names;
    private Runnable drawRunnable, keyRunnable;
    private int[] keys = {
            KeyEvent.VK_DOWN,
            KeyEvent.VK_UP,
            KeyEvent.VK_ENTER,
            KeyEvent.VK_ESCAPE
    };
    private volatile boolean running;
    private volatile int selected = 0;
    private boolean cooldown = false;
    private volatile int lastPressed;

    private int NAME_OFFSET = 80;
    private final static int NAME_HEIGHT = 60;
    private final static int NAME_WIDTH = 220;

    public SettingsScreen(JFrame root, SettingsObject defaults) {
        this.frame = root;
        this.keyHandler = new KeyHandler();
        this.frame.addKeyListener(keyHandler);
        this.defaults = defaults;
        this.names = Settings.settingNames;
        this.running = false;
        drawRunnable = new Runnable() {
            @Override
            public void run() {
                update();
            }
        };
        keyRunnable = new Runnable() {
            @Override
            public void run() {
                getKeys();
            }
        };
    }

    public void setBack(Runnable back){
        this.back = back;
    }

    @Override
    public void run() {
        this.running = true;
        selected = 0;
        (new Thread(drawRunnable)).start();
        (new Thread(keyRunnable)).start();
    }

    private void update(){
        while(running){
            Image draw = frame.createImage(frame.getWidth(),frame.getHeight());
            Graphics g = draw.getGraphics();
            g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
            g.drawString("Press escape to go back to the menu", 50, 60);
            for(int i = 0; i < names.length; i++){
                if(i == selected)
                    g.setColor(Color.RED);
                int column = 0;
                for(int f = i - 5; f > 0; f -= 6)
                    column++;
                int x = 50 + ((column * NAME_WIDTH) + (30 * column));
                int y = NAME_OFFSET + ((int)(1.5*NAME_HEIGHT) * (i - (column * 6)));
                g.drawRect(x,y,NAME_WIDTH,NAME_HEIGHT);
                String title = names[i];
                int stringX = ((NAME_WIDTH - g.getFontMetrics().stringWidth(title))/2) + x;
                int stringY = ((NAME_HEIGHT - g.getFontMetrics().getHeight())/2) + y + g.getFontMetrics().getAscent();
                g.drawString(title,stringX, stringY);
                g.setColor(Color.BLACK);
            }
            frame.getGraphics().drawImage(draw,0,0,null);
        }
    }

    private void getKeys(){
        keyHandler.registerKeys(keys);
        long now = 0;
        while (running){
            if(!cooldown){
                try {
                    if(keyHandler.getKeyPressed(keys[0])){
                        if(selected < names.length-1) {
                            selected++;
                        }else {
                            selected = 0;
                        }
                    }
                    if(keyHandler.getKeyPressed(keys[1])){
                        if(selected > 0) {
                            selected--;
                        }else {
                            selected = names.length-1;
                        }
                    }
                    if(keyHandler.getKeyPressed(keys[2]))
                        handleSetting();
                    if(keyHandler.getKeyPressed(keys[3]))
                        this.exit();
                    cooldown = true;
                    now = System.nanoTime();
                } catch (UnregisteredKeyException e) {
                    String error = e.getMessage();
                    for(int i = 0; i < error.length();i++){
                        if(error.charAt(i) == ':')
                            keyHandler.registerKeys(new int[] {Integer.parseInt(error.substring(i+1))});
                    }
                }
            }
            if(cooldown){
                if(now + TimeUnit.MILLISECONDS.toNanos(100) < System.nanoTime())
                    cooldown = false;
            }
        }
    }

    private void exit() {
        this.running = false;
        back.run();
    }

    private void handleSetting() {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        lastPressed = e.getKeyCode();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
