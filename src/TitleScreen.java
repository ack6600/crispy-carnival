import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class TitleScreen {
    private JFrame frame;
    private KeyHandler keyHandler;
    private Runnable[] targets;
    private String[] names;
    private Thread drawThread, keyThread;
    private int[] keys = {
            KeyEvent.VK_DOWN,
            KeyEvent.VK_UP,
            KeyEvent.VK_ENTER
    };
    private volatile boolean running;
    private volatile int selected = 0;
    private int nameOffset = 100;
    private boolean cooldown = false;
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
        drawThread = new Thread(this::update);
        keyThread = new Thread(this::getKeys);
    }

    public void start() {
        frame.setVisible(true);
        this.running = true;
        drawThread.start();
        keyThread.start();
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
        long now = 0;
        while (running){
            if(!cooldown){
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

    private void launch(){
        running = false;
        targets[selected].run();
    }
}
