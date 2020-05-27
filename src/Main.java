import javax.swing.*;
import java.util.ArrayList;

public class Main {

    public static Vector groundStart;
    public static Vector groundEnd;
    public static ArrayList<Vector> groundCoord;

    public static double time = 0;
    public static boolean hasTouchGround = false;
    public static int generationCount = 0;

    public static double mass = 1.0;
    public static double xStart;
    public static double yStart;
    public static double xVelStart;
    public static double yVelStart;
    public static int initialRotation;
    public static Vector initialPos=null;
    public static Vector initialVelocity=null;
    public static Vector gForceMars = new Vector(0.0*mass, -3.711*mass);
    public static SpaceShuttle physicObject=null;

    private static GUI gui;

    public static void main(String[] args){

        groundCoord = generateGround(7);

        // Identify landing zone
        int lastX = (int) groundCoord.get(0).x;
        int lastY = (int) groundCoord.get(0).x;
        for(int i=0 ; i<groundCoord.size() ; i++){
            if(i!=0) {
                if(groundCoord.get(i).y == lastY && groundCoord.get(i).x-lastX >= SpaceShuttle.MIN_LANDING_WIDTH) {
                    groundStart = new Vector(lastX,lastY);
                    groundEnd = new Vector(groundCoord.get(i).x,groundCoord.get(i).y);
                }else{
                    lastX = (int) groundCoord.get(i).x;
                    lastY = (int) groundCoord.get(i).y;
                }
            }
        }

        GUIFrame f = new GUIFrame("Mars Lander", physicObject);
        gui = new GUI(physicObject, groundCoord);
        f.getContentPane().add(gui);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setVisible(true);

        NavigationIndividual bestCandidate = evolve();

        double lastPosX = physicObject.position.x;
        double lastPosY = physicObject.position.y;

        for(int i=0 ; i<bestCandidate.getGenes().size() ; i++) {
            physicObject.netForce = new Vector(gForceMars.x, gForceMars.y);
            try { Thread.sleep(250); } catch (InterruptedException e) { e.printStackTrace(); }
            double elapsedTime = 1.0;


            physicObject.thrust(bestCandidate.getGene(i)[0]);
            physicObject.fuel-=bestCandidate.getGene(i)[0];
            physicObject.rotate(bestCandidate.getGene(i)[1]);


            time+=elapsedTime;
            physicObject.update(elapsedTime);
            f.repaint();
            if(Vector.isLineCrossingOther(new Vector(lastPosX,lastPosY),physicObject.position,groundCoord)!=null) {
                hasTouchGround = true;
                break;
            }
            lastPosX = physicObject.position.x;
            lastPosY = physicObject.position.y;
        }

    }

    private static ArrayList<Vector> generateGround(int levelNumber) {

        groundCoord = new ArrayList<>();

        if(levelNumber==0){
            xStart = 2500;
            yStart = 2700;
            xVelStart = 0.0;
            yVelStart = 0.0;
            initialRotation = 0;
            initialPos = new Vector(xStart, yStart);
            initialVelocity = new Vector(xVelStart*mass, yVelStart*mass);
            physicObject = new SpaceShuttle(mass, initialPos, initialVelocity, initialRotation);
            groundCoord.add(new Vector(0,100));
            groundCoord.add(new Vector(1000,500));
            groundCoord.add(new Vector(1500,1500));
            groundCoord.add(new Vector(3000 ,1000 ));
            groundCoord.add(new Vector(4000 ,150));
            groundCoord.add(new Vector(5500 ,150));
            groundCoord.add(new Vector(6999  ,800));
        }else if(levelNumber==1){
            xStart = 6500;
            yStart = 2800;
            xVelStart = -100.0;
            yVelStart = 0.0;
            initialRotation = -90;
            initialPos = new Vector(xStart, yStart);
            initialVelocity = new Vector(xVelStart*mass, yVelStart*mass);
            physicObject = new SpaceShuttle(mass, initialPos, initialVelocity, initialRotation);
            groundCoord.add(new Vector(0,100));
            groundCoord.add(new Vector(1000,500));
            groundCoord.add(new Vector(1500,100));
            groundCoord.add(new Vector(3000 ,100 ));
            groundCoord.add(new Vector(3500 ,500));
            groundCoord.add(new Vector(3700 ,200));
            groundCoord.add(new Vector(5000  ,1500));
            groundCoord.add(new Vector(5800  ,300));
            groundCoord.add(new Vector(6000  ,1000));
            groundCoord.add(new Vector(6999  ,2000));
        }else if(levelNumber==2){
            xStart = 500;
            yStart = 2700;
            xVelStart = 100.0;
            yVelStart = 0.0;
            initialRotation = 90;
            initialPos = new Vector(xStart, yStart);
            initialVelocity = new Vector(xVelStart*mass, yVelStart*mass);
            physicObject = new SpaceShuttle(mass, initialPos, initialVelocity, initialRotation);
            groundCoord.add(new Vector(0,1000));
            groundCoord.add(new Vector(300,1500));
            groundCoord.add(new Vector(350,1400));
            groundCoord.add(new Vector(500 ,2000));
            groundCoord.add(new Vector(800 ,1800));
            groundCoord.add(new Vector(1000 ,2500));
            groundCoord.add(new Vector(1200  ,2100));
            groundCoord.add(new Vector(1500  ,2400));
            groundCoord.add(new Vector(2000  ,1000));
            groundCoord.add(new Vector(2200  ,500));
            groundCoord.add(new Vector(2500 ,100));
            groundCoord.add(new Vector(2900 ,800));
            groundCoord.add(new Vector(3000 ,500));
            groundCoord.add(new Vector(3200  ,1000));
            groundCoord.add(new Vector(3500  ,2000));
            groundCoord.add(new Vector(3800  ,800));
            groundCoord.add(new Vector(4000  ,200));
            groundCoord.add(new Vector(5000  ,200));
            groundCoord.add(new Vector(5500  ,1500));
            groundCoord.add(new Vector(6999  ,2800));
        }else if(levelNumber==3){
            xStart = 6500;
            yStart = 2700;
            xVelStart = -50.0;
            yVelStart = 0.0;
            initialRotation = -90;
            initialPos = new Vector(xStart, yStart);
            initialVelocity = new Vector(xVelStart*mass, yVelStart*mass);
            physicObject = new SpaceShuttle(mass, initialPos, initialVelocity, initialRotation);
            groundCoord.add(new Vector(0,100));
            groundCoord.add(new Vector(300,1500));
            groundCoord.add(new Vector(350,1400));
            groundCoord.add(new Vector(500 ,2100 ));
            groundCoord.add(new Vector(1500 ,2100));
            groundCoord.add(new Vector(2000 ,200));
            groundCoord.add(new Vector(2500  ,500));
            groundCoord.add(new Vector(2900  ,300));
            groundCoord.add(new Vector(3000  ,200));
            groundCoord.add(new Vector(3200  ,1000));
            groundCoord.add(new Vector(3500 ,500 ));
            groundCoord.add(new Vector(3800 ,800));
            groundCoord.add(new Vector(4000 ,200));
            groundCoord.add(new Vector(4200  ,800));
            groundCoord.add(new Vector(4800  ,600));
            groundCoord.add(new Vector(5000  ,1200));
            groundCoord.add(new Vector(5500  ,900));
            groundCoord.add(new Vector(6000  ,500));
            groundCoord.add(new Vector(6500  ,300));
            groundCoord.add(new Vector(6999  ,500));
        }else if(levelNumber==4){
            xStart = 6500;
            yStart = 2600;
            xVelStart = -20.0;
            yVelStart = 0.0;
            initialRotation = -45;
            initialPos = new Vector(xStart, yStart);
            initialVelocity = new Vector(xVelStart*mass, yVelStart*mass);
            physicObject = new SpaceShuttle(mass, initialPos, initialVelocity, initialRotation);
            groundCoord.add(new Vector(0,450));
            groundCoord.add(new Vector(300,750));
            groundCoord.add(new Vector(1000,450));
            groundCoord.add(new Vector(1500 ,650 ));
            groundCoord.add(new Vector(1800 ,850));
            groundCoord.add(new Vector(2000 ,1950));
            groundCoord.add(new Vector(2200  ,1850));
            groundCoord.add(new Vector(2400  ,2000));
            groundCoord.add(new Vector(3100  ,1800));
            groundCoord.add(new Vector(3150  ,1550));
            groundCoord.add(new Vector(2500 ,1600));
            groundCoord.add(new Vector(2200 ,1650));
            groundCoord.add(new Vector(2100  ,750));
            groundCoord.add(new Vector(2200  ,150));
            groundCoord.add(new Vector(3200  ,150));
            groundCoord.add(new Vector(3500  ,450));
            groundCoord.add(new Vector(4000  ,950));
            groundCoord.add(new Vector(4500  ,1450));
            groundCoord.add(new Vector(5000  ,1550));
            groundCoord.add(new Vector(5500  ,1500));
            groundCoord.add(new Vector(6000  ,950));
            groundCoord.add(new Vector(6999  ,1750));
        }else if(levelNumber==5){
            xStart = 6500;
            yStart = 2000;
            xVelStart = -20.0;
            yVelStart = 0.0;
            initialRotation = 0;
            initialPos = new Vector(xStart, yStart);
            initialVelocity = new Vector(xVelStart*mass, yVelStart*mass);
            physicObject = new SpaceShuttle(mass, initialPos, initialVelocity, initialRotation);
            groundCoord.add(new Vector(0,1800));
            groundCoord.add(new Vector(300,1200));
            groundCoord.add(new Vector(1000,1550));
            groundCoord.add(new Vector(2000 ,1200 ));
            groundCoord.add(new Vector(2500 ,1650));
            groundCoord.add(new Vector(3700 ,220));
            groundCoord.add(new Vector(4700  ,220));
            groundCoord.add(new Vector(4750  ,1000));
            groundCoord.add(new Vector(4700  ,1650));
            groundCoord.add(new Vector(4000  ,1700));
            groundCoord.add(new Vector(3700 ,1600));
            groundCoord.add(new Vector(3750 ,1900));
            groundCoord.add(new Vector(4000  ,2100));
            groundCoord.add(new Vector(4900  ,2050));
            groundCoord.add(new Vector(5100  ,1000));
            groundCoord.add(new Vector(5500  ,500));
            groundCoord.add(new Vector(6200  ,800));
            groundCoord.add(new Vector(6999  ,600));
        }else if(levelNumber==6){
            xStart = 6500;
            yStart = 2800;
            xVelStart = -90.0;
            yVelStart = 0.0;
            initialRotation = -90;
            initialPos = new Vector(xStart, yStart);
            initialVelocity = new Vector(xVelStart*mass, yVelStart*mass);
            physicObject = new SpaceShuttle(mass, initialPos, initialVelocity, initialRotation);
            groundCoord.add(new Vector(0,100));
            groundCoord.add(new Vector(1000,500));
            groundCoord.add(new Vector(1500,1500));
            groundCoord.add(new Vector(3000,1000));
            groundCoord.add(new Vector(4000,150));
            groundCoord.add(new Vector(5500,150));
            groundCoord.add(new Vector(6999,800));
        }else if(levelNumber==7){
            xStart = 6000;
            yStart = 1500;
            xVelStart = 0.;
            yVelStart = 0.0;
            initialRotation = 0;
            initialPos = new Vector(xStart, yStart);
            initialVelocity = new Vector(xVelStart*mass, yVelStart*mass);
            physicObject = new SpaceShuttle(mass, initialPos, initialVelocity, initialRotation);
            groundCoord.add(new Vector(0,2500));
            groundCoord.add(new Vector(100,200));
            groundCoord.add(new Vector(500,150));
            groundCoord.add(new Vector(1000,2000));
            groundCoord.add(new Vector(2000,2000));
            groundCoord.add(new Vector(2200,800));
            groundCoord.add(new Vector(2500,200));
            groundCoord.add(new Vector(6899,300));
            groundCoord.add(new Vector(6999,2500));
            groundCoord.add(new Vector(4100,2600));
            groundCoord.add(new Vector(4200,1000));
            groundCoord.add(new Vector(3500,800));
            groundCoord.add(new Vector(3100,1100));
            groundCoord.add(new Vector(3400,2900));
            groundCoord.add(new Vector(6999,3000));
        }else if(levelNumber==8){
            xStart = 6000;
            yStart = 3000;
            xVelStart = 0.;
            yVelStart = 0.0;
            initialRotation = -50;
            initialPos = new Vector(xStart, yStart);
            initialVelocity = new Vector(xVelStart*mass, yVelStart*mass);
            physicObject = new SpaceShuttle(mass, initialPos, initialVelocity, initialRotation);
            groundCoord.add(new Vector(0,2500));
            groundCoord.add(new Vector(100,1000));
            groundCoord.add(new Vector(2000,800));
            groundCoord.add(new Vector(2100,100));
            groundCoord.add(new Vector(3100,100));
            groundCoord.add(new Vector(3200,1500));
            groundCoord.add(new Vector(1500,1600));
            groundCoord.add(new Vector(1500,1800));
            groundCoord.add(new Vector(4000,1700));
            groundCoord.add(new Vector(4100,100));
            groundCoord.add(new Vector(6999,200));
        }else{
            System.err.println("Error. Level " + levelNumber + " is not implemented.");
        }

        return groundCoord;
    }

    public static NavigationIndividual evolve() {
        // Create an initial population
        Population myPop = new Population(Algorithm.popSize, true);

        // Evolve our population until we reach an optimum solution
        generationCount = 0;
        while (generationCount < Algorithm.generationCount && Math.round(myPop.getFittest(1)[0].getFitness()) != FitnessCalc.maxFitness) {

            myPop = Algorithm.evolvePopulation(myPop);
            NavigationIndividual bestCandidate = (NavigationIndividual)myPop.getFittest(1)[0];

            SpaceShuttle physicObject = new SpaceShuttle(
                    mass,
                    new Vector(initialPos.x, initialPos.y),
                    new Vector(initialVelocity.x, initialVelocity.y),
                    initialRotation);

            double lastPosX = physicObject.position.x;
            double lastPosY = physicObject.position.y;

            for(int i=0 ; i<bestCandidate.getGenes().size() ; i++) {

                physicObject.netForce = new Vector(gForceMars.x, gForceMars.y);
                double elapsedTime = 1.0;
                physicObject.thrust(bestCandidate.getGene(i)[0]);
                physicObject.fuel-=bestCandidate.getGene(i)[0];
                physicObject.rotate(bestCandidate.getGene(i)[1]);
                physicObject.update(elapsedTime);

                if(Vector.isLineCrossingOther(new Vector(lastPosX,lastPosY),physicObject.position,groundCoord)!=null) {
                    break;
                }

                gui.drawPath(physicObject,(int)lastPosX,(int)lastPosY,generationCount);

                lastPosX = physicObject.position.x;
                lastPosY = physicObject.position.y;
            }

            generationCount++;
        }
        if (Math.round(myPop.getFittest(1)[0].getFitness()) == FitnessCalc.maxFitness) {
            System.out.println("Solution found! Generation " + generationCount);
            return (NavigationIndividual) myPop.getFittest(1)[0];
        } else {
            System.out.println("No solution found. Best result:");
        }
        System.out.println("Generation: " + generationCount);
        System.out.print("Genes: ");
        System.out.println(myPop.getFittest(1)[0]);
        System.out.print("Fitness: ");
        System.out.println(Math.round(myPop.getFittest(1)[0].getFitness()));
        return(NavigationIndividual)myPop.getFittest(1)[0];
    }
}