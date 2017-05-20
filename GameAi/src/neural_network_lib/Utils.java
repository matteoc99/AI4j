package neural_network_lib;

/**
 * Collection of useful functions for the Library
 *
 * @author Matteo Cosi
 * @since 20.05.2017
 */
public class Utils
{

    /**
     * Creates a random chromosome, of the size given by
     * {@link Network#getDescriptorLength(int, int, int, int[])}. So be sure to have set the set
     * Size of the Chromosome, before calling this Function
     *
     * @return a randomly generated chromosome
     */
    public static double[] createRandomDescriptor(int inputSize, int outputSize, int hiddenAmount, int[] hiddenSize) {
        double ret[] = new double[Network.getDescriptorLength( inputSize,  outputSize,  hiddenAmount, hiddenSize)];
        for (int i = 0; i < ret.length; i++) {
            if (Math.random() * 3 >= 1)
                ret[i] = Math.random();
        }
        return ret;
    }

    /**
     * Performs a crossover of two parent chromosomes and generates two child chromosomes
     *
     * @param parent1 chromosome of the parent 1
     * @param parent2 chromosome of the parent 2
     * @return two child chromosomes in a Matrix [2][parent1.length]<br>
     * child 1 is found in return[0] and child 2 is found in return[1]
     */
    public static double[][] crossOver(double[] parent1, double[] parent2) {
        double[][] ret = new double[2][parent1.length];
        int mid = parent1.length / 2;
        for (int i = 0; i <= mid; i++) {
            ret[0][i] = parent1[i];
            ret[1][i] = parent2[i];
        }
        for (int i = mid + 1; i < parent1.length; i++) {
            ret[0][i] = parent2[i];
            ret[1][i] = parent1[i];
        }
        return ret;
    }

    /**
     * Given a Chromosome, chromosome has the chance of 25% to lose information,
     * 50% to change or add information, 25% chance that nothing happens. By
     * Information I mean one connection between Neurons in the Neural net
     *
     * Mutation gives a new random value to one weight //TODO better
     *
     * @param chromosome
     *            the chromosome to mutate
     * @return the mutated chromosome
     */
    public static double[] mutate(double[] chromosome) {
        int probability = (int) (Math.random() * 5);
        int index = (int) (Math.random() * chromosome.length);
        chromosome[index] = Math.random();
        if (probability == 0)
            chromosome[index] = 0;
        return chromosome;
    }
}
