import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class League implements Runnable{
    private Runnable gameRunnable;
    private Runnable drawRunnable;
    private KeyHandler keyHandler;
    private EntityHandler entityHandler;
    private static SettingsObject settingsObject;
    private JFrame frame;
    private long lastTime = System.nanoTime();
    private long fps = 0;
    private Runnable back;
    private volatile boolean running = false;

    public static void main(String[] args){
        initSettings();
        JFrame root = new JFrame("League");
        League league = new League(root,1280,720, settingsObject);
        SettingsScreen settings = new SettingsScreen(root, settingsObject);
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File("images/header.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        TitleScreen titleScreen = new TitleScreen(root, 1280, 720, new String[] {"Start", "Settings"}, new Runnable[] {league, settings}, logo);
        settings.setBack(titleScreen);
        league.setBack(titleScreen);
        titleScreen.start();
    }

    private static void initSettings() {
        settingsObject = new SettingsObject(Settings.settingsKeys, Settings.allSettings, Math.min(Settings.allSettings.length, Settings.settingsKeys.length));
    }

    public League(JFrame root, int width, int height, SettingsObject settingsObject){
        this.frame = root;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setResizable(true);
        keyHandler = new KeyHandler();
        entityHandler = new EntityHandler(keyHandler, this);
        entityHandler.setDebug(true);
        gameRunnable = new Runnable() {
            @Override
            public void run() {
                updatePhysics();
            }
        };
        drawRunnable = new Runnable() {
            @Override
            public void run() {
                reDraw();
            }
        };
        frame.addKeyListener(keyHandler);
    }

    public void setBack(Runnable back){
        this.back = back;
    }


    @Override
    public void run(){
        this.running = true;
        frame.setVisible(true);
        new Thread(gameRunnable).start();
        new Thread(drawRunnable).start();
    }

    private void updatePhysics(){
        entityHandler.reset();
        int[] carOneControls = new int[] {
                (int) settingsObject.getSetting(Settings.settingsKeys[0]),
                (int) settingsObject.getSetting(Settings.settingsKeys[1]),
                (int) settingsObject.getSetting(Settings.settingsKeys[2]),
                (int) settingsObject.getSetting(Settings.settingsKeys[3]),
        };
        int[] carTwoControls = new int[] {
                (int) settingsObject.getSetting(Settings.settingsKeys[4]),
                (int) settingsObject.getSetting(Settings.settingsKeys[5]),
                (int) settingsObject.getSetting(Settings.settingsKeys[6]),
                (int) settingsObject.getSetting(Settings.settingsKeys[7]),
        };
        CarEntity car1 = new CarEntity(0,0, (Color) settingsObject.getSetting(Settings.settingsKeys[8]), carOneControls);
        CarEntity car2 = new CarEntity(50,50, (Color) settingsObject.getSetting(Settings.settingsKeys[9]), carTwoControls);
        BallEntity ballEntity = new BallEntity(this.frame.getWidth()/2, this.frame.getHeight()/2, Color.BLACK);
        car1.setCollisions(new int[] {car2.hashCode(), ballEntity.hashCode()});
        car2.setCollisions(new int[] {car1.hashCode(), ballEntity.hashCode()});
        ballEntity.setCollisions(new int[] {car2.hashCode(), car1.hashCode()});
        entityHandler.addEntity(car1);
        entityHandler.addEntity(car2);
        entityHandler.addEntity(ballEntity);
        while (running){
            entityHandler.run();
        }
    }

    private void reDraw(){
        while(running){
            this.paint();
        }
    }

    private void paint() {
        calculateFPS();
        frame.getGraphics().drawImage(entityHandler.drawFrame(frame.createImage(frame.getWidth(),frame.getHeight()),String.valueOf(fps)),0,0,null);
    }


    private void calculateFPS(){
        long now = System.nanoTime();
        this.fps = 1000000000 / (now - lastTime);
        lastTime = now;
    }

    public void exit() {
        this.running = false;
        back.run();
    }
}