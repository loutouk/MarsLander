import java.util.Random;

public class FitnessCalc {

    public static final int maxFitness = 5000;

    static double getFitness(NavigationIndividual individual) {

        SpaceShuttle physicObject = new SpaceShuttle(Main.mass,
                new Vector(Main.initialPos.x, Main.initialPos.y),
                new Vector(Main.initialVelocity.x,Main.initialVelocity.y),
                Main.initialRotation);

        int fitness=0;
        int lastAngle=0;
        double lastPosX = physicObject.position.x;
        double lastPosY = physicObject.position.y;

        double middleX = (Main.groundEnd.x + Main.groundStart.x) / 2;

        int fulfilledBasics = 0;

        for(int i=0 ; i<individual.getGenes().size() ; i++){

            physicObject.netForce = new Vector(Main.gForceMars.x, Main.gForceMars.y);
            double elapsedTime = 1;
            physicObject.thrust(individual.getGenes().getTerm(i)[0]);
            physicObject.rotate(individual.getGenes().getTerm(i)[1]);
            physicObject.update(elapsedTime);

            Vector startTouchedGround = Vector.isLineCrossingOther(
                    new Vector(lastPosX,lastPosY),physicObject.position,Main.groundCoord);


            if(startTouchedGround!=null) {

                if(physicObject.position.x < Main.groundStart.x || physicObject.position.x > Main.groundEnd.x) {
                    fitness-=Math.abs(middleX-physicObject.position.x);
                }else{
                    fulfilledBasics++;
                    fitness+=1000;
                }

                if(startTouchedGround.y == Main.groundStart.y){
                    fulfilledBasics++;
                    fitness+=1000;
                } else{
                    fitness-=Math.abs(physicObject.position.y-Main.groundStart.y);
                }

                if(fulfilledBasics<2 || physicObject.velocity.y < SpaceShuttle.MAX_LANDING_VSPEED) {
                    fitness += physicObject.velocity.y;
                } else {
                    fitness+=1000;
                    fulfilledBasics++;
                }

                if(fulfilledBasics<3 || Math.abs(physicObject.velocity.x) > SpaceShuttle.MAX_LANDING_HSPEED) {
                    fitness -= Math.abs(physicObject.velocity.x);
                } else {
                    fitness+=1000;
                    fulfilledBasics++;
                }


                if(fulfilledBasics>=4 && lastAngle == 0 && physicObject.angle == 0) {
                    fitness+=1000;
                } else if(fulfilledBasics>=4) {
                    fitness -= Math.abs(physicObject.angle) + Math.abs(lastAngle);
                }

                // Max landing vSpeed is 40 & 40-8=32 & 32 squared equals 1024 which is equivalent to a basic fulfilment reward
                // fitness -= Math.pow(physicObject.velocity.y+8,2) is for prompting low vSpeed

                if(fitness<maxFitness && physicObject.velocity.y<0) fitness -= Math.pow(physicObject.velocity.y+8,2);

                return fitness;

            } else if(physicObject.position.x<0 || physicObject.position.x > 6999) {
                return - Math.abs(physicObject.position.x-middleX) * 1000;
            }

            lastPosX = physicObject.position.x;
            lastPosY = physicObject.position.y;
            lastAngle=physicObject.angle;

        }

        return fitness;
    }
}