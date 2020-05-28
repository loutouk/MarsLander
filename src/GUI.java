import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GUI extends JPanel {

    private static final int displayOffset = 10;

    // Tinker with these variables to have the landscape fit your screen
    private static final double displayDividingFactor = 5.2;
    private static final int screenHeight = 768 - displayOffset;
    private static final int screenWidth = 1366;

    public static final int searchSpaceWidth = 30;
    public static final int searchSpaceHeight = 20;
    public static final double drawSearchSpaceFrequency = 0.5; // Between 0 and 1, the higher the more often
    private static final boolean showSearchSpace = true;

    // Do not touch these variables
    private static final int cellsXLength = screenWidth/searchSpaceWidth;
    private static final int cellsYLength = screenHeight/searchSpaceHeight;

    private Image landerImage;

    private SpaceShuttle physicObject;
    private ArrayList<Vector> groundCoordinates;
    private static Color whiteColor = new Color(255, 255, 255, 150);
    private static Font monoFont = new Font("Monospaced", Font.BOLD, 16);

    public void paint(Graphics g) {
        update(g);
    }

    public void drawPath(PhysicObject shuttle, int lastPosX, int lastPosY){
        Graphics g = this.getGraphics();

        if(showSearchSpace && new Random().nextDouble()<drawSearchSpaceFrequency) {
            int searchSpaceXIndex = (int)(shuttle.position.x/displayDividingFactor/screenWidth*searchSpaceWidth);
            int searchSpaceYIndex = (int)((screenHeight-(shuttle.position.y/displayDividingFactor))/screenHeight*searchSpaceHeight);
            g.setColor(new Color(255,0,255, 1));
            g.fillRect(Math.max(0,searchSpaceXIndex*cellsXLength-1),
                    Math.max(0,searchSpaceYIndex*cellsYLength-1),
                    cellsXLength,
                    cellsYLength);
        }


        g.setColor( new Color(
                    0,
                    Math.min(255,Math.abs((int)Math.abs(shuttle.velocity.x)*4)),
                    Math.min(255,Math.abs((int)Math.abs(shuttle.velocity.y)*4))));

        g.drawLine( (int)(shuttle.position.x/displayDividingFactor),
                (int)(screenHeight-(shuttle.position.y/displayDividingFactor)),
                (int)(lastPosX/displayDividingFactor),
                (int)(screenHeight-(lastPosY/displayDividingFactor)));

    }

    public void update(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0,0,1400,800);

        if(!Main.visualizationDone) {
            g.setColor(new Color(255,0,255,100));
            for(int i=0 ; i<searchSpaceWidth ; i++) {
                g.drawLine(i*cellsXLength, 0, i*cellsXLength, screenHeight);
            }
            for(int i=0 ; i<searchSpaceHeight ; i++) {
                g.drawLine(0, i*cellsYLength, screenWidth, i*cellsYLength);
            }
        }

        g.setFont(monoFont);
        g.setColor(whiteColor);
        g.drawString("TIME:" + String.format("%.2f", Main.time) + " sec", 50,100);
        g.drawString("ALTITUDE:" + String.format("%.2f", physicObject.position.y), 50,150);
        g.drawString("POSITION:" + String.format("%.2f", physicObject.position.x), 50,175);
        g.drawString("V SPEED:" + String.format("%.2f", physicObject.velocity.y), 50,225);
        g.drawString("H SPEED:" + String.format("%.2f", physicObject.velocity.x), 50,250);

        g.drawString("ANGLE:" + physicObject.angle, 50,300);
        g.drawString("THRUST:" + physicObject.lastThrust, 50,325);
        g.drawString("TOUCH DOWN:" + Main.hasTouchGround, 50,350);
        g.drawString("FUEL: " + physicObject.fuel, 50,375);

        g.drawString("GENERATION COUNT: " + Main.generationCount, 50,450);

        g.setColor(Color.red);

        for(int i=1 ; i<groundCoordinates.size() ; i++){
            g.drawLine( (int)(groundCoordinates.get(i-1).x/displayDividingFactor),
                        (int)(screenHeight-(groundCoordinates.get(i-1).y/displayDividingFactor)),
                        (int)(groundCoordinates.get(i).x/displayDividingFactor),
                        (int)(screenHeight-(groundCoordinates.get(i).y/displayDividingFactor)));
        }

        final Graphics2D gImg = (Graphics2D)g;

        gImg.setColor(Color.ORANGE);
        gImg.rotate(Math.toRadians(physicObject.angle),
                (int)(physicObject.position.x/displayDividingFactor),
                (int)(screenHeight-(physicObject.position.y/displayDividingFactor)));

        gImg.drawImage(landerImage,
                ((int)(physicObject.position.x/displayDividingFactor))-landerImage.getWidth(null)/2,
                ((int)(screenHeight-(physicObject.position.y/displayDividingFactor)))-landerImage.getHeight(null), null);

        if(!Main.hasTouchGround) {
            gImg.fillRect(
                    ((int)(physicObject.position.x/displayDividingFactor))-2,
                    ((int)(screenHeight-(physicObject.position.y/displayDividingFactor))),
                    4,
                    (int)physicObject.lastThrust*10);
        }
    }

    public GUI(SpaceShuttle physicObject, ArrayList<Vector> groundCoordinates){

        try { landerImage = ImageIO.read(new File("lander.png"));
        } catch (IOException e) {}

        this.physicObject = physicObject;
        this.groundCoordinates = groundCoordinates;
    }


}