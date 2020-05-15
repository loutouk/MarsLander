public abstract class Individual {

    public abstract int[] getGene(int index);

    public abstract void setGene(int index, int[] value);

    public abstract Individual crossover(Individual other);

    public abstract void mutate();

    public abstract void generateIndividual();

    public abstract double getFitness();

}