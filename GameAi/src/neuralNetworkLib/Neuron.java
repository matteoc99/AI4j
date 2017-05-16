package neuralNetworkLib;

import java.util.ArrayList;

/**
 * A Neuron is the  basic Node in a {@link Network}
 *
 * @author Matteo Cosi
 * @since 16.04.2017
 */
public class Neuron {

    /**
     * Connections to other Neurons
     */
    private ArrayList<Connection> axons = new ArrayList<>();

    /**
     * Connections from other Neurons
     */
    private ArrayList<Connection> dendrites = new ArrayList<>();

    /**
     * current value in the node
     */
    private double value = 0;

    /**
     * for identification purposes
     */
    private int index;

    /**
     * constructor for a Neuron
     *
     * @param index {@link Neuron#index}
     */
    public Neuron(int index) {
        this.index = index;
    }


    /**
     * sets the {@link Neuron#axons} for this Neuron
     * @param axons {@link #axons}
     */
    public void setAxons(ArrayList<Connection> axons) {
        this.axons = axons;
    }

    /**
     * sets the {@link Neuron#dendrites} for this Neuron
     * @param dendrites {@link #dendrites}
     */
    public void setDendrites(ArrayList<Connection> dendrites) {
        this.dendrites = dendrites;
    }

    /**
     * returns all active {@link Neuron#axons}
     * @return {@link Neuron#axons}
     */
    public ArrayList<Connection> getAxons() {
        return axons;
    }

    /**
     * returns all active {@link Neuron#dendrites}
     *
     * @return  {@link Neuron#dendrites}
     */
    public ArrayList<Connection> getDendrites() {
        return dendrites;
    }


    /**
     * returns the value of the current {@link Neuron}
     * @return {@link Neuron#value}
     */
    public double getValue() {
        return value;
    }

    /**
     * sets the value of the current {@link Neuron}
     * @param value {@link #value}
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * returns the index of the current {@link Neuron}
     * @return {@link Neuron#index}
     */
    public int getIndex() {
        return index;
    }

    /**
     * The Activation Function for this Neuron
     * Returns a value between 0 and 1 for a given value
     * @param val value to pass trougth the function
     * @return a value etween 0 and 1
     */
    public static double activationFunction(double val) {
        double ret = 1 / (1 + Math.exp(-val));
        return ret;
    }

    /**
     * send the signal between 0-1 to all {@link Neuron} the current Neuron is connected with (all {@link Neuron#axons});
     */
    public void send() {
        for (int i = 0; i < axons.size(); i++) {
            axons.get(i).send(activationFunction(value));
        }
    }

    /**
     * Add an Connection to {@link #axons}
     * @param s {@link Connection} to add
     */
    public void addAxon(Connection s) {
        axons.add(s);
    }
    /**
     * Add a Dendride to {@link #dendrites}
     * @param s {@link Connection} to add
     */
    public void addDendrite(Connection s) {
        dendrites.add(s);
    }
    /**
     * Add an Connection to {@link #axons}
     * @param s {@link Connection} to add
     */
    public void removeAxon(Connection s) {
        axons.remove(s);
    }
    /**
     * Add a Dendride to {@link #dendrites}
     * @param s {@link Connection} to add
     */
    public void removeDendrite(Connection s) {
        dendrites.remove(s);
    }

    public Connection getAxonByToIndex(int to) {
        Connection ret = null;
        //TODO
        return ret;
    }

    public Connection getAxonByFromIndex(int to) {
        Connection ret = null;
        //TODO
        return ret;
    }

    public Connection getDendroidByToIndex(int to) {
        Connection ret = null;
        //TODO
        return ret;
    }

    public Connection getDendroidByFromIndex(int to) {
        Connection ret = null;
        //TODO
        return ret;
    }

    /**
     * TODO method created for class axon implementation needed
     */
    public void recive(double val) {
        //TODO
    }

}
