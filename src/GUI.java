import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JPanel {

    private Image landerImage;

    private SpaceShuttle physicObject;
    private ArrayList<Vector> groundCoordinates;
    private static Color whiteColor = new Color(255, 255, 255, 150);
    private static Font monoFont = new Font("Monospaced", Font.BOLD, 16);

    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0,1400,800);
        g.setFont(monoFont);
        g.setColor(whiteColor);
        g.drawString("TIME:" + String.format("%.2f", Main.time) + " sec", 50,100);
        g.drawString("ALTITUDE:" + String.format("%.2f", physicObject.position.y), 50,125);
        g.drawString("POSITION:" + String.format("%.2f", physicObject.position.x), 50,150);
        g.drawString("FUEL: " + physicObject.fuel, 50,350);
        g.drawString("V SPEED:" + String.format("%.2f", physicObject.velocity.y), 50,375);
        g.drawString("H SPEED:" + String.format("%.2f", physicObject.velocity.x), 50,400);
        g.drawString("TOUCH DOWN:" + Main.hasTouchGround, 50,500);
        g.drawString("ANGLE:" + physicObject.angle, 50,550);

        g.setColor(Color.red);
        for(int i=1 ; i<groundCoordinates.size() ; i++){
            g.drawLine( (int)groundCoordinates.get(i-1).x, (int)groundCoordinates.get(i-1).y,
                    (int)groundCoordinates.get(i).x,(int)groundCoordinates.get(i).y);
        }
        final Graphics2D gImg = (Graphics2D)g;
        gImg.setColor(Color.ORANGE);
        gImg.rotate(Math.toRadians(physicObject.angle),physicObject.position.x,physicObject.position.y);
        gImg.drawImage(landerImage,((int)physicObject.position.x)-landerImage.getWidth(null)/2, ((int)(physicObject.position.y))-landerImage.getHeight(null), null);
        gImg.fillRect((int)physicObject.position.x-2, (int)(physicObject.position.y),4,(int)physicObject.lastThrust*10);
    }

    public GUI(SpaceShuttle physicObject, ArrayList<Vector> groundCoordinates){

        try { landerImage = ImageIO.read(new File("lander.png"));
        } catch (IOException e) {}

        this.physicObject = physicObject;
        this.groundCoordinates = groundCoordinates;
    }


}