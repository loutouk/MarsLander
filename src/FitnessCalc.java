
public class FitnessCalc {

    static double getFitness(NavigationIndividual individual) {

        SpaceShuttle physicObject = new SpaceShuttle(Main.mass,
                new Vector(Main.initialPos.x, Main.initialPos.y),
                new Vector(Main.initialVelocity.x,Main.initialVelocity.y));

        int fitness=4000;
        int lastAngle=0;
        double lastPosX = physicObject.position.x;
        double lastPosY = physicObject.position.y;

        for(int i=0 ; i<individual.getGenes().size() ; i++){

            physicObject.netForce = new Vector(Main.gForceMars.x, Main.gForceMars.y);
            double elapsedTime = 1;
            physicObject.thrust(individual.getGenes().getTerm(i)[0]);
            physicObject.rotate(individual.getGenes().getTerm(i)[1]);
            physicObject.update(elapsedTime);

            if(Vector.isLineCrossingOther(new Vector(lastPosX,lastPosY),physicObject.position,Main.groundCoord)) {

                if(physicObject.position.x < Main.groundStart.x) {
                    fitness+=(Main.groundStart.x-physicObject.position.x);
                } else if(physicObject.position.x >Main.groundEnd.x) {
                    fitness+=(physicObject.position.x-Main.groundEnd.x);
                } else {
                    fitness-=1000;
                }

                if(physicObject.velocity.y > SpaceShuttle.MAX_LANDING_VSPEED) {
                    fitness += physicObject.velocity.y;
                } else {
                    fitness-=1000;
                }

                if(physicObject.velocity.x > SpaceShuttle.MAX_LANDING_HSPEED) {
                    fitness += physicObject.velocity.x;
                } else {
                    fitness-=1000;
                }


                if(lastAngle == 0 && physicObject.angle == 0) {
                    fitness-=1000;
                } else {
                    fitness += Math.abs(physicObject.angle) + Math.abs(lastAngle);
                }

                return fitness;

            }


            if(physicObject.position.y > 768 && physicObject.position.x < 0) {
                return 10000;
            }else if(physicObject.position.y > 768 && physicObject.position.x > 1368) {
                return 10000;
            }

            lastPosX = physicObject.position.x;
            lastPosY = physicObject.position.y;
            lastAngle=physicObject.angle;

        }

        return fitness;
    }
}