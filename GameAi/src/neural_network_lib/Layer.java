package neural_network_lib;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

/**
 * The class Layer has the role of a container for the {@link Neuron} of a certain layer:
 *
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class Layer {
    //TODO increase sizes in Network when Neurons are added
    /**
     * Contains all the {@link Neuron} of this Layer
     */
    private ArrayList<Neuron> neurons;

    /**
     * Describes the Type of a Layer it can be either IN for InputLayer, OUT for OutputLayer and HIDDEN for HiddenLayer
     */
    private LayerType type;

    /**
     * Stores the Network this layer is into.
     *
     */
    private Network net;

    /**
     * Create an empty Layer with just the type {@link LayerType}
     * @param type {@link LayerType} to set for this Layer
     */
    public Layer(LayerType type) {
        neurons = new ArrayList<>();
        if (type != null)
            this.type = type;
        else
            this.type = LayerType.HIDDEN;
    }

    /**
     * Create a fully functional Layer by giving a bunch of Neurons to start with
     * @param neurons {@link Layer#neurons}
     * @param type {@link LayerType}
     */
    public Layer(Neuron[] neurons, LayerType type) {
        this(type);
        for (int i = 0; i < neurons.length; i++) {
            this.neurons.add(neurons[i]);
        }
        if (neurons.length <= 0) {
        }
    }

    /**
     * Create a fully functional Layer by giving a bunch of Neurons to start with
     * @param neurons {@link Layer#neurons}
     * @param type {@link LayerType}
     */
    public Layer(@NotNull ArrayList<Neuron> neurons, LayerType type) {
        this(type);
        if (neurons != null)
            this.neurons = neurons;
    }

    /**
     * returns the current type of the {@link Layer}
     * @return {@link LayerType}
     */
    public LayerType getType() {
        return type;
    }

    /**
     * sets the type fro the {@link Layer}
     * @param type{@link LayerType} to set
     */
    public void setType(LayerType type) {
        if (type != null)
            this.type = type;
    }

    /**
     * Removes a neuron from {@link Layer#neurons}
     *
     * @param neuron {@link Neuron} to remove from {@link #neurons}
     * @return true if the Neuron was removed. Otherwise false
     */
    public boolean removeNeuron(Neuron neuron) {
        if (neurons.contains(neuron))
            neurons.remove(neuron);
        else
            return false;
        return true;
    }

    /**
     * Add a neuron to {@link Layer#neurons}
     *
     * @param neuron {@link Neuron} to add to {@link #neurons}
     * @return true if the Neuron was added. Otherwise false
     */
    public boolean addNeuron(Neuron neuron) {
        if (!neurons.contains(neuron))
            neurons.add(neuron);
        else
            return false;
        return true;
    }

    /**
     * Many neurons to add to {@link Layer#neurons}
     *
     * @param neurons {@link Neuron} to add to {@link #neurons}
     */
    public void addNeurons(Neuron[] neurons) {
        for (int i = 0; i < neurons.length; i++) {
            if (!this.neurons.contains(neurons[i]))
                this.neurons.add(neurons[i]);
        }
    }
    /**
     * Connect all the Neurons of this {@link Layer} to all the Neurons of the layer given as parameter.
     * the weight is a random Number done with Math.random();
     *
     * @param layer {@link Layer} to connect this {@link Layer} with
     */
    public void connectWith(Layer layer) {
        for (int i = 0; i < neurons.size(); i++) {
            Neuron from = neurons.get(i);
            for (int j = 0; j < layer.neurons.size(); j++) {
                Neuron to = layer.neurons.get(j);
                Connection con = new Connection(from, to, Math.random() * 2 - 1);
                if (from.getAxonByToIndex(to.getIndex()) == null){
                    from.addAxon(con);
                to.addDendrite(con);
            }
            }
        }
    }

    /**
     * Returns the number of Neurons currently in neurons
     * @return {@link #neurons}
     */
    public int getNeuronCount(){
        return neurons.size();
    }


    /**
     * sets all the Neurons to the given value, if the Layer is of Type Input only
     * @param in values to use
     */
    public void feed(double[] in){
        if (in.length != neurons.size())
            throw new IllegalArgumentException("feed_ERR in size not right");
        for (int i = 0; i < in.length; i++) {
            neurons.get(i).setValue(in[i]);
        }
    }


    /**
     * Stimulates all the Neurons of this {@link Layer} to send a signal to all the Neurons of the next Layer
     */
    public void send() {
        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).send();
        }
    }

    /**
     * returns the Neuron from {@link #neurons} with a given index
     * @param i index
     * @return a {@link Neuron} from neurons with the requested index
     */
    public Neuron getNeuronAt(int i) {
        if(i>=neurons.size())
            throw new IndexOutOfBoundsException("Index out of Bound");
        return neurons.get(i);
    }
}