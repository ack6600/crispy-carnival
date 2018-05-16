import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class TitleScreen implements Runnable{
    private JFrame frame;
    private KeyHandler keyHandler;
    private Runnable[] targets;
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
    private int nameOffset = 100;
    private int cooldown = 0;
    private boolean drawLogo = false;
    private BufferedImage logo;

    private final static int NAME_HEIGHT = 60;
    private final static int NAME_WIDTH = 200;

    public TitleScreen(JFrame root, int width, int height, String[] names, Runnable[] targets, BufferedImage logo){
        this.frame = root;
        keyHandler = new KeyHandler();
        frame.addKeyListener(keyHandler);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setResizable(true);
        this.targets = targets;
        this.names = names;
        if(logo != null){
            drawLogo = true;
            this.logo = logo;
            nameOffset = logo.getHeight() + 100;
        }
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

    public void start() {
        frame.setVisible(true);
        this.running = true;
        (new Thread(drawRunnable)).start();
        (new Thread(keyRunnable)).start();
    }

    private void update(){
        while(running){
            Image draw = frame.createImage(frame.getWidth(),frame.getHeight());
            Graphics g = draw.getGraphics();
            g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
            for(int i = 0; i < targets.length; i++){
                if(i == selected)
                    g.setColor(Color.RED);
                int x = (int)((double)frame.getWidth()/2d) - NAME_WIDTH/2;
                int y = nameOffset + ((int)(1.5*NAME_HEIGHT) * i);
                g.drawRect(x,y,NAME_WIDTH,NAME_HEIGHT);
                String title = names[i];
                int stringX = ((NAME_WIDTH - g.getFontMetrics().stringWidth(title))/2) + x;
                int stringY = ((NAME_HEIGHT - g.getFontMetrics().getHeight())/2) + y + g.getFontMetrics().getAscent();
                g.drawString(title,stringX, stringY);
                g.setColor(Color.BLACK);
                if(drawLogo)
                    g.drawImage(logo, (x + NAME_WIDTH/2) - logo.getWidth()/2, 50, null);
            }
            frame.getGraphics().drawImage(draw,0,0,null);
        }
    }

    private void getKeys(){
        keyHandler.registerKeys(keys);
        long now = System.nanoTime();
        while (running){
            if(cooldown == 0){
                try {
                    if(keyHandler.getKeyPressed(keys[0])){
                        if(selected < targets.length-1) {
                            selected++;
                        }else {
                            selected = 0;
                        }
                    }
                    if(keyHandler.getKeyPressed(keys[1])){
                        if(selected > 0) {
                            selected--;
                        }else {
                            selected = targets.length-1;
                        }
                    }
                    if(keyHandler.getKeyPressed(keys[2]))
                        launch();
                    if(keyHandler.getKeyPressed(keys[3]))
                        System.exit(80085);
                    cooldown = 100;
                    now = System.nanoTime();
                } catch (UnregisteredKeyException e) {
                    KeyHandler.handleKeyError(e.getMessage(),keyHandler);
                }
            }
            if(cooldown > 0){
                if(now + TimeUnit.MILLISECONDS.toNanos(cooldown) < System.nanoTime())
                    cooldown = 0;
            }
        }
    }

    private void launch(){
        running = false;
        targets[selected].run();
    }

    @Override
    public void run() {
        cooldown = 500;
        this.start();
    }
}
