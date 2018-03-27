import javax.swing.*;
import java.awt.*;

public class League extends JFrame{
    private Thread gameThread;
    private Thread drawThread;
    private KeyHandler keyHandler;
    private EntityHandler entityHandler;
    private long lastTime = System.nanoTime();
    private long fps = 0;
    public static void main(String[] args){
        League league = new League(1280,720);
        league.start();
    }

    public League(int width, int height){
        super("League");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setResizable(true);
        keyHandler = new KeyHandler();
        entityHandler = new EntityHandler(keyHandler);
        gameThread = new Thread(this::updatePhysics);
        drawThread = new Thread(this::reDraw);
        this.addKeyListener(keyHandler);
    }

    public void start(){
        this.setVisible(true);
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
            this.repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        calculateFPS();
        g.drawImage(entityHandler.drawFrame(createImage(this.getWidth(),this.getHeight()),String.valueOf(fps)),0,0,null);
//        g.drawString(String.valueOf(fps), 10,42);
    }


    private void calculateFPS(){
        long now = System.nanoTime();
        this.fps = 1000000000 / (now - lastTime);
        lastTime = now;
    }
}