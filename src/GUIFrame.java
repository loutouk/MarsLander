import javax.swing.*;

public class GUIFrame extends JFrame {

    private SpaceShuttle physicObject;

    public GUIFrame(String frameName, SpaceShuttle physicObject){
        super(frameName);
        this.physicObject = physicObject;
    }
}
