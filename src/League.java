import javafx.scene.effect.ImageInput;

import javax.imageio.ImageIO;
import javax.swing.*;
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
        Runnable test2 = () -> {
            System.out.println("ur mum gay lol");
            System.exit(0);
        };
        Runnable test3 = () -> {
            System.out.println("My mom gay");
            System.exit(0);
        };
        Runnable test4 = () -> {
            System.out.println("our mom gay");
            System.exit(0);
        };
        Runnable test5 = () -> {
            System.out.println("stalin intensifies");
            System.exit(0);
        };
        Runnable test6 = () -> {
            System.out.println("gucci ganggggggg");
            System.exit(0);
        };
        Runnable test7 = () -> {
            System.out.println("我是爸爸");
            System.exit(0);
        };
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File("images/header.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        TitleScreen titleScreen = new TitleScreen(root, 1280, 720, new String[] {"Start", "OwO", "(Migos Adlib) MAMA","Mama 2", "Stalin mom","Bork","Lil Pump","Something Japanese"}, new Runnable[] {league, test1, test2, test3, test4, test5, test6, test7}, logo);
        titleScreen.start();
    }

    public League(JFrame root, int width, int height){
        this.frame = root;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setResizable(true);
        keyHandler = new KeyHandler();
        entityHandler = new EntityHandler(keyHandler);
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
        entityHandler.addEntity(new CarEntity(20, 20));
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