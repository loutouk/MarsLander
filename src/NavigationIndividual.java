import java.util.Random;

public class NavigationIndividual extends Individual {

    public static final int MAX_INSTRUCTIONS = 100;
    private NavigationInstructions genes;
    private double fitness = -1;

    // Angle change is capped by a constant (15 degrees) so rotation is often done under several turns
    // and a typical behaviour for the shuttle is to maintain its rotation for many turns
    // and then rotate for fewer turns
    // rotation probability should be low, but heighten when we just previously rotate
    public static final double CHANGE_ANGLE_PROB = 0.01;
    public static final double CHANGE_ANGLE_AGAIN_PROB = 0.85;
    public static boolean hasRotated = true; // the initial value will foster or not change in rotation

    public NavigationIndividual() {

        int[][] array = new int[MAX_INSTRUCTIONS][2];
        int lastThrustValue=0;
        int lastAngleValue=0;

        for(int timePeriod=0 ; timePeriod<MAX_INSTRUCTIONS ; timePeriod++){
            lastThrustValue = generateThrust(lastThrustValue);
            lastAngleValue = generateAngle(lastAngleValue);

            array[timePeriod][0] = lastThrustValue;
            array[timePeriod][1] = lastAngleValue;
        }

        genes = new NavigationInstructions(array);
    }

    public void generateIndividual() {

        int[][] array = new int[MAX_INSTRUCTIONS][2];
        int lastThrustValue=0;
        int lastAngleValue=0;

        for(int timePeriod=0 ; timePeriod<MAX_INSTRUCTIONS ; timePeriod++){
            lastThrustValue = generateThrust(lastThrustValue);
            lastAngleValue = generateAngle(lastAngleValue);
            array[timePeriod][0] = lastThrustValue;
            array[timePeriod][1] = lastAngleValue;
        }

        genes = new NavigationInstructions(array);
    }

    public int[] getGene(int index) {
        return genes.getTerm(index);
    }

    public void setGene(int index, int[] value) {
        genes.setTerm(index, value);
        fitness = -1;
    }

    /* Public methods */
    public int size() {
        return genes.getInstructions().length;
    }

    public double getFitness() {
        if (fitness == -1) {
            fitness = FitnessCalc.getFitness(this);
        }
        return fitness;
    }

    @Override
    public String toString() {
        return genes.toString();
    }

    // Crossover individuals
    public NavigationIndividual crossover(Individual other) {
        NavigationIndividual newSol = new NavigationIndividual();
        // Loop through genes
        for (int i = 0; i < this.genes.size(); i++) {
            // Crossover
            if (Math.random() <= Algorithm.uniformRate || !crossoverPossible(this, other, i)) {
                newSol.setGene(i, this.getGene(i));
            } else {
                newSol.setGene(i, other.getGene(i));
            }
        }
        return newSol;
    }

    private boolean crossoverPossible(NavigationIndividual a, Individual b, int index) {

        if(index==0) {
            if( Math.abs(a.getGene(index+1)[0] - b.getGene(index)[0])<=SpaceShuttle.MAX_THRUST_CHANGE &&
                    Math.abs(a.getGene(index+1)[1] - b.getGene(index)[1])<=SpaceShuttle.MAX_ANLGE_CHANGE) {
                return true;
            } else {
                return false;
            }
        }

        if(index==a.genes.size()-1) {
            if( Math.abs(a.getGene(index-1)[0] - b.getGene(index)[0])<=SpaceShuttle.MAX_THRUST_CHANGE &&
                Math.abs(a.getGene(index-1)[1] - b.getGene(index)[1])<=SpaceShuttle.MAX_ANLGE_CHANGE) {
                return true;
            } else {
                return false;
            }
        }

        else if( Math.abs(a.getGene(index-1)[0] - b.getGene(index)[0])<=SpaceShuttle.MAX_THRUST_CHANGE &&
            Math.abs(a.getGene(index+1)[0] - b.getGene(index)[0])<=SpaceShuttle.MAX_THRUST_CHANGE &&
            Math.abs(a.getGene(index-1)[1] - b.getGene(index)[1])<=SpaceShuttle.MAX_ANLGE_CHANGE &&
            Math.abs(a.getGene(index+1)[1] - b.getGene(index)[1])<=SpaceShuttle.MAX_ANLGE_CHANGE) {
            return true;
        }

        return false;
    }

    // Mutate an individual
    public void mutate() {
        // Loop through genes
        for (int i = 0; i < this.genes.size(); i++) {
            if (Math.random() <= Algorithm.mutationRate) {


                int[] array = new int[2];

                int thrustBefore = 0;
                int thrustAfter = 0;
                int angleBefore = 0;
                int angleAfter = 0;
                int newThrust = getGene(i)[0];
                int newAngle = getGene(i)[1];

                if(i==0) {
                    thrustBefore = 0;
                    angleBefore = 0;
                }else if(i==this.genes.size()-1) {
                    thrustAfter = getGene(i)[0];
                    angleAfter = getGene(i)[1];
                }else {
                    thrustBefore = getGene(i-1)[0];
                    angleBefore = getGene(i-1)[1];
                    thrustAfter = getGene(i+1)[0];
                    angleAfter = getGene(i+1)[1];
                }

                // change thrust if possible
                if(Math.abs(thrustAfter-thrustBefore)*2<SpaceShuttle.MAX_THRUST_CHANGE) {
                    // can change both right and left values
                    if(     Math.abs(thrustBefore-getGene(i)[0])<SpaceShuttle.MAX_THRUST_CHANGE &&
                            Math.abs(thrustAfter-getGene(i)[0])<SpaceShuttle.MAX_THRUST_CHANGE) {
                        // change right
                        if(new Random().nextInt(2)==0) {
                            int possibleRange = SpaceShuttle.MAX_THRUST_CHANGE - Math.abs(thrustAfter-getGene(i)[0]);
                            int thrustChange = new Random().nextInt(possibleRange*2 + 1) - possibleRange;
                            newThrust = Math.min(SpaceShuttle.MAX_THRUST_VALUE,Math.max(0,getGene(i)[0]+thrustChange));
                        }
                        // change left
                        else {
                            int possibleRange = SpaceShuttle.MAX_THRUST_CHANGE - Math.abs(thrustBefore-getGene(i)[0]);
                            int thrustChange = new Random().nextInt(possibleRange*2 + 1) - possibleRange;
                            newThrust = Math.min(SpaceShuttle.MAX_THRUST_VALUE,Math.max(0,getGene(i)[0]+thrustChange));
                        }
                    } else if(Math.abs(thrustBefore-getGene(i)[0])<SpaceShuttle.MAX_THRUST_CHANGE) {
                        // can only change left
                        int possibleRange = SpaceShuttle.MAX_THRUST_CHANGE - Math.abs(thrustBefore-getGene(i)[0]);
                        int thrustChange = new Random().nextInt(possibleRange*2 + 1) - possibleRange;
                        newThrust = Math.min(SpaceShuttle.MAX_THRUST_VALUE,Math.max(0,getGene(i)[0]+thrustChange));
                    } else {
                        // can only change right
                        int possibleRange = SpaceShuttle.MAX_THRUST_CHANGE - Math.abs(thrustAfter-getGene(i)[0]);
                        int thrustChange = new Random().nextInt(possibleRange*2 + 1) - possibleRange;
                        newThrust = Math.min(SpaceShuttle.MAX_THRUST_VALUE,Math.max(0,getGene(i)[0]+thrustChange));
                    }
                }

                // change angle if possible
                if(Math.abs(angleAfter-angleBefore)*2<SpaceShuttle.MAX_ANLGE_CHANGE) {
                    // TODO picking a wider range is possible
                    int leftDif = Math.abs(getGene(i)[1]-angleBefore);
                    int rightDif =Math.abs(getGene(i)[1]-angleAfter);
                    // TODO find why Math.abs(getGene(i)[1]-angleBefore) is sometimes > MAX_ANLGE_CHANGE
                    int possibleRange = Math.max(0,SpaceShuttle.MAX_ANLGE_CHANGE - Math.max(leftDif,rightDif));
                    int angleChange = new Random().nextInt(possibleRange*2 + 1) - possibleRange;
                    newAngle = getGene(i)[1]+angleChange;
                }


                array[0] = newThrust;
                array[1] = newAngle;

                if(Math.abs(newAngle-getGene(i)[1])>15) System.out.println("a");
                if(Math.abs(newThrust-getGene(i)[0])>1) System.out.println("b");
                this.setGene(i, array);
            }
        }
    }

    public int generateThrust(int lastThrustValue) {
        // thrust value should be an int between 0 and 4
        // its value can only be changed by one at a time
        int thrustChange=0;

        if(lastThrustValue==0) {
            thrustChange = new Random().nextInt(SpaceShuttle.MAX_THRUST_CHANGE+1);
        }else{
            thrustChange = new Random().nextInt(SpaceShuttle.MAX_THRUST_CHANGE*2+1)-SpaceShuttle.MAX_THRUST_CHANGE;
        }
        lastThrustValue += thrustChange;
        lastThrustValue = Math.min(SpaceShuttle.MAX_THRUST_VALUE,Math.max(0,lastThrustValue));

        return lastThrustValue;
    }

    public int generateAngle(int lastAngleValue) {
        // angle should be an int with no more than 15 degrees difference from last time period
        int angleChange = 0;
        boolean hasRotatedSave = hasRotated;
        hasRotated = false;

        if(hasRotatedSave) {
            if(new Random().nextDouble()<CHANGE_ANGLE_AGAIN_PROB) {
                hasRotated = true;
                angleChange = new Random().nextInt(SpaceShuttle.MAX_ANLGE_CHANGE * 2 + 1) - SpaceShuttle.MAX_ANLGE_CHANGE;
            }
        } else if(new Random().nextDouble()<CHANGE_ANGLE_PROB) {
            hasRotated = true;
            angleChange = new Random().nextInt(SpaceShuttle.MAX_ANLGE_CHANGE*2+1)-SpaceShuttle.MAX_ANLGE_CHANGE;
        }

        return lastAngleValue + angleChange;
    }


    public NavigationInstructions getGenes() {
        return genes;
    }
}