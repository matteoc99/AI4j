package agent;


import network.Network;

/**
 * @author Matteo Cosi
 * @since 15.04.2017
 */
public class Agent {

    /**
     * Contains the {@link Network} of the Agent
     */
    private Network net;
    /**
     * Describes the fitness of an Agent
     */
    private double fitness = 0;

    public Agent(Network net) {
        setNet(net);
    }

    /**
     * Returns the {@link Network} of the agent
     *
     * @return {@link #net}
     */
    public Network getNet() {
        return net;
    }

    /**
     * Sets the {@link Network} for the agent
     *
     * @param net {@link #net} of this agent
     */
    public void setNet(Network net) {
        this.net = net;
    }

    /**
     * Processes the data thought the {@link Network}
     *
     * @param in values for the input Neuron
     * @return values of the output Neuron
     */
    public double[] processData(double[] in) {
        return net.processData(in);
    }

    /**
     * Returns the fitness of an Agent
     *
     * @return {@link #fitness}
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Sets the fitness of an Agent
     *
     * @param fitness {@link #fitness}
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Method used to increase the fitness. Can or should be overwritten in the specific agents
     */
    public void increaseFitness() {
        fitness++;
    }


}

