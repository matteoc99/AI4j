package network;

/**
 * An network.Connection is a connection between two {@link Neuron} with a specific weight
 *
 * @author Matteo Cosi
 * @since 16.04.2017
 */
public class Connection {

    /**
     * weight also known as value or cost of a network.Connection
     */
    private double weight = 0;
    /**
     * Constant for maximum weight of a connection
     */
    public static final int MAX_WEIGHT = 1;
    /**
     * Constant for minimum weight of a connection
     */
    public static final int MIN_WEIGHT = -1;
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
     * basic Constructor for a network.Connection, which connects two {@link Neuron}s
     *
     * @param from {@link Neuron} where the connection begins
     * @param to   {@link Neuron} where the connection ends
     */
    public Connection(Neuron from, Neuron to) {
        this(from, to, Math.random() * 2 - 1, true);
    }

    /**
     * Constructor for a network.Connection, which connects two {@link Neuron}s
     *
     * @param from   {@link Neuron} where the connection begins
     * @param to     {@link Neuron} where the connection ends
     * @param weight {@link Connection#weight} of the Connection
     */
    public Connection(Neuron from, Neuron to, double weight) {
        this(from, to, weight, true);
    }

    /**
     * Constructor for a network.Connection, which connects two {@link Neuron}s
     *
     * @param from   {@link Neuron} where the connection begins
     * @param to     {@link Neuron} where the connection ends
     * @param weight {@link Connection#weight} of the Connection
     * @param active {@link #active} if the connection is active
     */
    public Connection(Neuron from, Neuron to, double weight, boolean active) {
        if (from == null || to == null)
            return;
        setFrom(from);
        setTo(to);
        setWeight(weight);
        setActive(active);
    }

    /**
     * returns if the {@link Connection} is active
     *
     * @return {@link #active}
     */
    public boolean isActive() {
        return active;
    }

    /**
     * activates or deactivates this {@link Connection}
     *
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
    public void setFrom(Neuron from) {
        if (from == null)
            return;
        if (!from.containsAxon(this)) {
            from.addAxon(this);
            this.from = from;
        }
    }

    /**
     * sets {@link Connection#to}
     *
     * @param to {@link Neuron}
     */
    public void setTo(Neuron to) {
        if (from == null)
            return;
        if (!to.containsDendrite(this)) {
            to.addDendrite(this);
            this.to = to;
        }
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
     * Send a value to the {@link Connection#to} network.Neuron multiplied
     * by the {@link Connection#weight} if the connection is active
     *
     * @param value the value to forward
     */
    public void send(double value) {
        if (isActive())
            to.receive(value * weight);
    }

    /**
     * returns if this connection equals an other connection
     *
     * @param other the {@link Connection} to check if equal
     * @return if the two connections are equal
     */
    public boolean equals(Connection other) {
        boolean ret = false;
        if (this.to.equals(other.to) && this.from.equals(other.from)
                && this.to.getMyLayer().equals(other.to.getMyLayer())
                && this.from.getMyLayer().equals(other.from.getMyLayer()))
            ret = true;
        return ret;

    }

    public void changeWeightRelativeToError(double rate, double error) {

        weight = rate * error *
                from.getPrevValue() + weight;

        if (weight > 1) {
            weight=1;
        }
        if (weight < -1) {
            weight=-1;
        }
        from.setError(error * weight);
    }
}
