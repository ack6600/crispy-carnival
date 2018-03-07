import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class League extends JFrame implements KeyListener{
    private Thread gameThread;
    private Thread drawThread;
    private KeyHandler keyHandler;
    private int[] controls = {
            KeyEvent.VK_UP,
            KeyEvent.VK_DOWN,
            KeyEvent.VK_RIGHT,
            KeyEvent.VK_LEFT};
    private double charX = 50;
    private double charY = 50;
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
        this.addKeyListener(this);
        keyHandler = new KeyHandler(controls);
        gameThread = new Thread(this::updatePhysics);
        drawThread = new Thread(this::reDraw);
    }

    public void start(){
        this.setVisible(true);
        gameThread.start();
        drawThread.start();
    }

    private void updatePhysics(){
        while (true){
            if(keyHandler.getKeyPressed(controls[0]) == 1){
                charY += 0.1;
                System.out.println("yo");
            }
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
        g.drawImage(drawFrame(createImage(this.getWidth(),this.getHeight())),0,0,null);
    }

    private void calculateFPS(){
        long now = System.nanoTime();
        this.fps = 1000000000 / (now - lastTime);
        lastTime = now;
    }

    private Image drawFrame(Image frame){
        Graphics graphics = frame.getGraphics();
        graphics.drawRect((int) charX, (int) charY, 20, 20);
        graphics.drawString(String.valueOf(fps), 10,42);
        return frame;
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyHandler.setKey(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyHandler.setKey(e, false);
    }
}