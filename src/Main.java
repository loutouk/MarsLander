import javax.swing.*;
import java.util.ArrayList;

public class Main {

    public static Vector groundStart;
    public static Vector groundEnd;
    public static double time = 0;
    public static double mass = 1.0;
    public static double xStart = 500;
    public static double yStart = 50;
    public static double xVelStart = 0.0;
    public static double yVelStart = 0.0;
    public static boolean hasTouchGround = false;
    public static Vector gForceMars = new Vector(1.0*mass, 3.711*mass);
    public static Vector initialPos = new Vector(xStart, yStart);
    public static Vector initialVelocity = new Vector(xVelStart*mass, yVelStart*mass);
    public static ArrayList<Vector> groundCoord;
    public static int generationCount = 0;

    public static void main(String[] args){

        groundCoord = generateGround();

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


        SpaceShuttle physicObject = new SpaceShuttle(mass, initialPos, initialVelocity);
        GUIFrame f = new GUIFrame("Mars Lander", physicObject);
        GUI gui = new GUI(physicObject, groundCoord);
        f.getContentPane().add(gui);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setVisible(true);

        NavigationIndividual bestCandidate = evolve();

        double lastPosX = physicObject.position.x;
        double lastPosY = physicObject.position.y;
        for(int i=0 ; i<=bestCandidate.getGenes().size() ; i++) {
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

    private static ArrayList<Vector> generateGround() {
        groundCoord = new ArrayList<>();

        /*groundCoord.add(new Vector(0,768));
        groundCoord.add(new Vector(200,600));
        groundCoord.add(new Vector(1200,600));
        groundCoord.add(new Vector(1250,550));
        groundCoord.add(new Vector(1300,500));
        groundCoord.add(new Vector(1350,450));*/

        /*groundCoord.add(new Vector(0,900));
        groundCoord.add(new Vector(400,700));
        groundCoord.add(new Vector(1000 ,550));
        groundCoord.add(new Vector(2000,550));*/

        groundCoord.add(new Vector(0,900));
        groundCoord.add(new Vector(400,700));
        groundCoord.add(new Vector(750,200));
        groundCoord.add(new Vector(775,150));
        groundCoord.add(new Vector(800,700));
        groundCoord.add(new Vector(920,720));
        groundCoord.add(new Vector(995,290));
        groundCoord.add(new Vector(1000 ,650));
        groundCoord.add(new Vector(2000,650));
        groundCoord.add(new Vector(1050,220));
        groundCoord.add(new Vector(1400,300));

        /*groundCoord.add(new Vector(0,750));
        groundCoord.add(new Vector(100,650));
        groundCoord.add(new Vector(200,550));
        groundCoord.add(new Vector(300,450));
        groundCoord.add(new Vector(400,450));
        groundCoord.add(new Vector(800,400));
        groundCoord.add(new Vector(1000,200));
        groundCoord.add(new Vector(2000,200));*/

        return groundCoord;
    }

    public static NavigationIndividual evolve() {

        boolean found = false;
        int maxTry = 10;
        int count=0;
        NavigationIndividual fittest = null;


        while(!found && ++count<maxTry) {
            // Create an initial population
            Population myPop = new Population(Algorithm.popSize, true);

            // Evolve our population until we reach an optimum solution
            generationCount = 0;
            while (generationCount < Algorithm.generationCount && Math.round(myPop.getFittest(1)[0].getFitness()) != 0) {
                generationCount++;
                //System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
                //GeneticAlgorithmPolynomial.IndividualExample current = (GeneticAlgorithmPolynomial.IndividualExample) myPop.getFittest();
                //System.out.println(current.getGenes());
                myPop = Algorithm.evolvePopulation(myPop);
            }
            if (Math.round(myPop.getFittest(1)[0].getFitness()) == 0) {
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
            NavigationIndividual tmp = (NavigationIndividual)myPop.getFittest(1)[0];

            if(fittest==null || tmp.getFitness()>fittest.getFitness()) fittest=tmp;
        }

        return fittest;
    }
}
