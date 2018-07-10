package network;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

/**
 * A network.Neuron is the  basic Node in a {@link Network}
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
     * previous value of the node
     * 0.5 so it is neutral. not 1 and not 0
     */
    private double prevValue = 0.5;

    /**
     * The error to propagate back
     */
    private double error = 0;

    /**
     * for identification purposes inside a {@link Layer}
     */
    private int index;

    /**
     * {@link Layer} where this network.Neuron is contained
     */
    private Layer myLayer = null;

    /**
     * the network.Neuron Activation network.Function
     */
    private Function function = null;

    /**
     * basic constructor for a network.Neuron
     */
    public Neuron() {
        this(null, val -> {
            double ret = 1 / (1 + Math.exp(-val));
            return ret;
        });
    }

    /**
     * constructor for a network.Neuron
     *
     * @param index {@link #index}
     */
    public Neuron(int index) {
        this(null, val -> {
            double ret = 1 / (1 + Math.exp(-val));
            return ret;
        });
    }

    /**
     * more advanced Constructor for a network.Neuron, in which the network.Layer,where the network.Neuron is into, can be set.
     *
     * @param layer {@link Layer}
     */
    public Neuron( Layer layer) {
        this(layer, val -> {
            double ret = 1 / (1 + Math.exp(-val));
            return ret;
        });
    }

    /**
     * more advanced Constructor for a network.Neuron, in which the network.Layer,where the network.Neuron is into, can be set.
     * The Activation function can also be changed
     *
     * @param layer    {@link Layer}
     * @param function {@link #function}
     */
    public Neuron(@NotNull Layer layer, Function function) {
        setMyLayer(layer);
        setFunction(function);
        setIndex(index);
    }


    /**
     * returns the network.Layer, in which the network.Neuron is into
     *
     * @return {@link Layer} where the {@link Layer} is currently in. null, if there is no layer
     */
    public Layer getMyLayer() {
        return myLayer;
    }

    /**
     * sets the network.Layer, in which the network.Neuron is into
     *
     * @param myLayer {@link Layer}
     */
    public void setMyLayer(Layer myLayer) {
        if (myLayer != null) {
            if (this.myLayer != null) {
                this.myLayer.removeNeuron(this);
            }
            this.myLayer = myLayer;
            myLayer.addNeuron(this);
        }
    }

    /**
     * setter for {@link #index}
     *
     * @param index index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * sets the {@link Neuron#axons} for this network.Neuron
     *
     * @param axons {@link #axons}
     */
    public void setAxons(ArrayList<Connection> axons) {
        this.axons = axons;
    }

    /**
     * sets the {@link Neuron#dendrites} for this network.Neuron
     *
     * @param dendrites {@link #dendrites}
     */
    public void setDendrites(ArrayList<Connection> dendrites) {
        this.dendrites = dendrites;
    }

    /**
     * returns all active {@link Neuron#axons}
     *
     * @return {@link Neuron#axons}
     */
    public ArrayList<Connection> getAxons() {
        return axons;
    }

    /**
     * returns all active {@link Neuron#dendrites}
     *
     * @return {@link Neuron#dendrites}
     */
    public ArrayList<Connection> getDendrites() {
        return dendrites;
    }


    /**
     * sets the {@link Neuron#function} for this network.Neuron
     *
     * @param function {@link #function}
     */
    public void setFunction(@NotNull Function function) {
        if (function == null)
            return;
        this.function = function;
    }

    /**
     * returns the {@link Function} of the current {@link Neuron}
     *
     * @return {@link Neuron#function}
     */
    public Function getFunction() {
        return function;
    }

    /**
     * returns the value of the current {@link Neuron}
     *
     * @return {@link Neuron#value}
     */
    public double getValue() {
        prevValue = value;
        return function.calculate(value);
    }

    /**
     * sets the value of the current {@link Neuron}
     *
     * @param value {@link #value}
     */
    public void setValue(double value) {
        prevValue = this.value;
        this.value = value;
    }

    /**
     * returns the prevValue of the current {@link Neuron}
     *
     * @return {@link Neuron#prevValue}
     */
    public double getPrevValue() {
        return function.calculate(prevValue);
    }

    /**
     * sets the prevValue of the current {@link Neuron}
     *
     * @param prevValue {@link #prevValue}
     */
    public void setPrevValue(double prevValue) {
        this.prevValue = prevValue;
    }


    /**
     * returns the index of the current {@link Neuron}
     *
     * @return {@link Neuron#index}
     */
    public int getIndex() {
        return index;
    }

    /**
     * send a signal to all {@link Neuron} the current network.Neuron is connected with (all {@link Neuron#axons});
     * the function used is {@link Function#calculate(double)}
     */
    public void send() {
        for (int i = 0; i < axons.size(); i++) {
            axons.get(i).send(getValue());
        }
        prevValue = value;
        value = 0;
    }

    /**
     * @param s {@link Connection} to add
     * @return true if the connection was added. Otherwise false
     * @deprecated do not use
     * Add an network.Connection to {@link #axons}
     */
    public boolean addAxon(Connection s) {
        if (!axons.contains(s)) {
            axons.add(s);
        } else
            return false;
        return true;
    }

    /**
     * @param s {@link Connection} to add
     * @return true if the connection was added. Otherwise false
     * @deprecated do not use
     * Add a Dendride to {@link #dendrites}
     */
    public boolean addDendrite(Connection s) {
        if (!dendrites.contains(s)) {
            dendrites.add(s);
        } else
            return false;
        return true;
    }

    /**
     * Remove an network.Connection from {@link #axons}
     *
     * @param s {@link Connection} to remove
     * @return true if the connection was removed. Otherwise false
     */
    public boolean removeAxon(Connection s) {
        if (axons.contains(s)) {
            axons.remove(s);
            s.getTo().removeDendrite(s);
        } else
            return false;
        return true;
    }

    /**
     * remove a Dendrite from {@link #dendrites}
     *
     * @param s {@link Connection} to remove
     * @return true if the connection was removed. Otherwise false
     */
    public boolean removeDendrite(Connection s) {
        if (dendrites.contains(s)) {
            dendrites.remove(s);
            s.getFrom().removeAxon(s);
        } else
            return false;
        return true;
    }


    /**
     * Searches for an Axon with the requested "to" index {@link Neuron#index}
     *
     * @param to {@link Neuron#index}
     * @return null if no Axon was found otherwise the requested connection
     */
    public Connection getAxonByToIndex(int to) {
        Connection ret = null;
        for (int i = 0; i < axons.size(); i++) {
            if (to == axons.get(i).getTo().getIndex())
                ret = axons.get(i);
        }
        return ret;
    }

    /**
     * Searches for a Dendrite with the requested "from" index {@link Neuron#index}
     *
     * @param from {@link Neuron#index}
     * @return null if no Dendrite was found otherwise the requested connection
     */
    public Connection getDendriteByFromIndex(int from) {
        Connection ret = null;
        for (int i = 0; i < dendrites.size(); i++) {
            if (from == dendrites.get(i).getFrom().getIndex())
                ret = dendrites.get(i);
        }
        return ret;
    }

    /**
     * changes the activation state of an Axon with the requested "to" index {@link Neuron#index}
     *
     * @param to {@link Neuron#index}
     * @return true if everything went fine, otherwise false
     */
    public boolean toggleAxonByToIndex(int to) {
        Connection ret = null;
        for (int i = 0; i < axons.size(); i++) {
            if (to == axons.get(i).getTo().getIndex())
                ret = axons.get(i);
        }
        return toggle(ret);
    }

    /**
     * changes the activation state of a Dendrite with the requested "from" index {@link Neuron#index}
     *
     * @param from {@link Neuron#index}
     * @return true if everything went fine, otherwise false
     */
    public boolean toggleDendriteByFromIndex(int from) {
        Connection ret = null;
        for (int i = 0; i < dendrites.size(); i++) {
            if (from == dendrites.get(i).getFrom().getIndex())
                ret = dendrites.get(i);
        }
        return toggle(ret);
    }

    /**
     * toggles the activation state of a network.Connection
     *
     * @param ret {@link Connection}
     * @return true if everything went fine, otherwise false
     */
    private boolean toggle(Connection ret) {
        if (ret != null)
            if (ret.isActive())
                ret.setActive(false);
            else
                ret.setActive(true);
        else
            return false;
        return true;
    }

    /**
     * the function which is called by all the dendrites, to send the data to this network.Neuron.
     * The function simply adds all the "val" inputs to {@link Neuron#value}
     *
     * @param val the value that arrives from the dendrites
     */
    public void receive(double val) {
        value += val;
    }

    /**
     * returns an Axon at a given index
     *
     * @param index the index of the Axon
     */
    public Connection getAxonAt(int index) {
        if (index > axons.size())
            throw new IndexOutOfBoundsException("index > axons.size()");
        return axons.get(index);
    }

    /**
     * returns an Dendrite at a given index
     *
     * @param index the index of the Dendrite
     */
    public Connection getDendriteAt(int index) {
        if (index > dendrites.size())
            throw new IndexOutOfBoundsException("index > dendrites.size()");
        return dendrites.get(index);
    }

    /**
     * searches if a connection is already present in axons
     *
     * @param con {@link Connection} to check
     * @return if a connection is already present in axons
     */
    public boolean containsAxon(Connection con) {
        boolean ret = false;
        for (int i = 0; i < axons.size(); i++) {
            if (axons.get(i).equals(con))
                ret = true;
        }
        return ret;
    }

    /**
     * searches if a connection is already present in dendrites
     *
     * @param con {@link Connection} to check
     * @return if a connection is already present in dendrites
     */
    public boolean containsDendrite(Connection con) {
        boolean ret = false;
        for (int i = 0; i < dendrites.size(); i++) {
            if (dendrites.get(i).equals(con))
                ret = true;
        }
        return ret;
    }

    /**
     * searches if a connection is already present in dendrites or axons
     *
     * @param con {@link Connection} to check
     * @return if a connection is already present in dendrites or axons
     */
    public boolean containsConnection(Connection con) {
        return containsAxon(con) || containsDendrite(con);
    }

    /**
     * Changes the Weights of the Dendrites

     * @param error how much to change
     */
    public void setError(double error) {
        this.error = error;
    }

    public void processError(double rate) {
        for (int i = 0; i < dendrites.size(); i++) {
            Connection c = dendrites.get(i);
            c.changeWeightRelativeToError(rate, error);
        }
    }

    /**
     * Returns this Layer in the form of a readable String
     *
     * @return Layer as a String
     */
    public String toString() {
        String ret = "index" + index + " val:" + prevValue;
        return ret;
    }


}
