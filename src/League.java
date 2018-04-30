import javafx.scene.effect.ImageInput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class League implements Runnable{
    private Thread gameThread;
    private Thread drawThread;
    private KeyHandler keyHandler;
    private EntityHandler entityHandler;
    private JFrame frame;
    private long lastTime = System.nanoTime();
    private long fps = 0;

    public static void main(String[] args){
        JFrame root = new JFrame("League");
        League league = new League(root,1280,720);
        Runnable test1 = () -> {
            System.out.println("OwO whats this");
            System.exit(0);
        };
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File("images/header.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        TitleScreen titleScreen = new TitleScreen(root, 1280, 720, new String[] {"Start", "OwO"}, new Runnable[] {league, test1}, logo);
        titleScreen.start();
    }

    public League(JFrame root, int width, int height){
        this.frame = root;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setResizable(true);
        keyHandler = new KeyHandler();
        entityHandler = new EntityHandler(keyHandler);
        entityHandler.setDebug(true);
        gameThread = new Thread(this::updatePhysics);
        drawThread = new Thread(this::reDraw);
        frame.addKeyListener(keyHandler);
    }

    @Override
    public void run(){
        frame.setVisible(true);
        gameThread.start();
        drawThread.start();
    }

    private void updatePhysics(){
        CarEntity car1 = new CarEntity(0,0, Color.RED, Controls.carOneControls);
        CarEntity car2 = new CarEntity(50,50, Color.BLUE, Controls.carTwoControls);
        BallEntity ballEntity = new BallEntity(this.frame.getWidth()/2, this.frame.getHeight()/2, Color.BLACK);
        car1.setCollisions(new int[] {car2.hashCode(), ballEntity.hashCode()});
        car2.setCollisions(new int[] {car1.hashCode(), ballEntity.hashCode()});
        ballEntity.setCollisions(new int[] {car2.hashCode(), car1.hashCode()});
        entityHandler.addEntity(car1);
        entityHandler.addEntity(car2);
        entityHandler.addEntity(ballEntity);
//        entityHandler.addEntity(new CarEntity(40,40));
//        entityHandler.addEntity(new CarEntity(60, 60));
        while (true){
            entityHandler.run();
        }
    }

    private void reDraw(){
        while(true){
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
}