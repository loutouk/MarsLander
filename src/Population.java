import java.util.*;

public class Population {

    Individual[] individuals;

    // Create a population
    public Population(int populationSize, boolean initialise) {
        individuals = new Individual[populationSize];
        // Initialise population
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < size(); i++) {
                Individual newIndividual = new NavigationIndividual();
                newIndividual.generateIndividual();
                saveIndividual(i, newIndividual);
            }
        }
    }

    public Individual getIndividual(int index) {
        return individuals[index];
    }

    public Individual[] getFittest(int n) {

        Collections.sort(Arrays.asList(individuals), new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {

                double o1Fit = o1.getFitness();
                double o2Fit = o2.getFitness();

                if(o1Fit>o2Fit) {
                    return 1;
                } else if(o1Fit<o2Fit) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        Individual[] nFittests = new Individual[n];
        for(int i=0 ; i<n ; i++){
            nFittests[i] = individuals[i];
        }
        return nFittests;
    }

    public int size() {
        return individuals.length;
    }

    public void saveIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }
}