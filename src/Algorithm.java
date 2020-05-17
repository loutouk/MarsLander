public class Algorithm {

    /* GA parameters */

    public static final int popSize = 1000;
    public static final double uniformRate = 0.50;
    public static final double mutationRate = 0.060;
    public static final int tournamentSize = 5;
    public static final boolean elitism = true;
    public static final int elitismCount = 10;
    public static int generationCount = 50;

    /* Public methods */

    // Evolve a population
    public static Population evolvePopulation(Population pop) {

        Population newPopulation = new Population(pop.size(), false);

        // Keep our best individualSystem
        if (elitism) {
            Individual[] elits = pop.getFittest(elitismCount);
            for(int i=0 ; i<elits.length ; i++) {
                newPopulation.saveIndividual(i, elits[i]);
            }

        }

        // Crossover population
        int elitismOffset;
        if (elitism) {
            elitismOffset = elitismCount;
        } else {
            elitismOffset = 0;
        }
        // Loop over the population size and create new individuals with
        // crossover

        for (int i = elitismOffset; i < pop.size(); i++) {
            Individual indiv1 = tournamentSelection(pop);
            Individual indiv2 = tournamentSelection(pop);
            Individual newIndiv = indiv1.crossover(indiv2);
            newPopulation.saveIndividual(i, newIndiv);
        }

        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            newPopulation.getIndividual(i).mutate();
        }

        return newPopulation;
    }

    // Select individuals for crossover
    private static Individual tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest
        Individual fittest = tournament.getFittest(1)[0];
        return fittest;
    }
}