package neural_network_lib;

import com.sun.istack.internal.NotNull;

/**
 * An Connection is a connection between two {@link Neuron} with a specific weight
 *
 * @author Matteo Cosi
 * @since 16.04.2017
 */
public class Connection {

    /**
     * weight also known as value or cost of a Connection
     */
    private double weight = 0;
    /**
     * Constant for maximum weight of a connection
     */
    private static final int MAX_WEIGHT = 1;
    /**
     * Constant for minimum weight of a connection
     */
    private static final int MIN_WEIGHT = -1;
    /**
     * {@link Neuron} where the connection begins
     */
    private Neuron from;
    /**
     * {@link Neuron} where the connection ends
     */
    private Neuron to;

    /**
     * Describes whether this connection is active or not. the connection behaves like it does not exist
     */
    private boolean active;

    /**
     *basic Constructor for a Connection, which connects two {@link Neuron}s
     *
     * @param from   {@link Neuron} where the connection begins
     * @param to     {@link Neuron} where the connection ends
     *
     */
    public Connection(Neuron from, Neuron to) {
        this(from,to,Math.random()*2-1,true);
    }

    /**
     * Constructor for a Connection, which connects two {@link Neuron}s
     *
     * @param from   {@link Neuron} where the connection begins
     * @param to     {@link Neuron} where the connection ends
     * @param weight {@link Connection#weight} of the Synapse
     */
    public Connection(Neuron from, Neuron to, double weight) {
        this(from,to,weight,true);
    }

    /**
     * Constructor for a Connection, which connects two {@link Neuron}s
     *
     * @param from   {@link Neuron} where the connection begins
     * @param to     {@link Neuron} where the connection ends
     * @param weight {@link Connection#weight} of the Synapse
     *  @param active {@link #active} if the connection is active
     */
    public Connection(@NotNull Neuron from, @NotNull Neuron to, double weight,boolean active) {
        if (from == null || to == null)
            return;
        this.from = from;
        this.to = to;
        setWeight(weight);
        this.active=active;
        to.addDendrite(this);
        from.addAxon(this);
    }

    /**
     * returns if the {@link Connection} is active
     * @return {@link #active}
     */
    public boolean isActive() {
        return active;
    }

    /**
     * activates or deactivates this {@link Connection}
     * @param active {@link #active}
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * sets {@link Connection#weight}
     */
    public void setWeight(double weight) {
        if (weight <= MAX_WEIGHT || weight >= MIN_WEIGHT)
            this.weight = weight;
    }

    /**
     * sets {@link Connection#from}
     */
    public void setFrom(@NotNull Neuron from) {
        if (from == null)
            return;
        from.addAxon(this);
        this.from = from;
    }

    /**
     * sets {@link Connection#to}
     *
     * @param to {@link Neuron}
     */
    public void setTo(@NotNull Neuron to) {
        if (from == null)
            return;
        to.addDendrite(this);
        this.to = to;
    }

    /**
     * @return {@link Connection#from}
     */
    public Neuron getFrom() {
        return from;
    }


    /**
     * @return {@link Connection#to}
     */
    public Neuron getTo() {
        return to;
    }

    /**
     * @return {@link Connection#weight}
     */
    public double getWeight() {
        return weight;
    }


    /**
     * Send a value to the {@link Connection#to} Neuron multiplied
     * by the {@link Connection#weight} if the connection is active
     *
     * @param value the value to forward
     */
    public void send(double value) {
        if(isActive())
            to.receive(value * weight);
    }
}
