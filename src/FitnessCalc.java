import java.util.ArrayList;

public class FitnessCalc {

    static double mass = 1;
    static double xStart = 500;
    static double yStart = 50;
    static Vector gForceMars = new Vector(0, 3.711*mass);

    static double getFitness(NavigationIndividual individual) {

        int fitness=10000;
        int lastAngle=0;
        SpaceShuttle physicObject = new SpaceShuttle(mass, new Vector(xStart,yStart));

        for(int i=0 ; i<individual.getGenes().size() ; i++){


            int currentFitness = fitness;

            physicObject.netForce = new Vector(gForceMars.x, gForceMars.y);
            double elapsedTime = 1;
            physicObject.thrust(individual.getGenes().getTerm(i)[0]);
            physicObject.rotate(individual.getGenes().getTerm(i)[1]);
            physicObject.update(elapsedTime);


            fitness+=physicObject.velocity.y;


            lastAngle=physicObject.angle;

        }

        return fitness;
    }
}