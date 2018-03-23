import javafx.scene.input.KeyCode;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyHandler implements KeyListener {
    private ArrayList<Key> trackedKeys;
    public KeyHandler(int[] valuesToTrack){
        trackedKeys = new ArrayList<>(valuesToTrack.length);
        for(int keyCode : valuesToTrack){
            trackedKeys.add(new Key(keyCode));
            System.out.println("Added key " + keyCode);
        }
    }

    public void setKey(KeyEvent event, boolean pressed){
        for(Key key : trackedKeys){
            if(key.getKeyCode() == event.getKeyCode())
                key.setPressed(pressed);
        }
    }

    public int getKeyPressed(int keyCode){
        for(Key key : trackedKeys){
            if(key.getKeyCode() == keyCode)
                return key.getPressed() ? 1 : 0;
        }
        return -1;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        setKey(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        setKey(e, false);
    }

    private class Key{
        private int keyCode;
        private volatile boolean pressed = false;
        public Key(int keyCode){
            this.keyCode = keyCode;
        }

        public void setPressed(boolean pressed){
            this.pressed = pressed;
        }

        public boolean getPressed(){
            return this.pressed;
        }

        public int getKeyCode(){
            return this.keyCode;
        }
    }
}
