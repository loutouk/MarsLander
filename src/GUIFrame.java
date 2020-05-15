import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUIFrame extends JFrame implements KeyListener {

    private SpaceShuttle physicObject;

    public GUIFrame(String frameName, SpaceShuttle physicObject){
        super(frameName);
        this.physicObject = physicObject;
        addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
            physicObject.thrust(1);
        }else if(e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
            physicObject.thrust(2);
        }else if(e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
            physicObject.thrust(3);
        }else if(e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
            physicObject.thrust(4);
        }else if(e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
            physicObject.rotate(10);
        }else if(e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
            physicObject.rotate(-10);
        }
    }
}
