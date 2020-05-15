import javax.swing.*;
import java.util.ArrayList;

public class Main {

    public static Vector groundStart;
    public static Vector groundEnd;
    public static double time = 0;
    public static double mass = 1;
    public static double xStart = 500;
    public static double yStart = 50;
    public static boolean hasTouchGround = false;
    public static ArrayList<Vector> groundCoord;

    public static void main(String[] args){

        groundCoord = generateGround();

        int lastX = (int) groundCoord.get(0).x;
        int lastY = (int) groundCoord.get(0).x;
        for(int i=0 ; i<groundCoord.size() ; i++){
            if(i!=0) {
                if(groundCoord.get(i).y == lastY && groundCoord.get(i).x-lastX >= SpaceShuttle.MIN_LANDING_WIDTH) {
                    groundStart = new Vector(lastX,lastY);
                    groundEnd = new Vector(groundCoord.get(i).x,groundCoord.get(i).y);
                    System.out.println(groundStart.x +" "+groundStart.y);
                    System.out.println(groundEnd.x +" "+groundEnd.y);
                    i=groundCoord.size();
                }else{
                    lastX = (int) groundCoord.get(i).x;
                    lastY = (int) groundCoord.get(i).y;
                }
            }
        }

        NavigationIndividual bestCandidate = evolve();

        Vector gForceMars = new Vector(0, 3.711*mass);

        SpaceShuttle physicObject = new SpaceShuttle(mass, new Vector(xStart,yStart));

        GUIFrame f = new GUIFrame("Mars Lander", physicObject);
        GUI gui = new GUI(physicObject, groundCoord);
        f.getContentPane().add(gui);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setVisible(true);

        for(int i=0 ; i<=bestCandidate.getLandingTime() ; i++) {

            physicObject.netForce = new Vector(gForceMars.x, gForceMars.y);


            try { Thread.sleep(250); } catch (InterruptedException e) { e.printStackTrace(); }

            double elapsedTime = 1.0;

            physicObject.thrust(bestCandidate.getGene(i)[0]);
            physicObject.fuel-=bestCandidate.getGene(i)[0];
            physicObject.rotate(bestCandidate.getGene(i)[1]);

            System.out.println(bestCandidate.getGene(i)[0] + " " + bestCandidate.getGene(i)[1]);

            time+=elapsedTime;
            physicObject.update(elapsedTime);

            //hasTouchGround = checkTouchGround(physicObject,groundCoord);
            //System.out.println("TIME " + time + "\tVSPEED " + physicObject.velocity.y + "\tPOS " + (yStart-(physicObject.position.y-yStart)));
            f.repaint();

        }

    }

    private static ArrayList<Vector> generateGround() {
        ArrayList<Vector> groundCoord = new ArrayList<>();
        /*groundCoord.add(new Vector(0,900));
        groundCoord.add(new Vector(400,650));
        groundCoord.add(new Vector(700,650));
        groundCoord.add(new Vector(900,750));
        groundCoord.add(new Vector(950,600));
        groundCoord.add(new Vector(2000,600));*/
        groundCoord.add(new Vector(0,900));
        groundCoord.add(new Vector(400,650));
        groundCoord.add(new Vector(700,650));
        groundCoord.add(new Vector(900,750));
        groundCoord.add(new Vector(1100,600));
        groundCoord.add(new Vector(2100,600));
        return groundCoord;
    }

    public static NavigationIndividual evolve() {

        // Create an initial population
        Population myPop = new Population(Algorithm.popSize, true);

        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        while (generationCount < Algorithm.generationCount && Math.round(myPop.getFittest(1)[0].getFitness()) != 0) {
            generationCount++;
            //System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
            //GeneticAlgorithmPolynomial.IndividualExample current = (GeneticAlgorithmPolynomial.IndividualExample) myPop.getFittest();
            //System.out.println(current.getGenes());
            myPop = Algorithm.evolvePopulation(myPop);
        }
        if (Math.round(myPop.getFittest(1)[0].getFitness()) == 0) {
            System.out.println("Solution found!");
        } else {
            System.out.println("No solution found. Best result:");
        }
        System.out.println("Generation: " + generationCount);
        System.out.print("Genes: ");
        System.out.println(myPop.getFittest(1)[0]);
        System.out.print("Fitness: ");
        System.out.println(Math.round(myPop.getFittest(1)[0].getFitness()));
        return (NavigationIndividual)myPop.getFittest(1)[0];
    }

    public static void render(){
        ArrayList<Vector> groundCoord = new ArrayList<>();
        groundCoord.add(new Vector(0,900));
        groundCoord.add(new Vector(400,650));
        groundCoord.add(new Vector(700,650));
        groundCoord.add(new Vector(1100,750));
        groundCoord.add(new Vector(1400,800));

        //Vector gForceEarth = new Vector(0, 9.8*mass);
        Vector gForceMars = new Vector(0, 3.711*mass);

        SpaceShuttle physicObject = new SpaceShuttle(mass, new Vector(xStart,yStart));

        GUIFrame f = new GUIFrame("Mars Lander", physicObject);
        GUI gui = new GUI(physicObject, groundCoord);
        f.getContentPane().add(gui);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setVisible(true);

        while (true) {

            physicObject.netForce = new Vector(gForceMars.x, gForceMars.y);


            try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }

            double elapsedTime = 0.1;



            time+=elapsedTime;
            physicObject.update(elapsedTime);

            hasTouchGround = checkTouchGround(physicObject,groundCoord);

            //System.out.println("TIME " + time + "\tVSPEED " + physicObject.velocity.y + "\tPOS " + (yStart-(physicObject.position.y-yStart)));
            f.repaint();
        }
    }

    public static boolean checkTouchGround(PhysicObject physObj, ArrayList<Vector> groundCoord) {

        int groundVecBelow=-1;

        for(int i=1 ; i<groundCoord.size() ; i++){
            if(physObj.position.x>=groundCoord.get(i-1).x && physObj.position.x<=groundCoord.get(i).x){
                groundVecBelow = i-1;
                break;
            }
        }

        if(groundVecBelow==-1){
            // System.err.println("No ground has been detected under the shuttle. X:" + physObj.position.x + " Y:" + physObj.position.y);
        }else{
            // flat
            if(groundCoord.get(groundVecBelow).y == groundCoord.get(groundVecBelow+1).y) {
                return physObj.position.y > groundCoord.get(groundVecBelow).y;
            }
            // up hill
            else if(groundCoord.get(groundVecBelow).y > groundCoord.get(groundVecBelow+1).y){
                double yLength = -(groundCoord.get(groundVecBelow+1).y - groundCoord.get(groundVecBelow).y);
                double xLength = groundCoord.get(groundVecBelow+1).x - groundCoord.get(groundVecBelow).x;
                double radianAngle = Math.atan(yLength/xLength);
                double groundHeight = (physObj.position.x - groundCoord.get(groundVecBelow).x) * Math.tan(radianAngle);
                return groundHeight<physObj.position.y;
            }
            // down hill
            else if(groundCoord.get(groundVecBelow).y < groundCoord.get(groundVecBelow+1).y) {
                double yLength = -(groundCoord.get(groundVecBelow).y - groundCoord.get(groundVecBelow+1).y);
                double xLength = groundCoord.get(groundVecBelow+1).x - groundCoord.get(groundVecBelow).x;
                double radianAngle = Math.atan(yLength/xLength);
                double groundHeight = (physObj.position.x - groundCoord.get(groundVecBelow).x) * Math.tan(radianAngle);
                return groundHeight<physObj.position.y;
            }
        }

        return false;
    }
}
