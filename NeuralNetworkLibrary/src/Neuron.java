import com.sun.istack.internal.NotNull;

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
     * for identification purposes inside a {@link Layer}
     */
    private int index;

    /**
     * The bias of the {@link Neuron}
     */
    private double bias;

    /**
     * {@link Layer} where this Neuron is contained
     */
    private Layer myLayer = null;

    /**
     * the Neuron Activation Function
     */
    private Function function = null;

    /**
     * basic constructor for a Neuron
     */
    public Neuron() {
        this(Math.random(), null, val -> {
            double ret = 1 / (1 + Math.exp(-val));
            return ret;
        });
    }

    /**
     * constructor for a Neuron
     *
     * @param bias {@link #bias}
     */
    public Neuron(int index, double bias) {
        this(bias, null, val -> {
            double ret = 1 / (1 + Math.exp(-val));
            return ret;
        });
    }

    /**
     * more advanced Constructor for a Neuron, in which the Layer,where the Neuron is into, can be set.
     *
     * @param bias  {@link #bias}
     * @param layer {@link Layer}
     */
    public Neuron(double bias, Layer layer) {
        this(bias, layer, val -> {
            double ret = 1 / (1 + Math.exp(-val));
            return ret;
        });
    }

    /**
     * more advanced Constructor for a Neuron, in which the Layer,where the Neuron is into, can be set.
     * The Activation function can also be changed
     *
     * @param bias     {@link #bias}
     * @param layer    {@link Layer}
     * @param function {@link #function}
     */
    public Neuron(double bias, @NotNull Layer layer, Function function) {
        setBias(bias);
        setMyLayer(layer);
        setFunction(function);
        setIndex(index);
        //TODO control if index is unique in layer
    }


    /**
     * sets the Layer, in which the Neuron is into
     *
     * @return {@link Layer} where the {@link Layer} is currently in. null, if there is no layer
     */
    public Layer getMyLayer() {
        return myLayer;
    }

    /**
     * sets the Layer, in which the Neuron is into
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
     * sets the {@link Neuron#axons} for this Neuron
     *
     * @param axons {@link #axons}
     */
    public void setAxons(ArrayList<Connection> axons) {
        this.axons = axons;
    }

    /**
     * sets the {@link Neuron#dendrites} for this Neuron
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
     * sets the {@link Neuron#function} for this Neuron
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
        return function.calculate(value);
    }

    /**
     * sets the value of the current {@link Neuron}
     *
     * @param value {@link #value}
     */
    public void setValue(double value) {
        this.value = value;
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
     * returns the bias of the current {@link Neuron}
     *
     * @return {@link Neuron#bias}
     */
    public double getBias() {
        return bias;
    }

    /**
     * sets the bias of the current {@link Neuron}
     *
     * @param bias {@link Neuron#bias}
     */
    public void setBias(double bias) {
        this.bias = bias;
    }

    /**
     * send a signal to all {@link Neuron} the current Neuron is connected with (all {@link Neuron#axons});
     * the function used is {@link Function#calculate(double)}
     */
    public void send() {
        for (int i = 0; i < axons.size(); i++) {
            axons.get(i).send(function.calculate(value * bias));
        }
        value = 0;
    }

    /**
     * @param s {@link Connection} to add
     * @return true if the connection was added. Otherwise false
     * @deprecated do not use
     * Add an Connection to {@link #axons}
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
     * Remove an Connection from {@link #axons}
     *
     * @param s {@link Connection} to add
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
     * @param s {@link Connection} to add
     * @return true if the connection was added. Otherwise false
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
     * toggles the activation state of a Connection
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
     * the function which is called by all the dendrites, to send the data to this Neuron.
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
            throw new IndexOutOfBoundsException("index > axons.size()");
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
}
