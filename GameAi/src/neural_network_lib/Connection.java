package neural_network_lib;

/**
 * An Connection is a connection between two {@link Neuron} with a specific weight
 * @author Matteo Cosi
 * @since 16.04.2017
 */
public class Connection {

    /**
     * The minimum {@link Connection#weight} a connection is allowed to have
     */
    private static final int MIN_WEIGTH = 0;
    /**
     * The maximum {@link Connection#weight} a connection is allowed to have
     */
    private static final int MAX_WEIGTH = 1;
    /**
     * weight also known as value or cost of a Connection
     */
    private double weight;
    /**
     * {@link Neuron} where the connection begins
     */
    private Neuron from;
    /**
     * {@link Neuron} where the connection ends
     */
    private Neuron to;


    /**
     * Constructor for a Connection, which connects two {@link Neuron}s
     *
     * @param from   {@link Neuron} where the connection begins
     * @param to     {@link Neuron} where the connection ends
     * @param weight {@link Connection#weight} of the Synapse
     */
    public Connection(Neuron from, Neuron to, double weight) {
        if (from == null || to == null)
            throw new NullPointerException("from or to = null");
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    /**
     * sets {@link Connection#weight}
     */
    public void setWeight(double weight) {
        if (weight >= MIN_WEIGTH || weight <= MAX_WEIGTH)
            this.weight = weight;
        else
            throw new ValueOutOfBoundException("Value out of Bound");
    }

    /**
     * sets {@link Connection#from}
     */
    public void setFrom(Neuron from) {
        if (from != null)
            this.from = from;
        else
            throw new NullPointerException("from = null");
    }

    /**
     * sets {@link Connection#to}
     *
     * @param to {@link Neuron}
     */
    public void setTo(Neuron to) {
        if (from != null)
            this.to = to;
        else
            throw new NullPointerException("from = null");
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
     * Send a value to the {@link Connection#to} Neuron multiplied by the {@link Connection#weight}
     *
     * @param value
     */
    public void send(double value) {
        to.recive(value * weight);
    }

}
