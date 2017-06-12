package network;

import com.sun.istack.internal.NotNull;
import network.Layer.LayerType;

import java.util.ArrayList;


/**
 * network.Network is a class that combines the Layers and offers some utilities
 * <p>
 * TODO training and learning. for now it only processes some values
 *
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class Network {

    /**
     * the {@link Layer} of witch the network.Network is build
     */
    private ArrayList<Layer> layers;

    /**
     * used to describe a network.Network. The length equals {@link #getDescriptorLength(int, int, int, int[])}.
     * it consists of a network.Network bias and all the connection weights for all the Neurons
     * [0]...layercount
     * [1-layercount]...size of the layers
     * 0...layer is deactivated
     */
    private double[] descriptor;


    /**
     * Create an empty network.Network
     */
    public Network() {
        layers = new ArrayList<>();
    }

    /**
     * Create a network.Network with some Layers and connects them in the order they arrive
     *
     * @param layerSet
     */
    public Network(ArrayList<Layer> layerSet) {
        this();
        if (layerSet != null) {
            this.layers = layerSet;
            for (int i = 0; i < layerSet.size() - 1; i++) {
                connect(layerSet.get(i), layerSet.get(i + 1));
            }
        }
    }

    /**
     * Create a network.Network given the descriptor of another {@link Network}
     *
     * @param descriptor {@link #descriptor}
     */
    public Network(double[] descriptor) {
        if (descriptor[0] < 2 && descriptor[1] > 0 && descriptor[(int) (descriptor[0] - 1)] > 0)
            throw new IllegalArgumentException("descriptor format error");

        layers = new ArrayList<>();

        //creating network.Layer structure and adding Neurons
        int anzLayer = (int) descriptor[0];
        int inputSize = (int) descriptor[1];
        int outputSize = (int) descriptor[(int) descriptor[0]];
        int hiddenAmount = (int) (descriptor[0] - 2);
        int[] hiddenSize = new int[hiddenAmount];

        int neuronCount = 0;
        for (int i = 0; i < hiddenAmount; i++) {
            hiddenSize[i] = (int) descriptor[i + 2];
        }
        Layer layerin = new Layer(LayerType.IN);
        for (int i = 0; i < inputSize; i++) {
            Neuron neuron = new Neuron();
            layerin.addNeuron(neuron);
            neuronCount++;
        }

        Layer[] layerhid = new Layer[hiddenAmount];
        for (int i = 0; i < hiddenAmount; i++) {
            layerhid[i] = new Layer(LayerType.HIDDEN);
            for (int j = 0; j < hiddenSize[i]; j++) {
                Neuron neuron = new Neuron();
                layerhid[i].addNeuron(neuron);
                neuronCount++;
            }
        }
        Layer layerout = new Layer(LayerType.OUT);
        for (int i = 0; i < outputSize; i++) {
            Neuron neuron = new Neuron();
            layerout.addNeuron(neuron);
            neuronCount++;
        }
        //create connections
        if (hiddenAmount > 0) {
            connect(layerin, layerhid[0]);
            for (int i = 0; i < hiddenAmount; i++) {
                if (i == hiddenAmount - 1) {
                    connect(layerhid[i], layerout);
                } else {
                    connect(layerhid[i], layerhid[i + 1]);
                }
            }
        } else {
            connect(layerin, layerout);
        }
        /*
            stores the iteration over the descriptor
         */
        int index = anzLayer + 1;

        //iterate over the descriptor
        for (int i = 0; i < inputSize; i++) {
            layerin.getNeuronAt(i).setBias(descriptor[index]);
            index++;
            for (int j = 0; j < layerin.getNeuronAt(i).getAxons().size(); j++) {
                Connection con = layerin.getNeuronAt(i).getAxons().get(j);
                if (descriptor[index] < Connection.MIN_WEIGHT) {
                    con.setActive(false);
                    con.setWeight(descriptor[index] + Integer.MAX_VALUE - Connection.MAX_WEIGHT);
                } else {
                    con.setWeight(descriptor[index]);
                }
                index++;
            }
        }
        if (hiddenAmount > 0) {
            for (int i = 0; i < hiddenAmount; i++) {
                for (int j = 0; j < hiddenSize[i]; j++) {
                    layerhid[i].getNeuronAt(j).setBias(descriptor[index]);
                    index++;
                    for (int k = 0; k < layerhid[i].getNeuronAt(i).getAxons().size(); k++) {
                        Connection con = layerhid[i].getNeuronAt(j).getAxons().get(k);
                        if (descriptor[index] < Connection.MIN_WEIGHT) {
                            con.setActive(false);
                            con.setWeight(descriptor[index] + Integer.MAX_VALUE - Connection.MAX_WEIGHT);
                        } else {
                            con.setWeight(descriptor[index]);
                        }
                        index++;
                    }
                }
            }
        }
        //add the Layers to the network.Network
        layers.add(layerin);
        for (int i = 0; i < hiddenAmount; i++) {
            layers.add(layerhid[i]);
        }
        layers.add(layerout);
        this.descriptor = generateDescriptor();
    }

    /**
     * Create a Custom network.Network with everything set up.
     * hiddenSize.length() has to be equal to hiddenAmount
     *
     * @param inputSize    the preferred size of
     * @param outputSize   the preferred size of output layers
     * @param hiddenAmount the preferred amount of hidden layers
     * @param hiddenSize   the preferred size of the hidden layers for every hidden layer
     */
    public Network(int inputSize, int outputSize, int hiddenAmount, int[] hiddenSize) {

        this();
        if (hiddenSize.length != hiddenAmount)
            throw new IllegalArgumentException("hiddenSize count not right");
        if (hiddenAmount < 0 || outputSize <= 0 || inputSize <= 0)
            throw new IllegalArgumentException("all Sizes must be >0");

        for (int i = 0; i < hiddenAmount; i++) {
            if (hiddenSize[i] <= 0)
                throw new IllegalArgumentException("all Sizes must be >0");
        }


        //IN network.Layer setup
        Layer inLayers = new Layer(LayerType.IN);

        //IN network.Neuron setup&initialization
        Neuron[] inNeurons = new Neuron[inputSize];
        for (int i = 0; i < inNeurons.length; i++) {
            inNeurons[i] = new Neuron(i, Math.random());
        }
        Layer[] hiddenLayers = null;
        Neuron[][] hiddenNeurons = null;
        if (hiddenAmount > 0) {

            //HIDDEN network.Layer setup
            hiddenLayers = new Layer[hiddenAmount];


            //HIDDEN network.Neuron setup
            hiddenNeurons = new Neuron[hiddenAmount][];
            for (int i = 0; i < hiddenAmount; i++) {
                hiddenLayers[i] = new Layer(LayerType.HIDDEN);
                hiddenNeurons[i] = new Neuron[hiddenSize[i]];
            }
            //&initialization
            for (int i = 0; i < hiddenNeurons.length; i++) {
                for (int j = 0; j < hiddenNeurons[i].length; j++) {
                    hiddenNeurons[i][j] = new Neuron(j, Math.random());
                }
            }
        }

        //OUT network.Layer setup
        Layer outLayers = new Layer(LayerType.OUT);

        //OUT network.Neuron setup&initialization
        Neuron[] outNeurons = new Neuron[outputSize];
        for (int i = 0; i < outNeurons.length; i++) {
            outNeurons[i] = new Neuron(i, Math.random());
        }

        //adding all the Neurons to the Layers
        inLayers.addNeurons(inNeurons);
        for (int i = 0; i < hiddenAmount; i++) {
            hiddenLayers[i].addNeurons(hiddenNeurons[i]);
        }
        outLayers.addNeurons(outNeurons);

        //connecting all the layers
        if (hiddenAmount > 0) {
            connect(inLayers, hiddenLayers[0]);
            for (int i = 0; i < hiddenAmount; i++) {
                if (i == hiddenAmount - 1) {
                    connect(hiddenLayers[i], outLayers);
                } else {
                    connect(hiddenLayers[i], hiddenLayers[i + 1]);
                }
            }
        } else {
            connect(inLayers, outLayers);
        }
        //adding The Layers to the network.Network used to calculate later
        layers.add(inLayers);
        for (int i = 0; i < hiddenAmount; i++) {
            layers.add(hiddenLayers[i]);
        }
        layers.add(outLayers);
        descriptor = generateDescriptor();
    }

    /**
     * Create a Deep Feed Forward network.Network with everything set up.
     *
     * @param inputSize    the preferred size of
     * @param outputSize   the preferred size of output layers
     * @param hiddenAmount the preferred ammount of hidden layers
     * @param hiddenSize   the preferred size of the hidden layers for every hidden layer
     * @return a DFF network.Network with everything set up.
     */
    public static Network createDFF(int inputSize, int outputSize, int hiddenAmount, int hiddenSize) {
        int[] hidden = new int[hiddenAmount];
        for (int i = 0; i < hiddenAmount; i++) {
            hidden[i] = hiddenSize;
        }
        return new Network(inputSize, outputSize, hiddenAmount, hidden);
    }


    /**
     * create a support Vector Machine
     *
     * @param size size of the network.Network
     * @return a SVM network.Network
     */
    public static Network createSVM(int size, int hiddenAmount) {
        Network ret = createDFF(size, 1, hiddenAmount, size);
        for (int i = 0; i < ret.getLayerByIndex(0).getNeuronCount(); i++) {
            Neuron in = ret.getLayerByIndex(0).getNeuronAt(i);
            for (int j = 0; j < in.getAxons().size(); j++) {
                if (i != j)
                    in.getAxonAt(j).setActive(false);
            }
        }
        ret.descriptor = ret.generateDescriptor();
        return ret;
    }

    /**
     * Adds a layer to {@link Network#layers}
     *
     * @param layer {@link Layer} to add to {@link #layers}
     * @return true if the network.Layer was added. Otherwise false
     */
    public boolean addLayer(Layer layer) {
        if (!layers.contains(layer))
            layers.add(layer);
        else
            return false;
        return true;
    }

    /**
     * Removes a layer from {@link Network#layers}
     *
     * @param layer {@link Layer} to remove from {@link #layers}
     * @return true if the network.Layer was removed. Otherwise false
     */
    public boolean removeLayer(Layer layer) {
        if (layers.contains(layer))
            layers.remove(layer);
        else
            return false;
        return true;
    }

    /**
     * Returns the Layers of this network.Network
     *
     * @return {@link #layers}
     */
    public ArrayList<Layer> getLayers() {
        return layers;
    }

    /**
     * Returns the network.Layer at a given index
     *
     * @param index index of the network.Layer
     * @return the requested network.Layer
     */
    public Layer getLayerByIndex(int index) {
        if (index < layers.size())
            return layers.get(index);
        throw new IndexOutOfBoundsException("index greater than layer size");
    }

    /**
     * Connects the first network.Layer with the second
     * from layer1 -> layer2 axon
     * from layer1 <- layer2 dendrites
     *
     * @param layer1 first network.Layer
     * @param layer2 second network.Layer
     */
    public void connect(@NotNull Layer layer1, @NotNull Layer layer2) {
        if (layer1 == null || layer2 == null)
            return;
        layer1.connectWith(layer2);
    }

    /**
     * Returns the index of a layer in {@link #layers}
     *
     * @param layer The {@link Layer} whose index is requested
     * @return -1 if the index was not found. Otherwise the index of the network.Layer
     */
    public int getIndexOfLayer(Layer layer) {
        if (layers.contains(layer))
            return layers.indexOf(layer);
        else
            return -1;
    }

    /**
     * Process Data through all the Layers, and return the
     *
     * @param in Data for the Input network.Layer
     * @return the Data that the network.Network Processes
     */
    public double[] processData(double[] in) {
        double[] ret = new double[layers.get(layers.size() - 1).getNeuronCount()];

        if (layers.size() <= 0)
            throw new IllegalStateException("network.Network is still empty, cant process Data");
        if (in.length != layers.get(0).getNeuronCount())
            throw new IllegalArgumentException("input size not right");
        Layer inLayer = layers.get(0);
        if (inLayer.getType() != LayerType.IN)
            throw new IllegalStateException("cant find the in-network.Layer");
        for (int i = 0; i < in.length; i++) {
            if (in[i] > 1)
                throw new IllegalArgumentException("Inputs have to be smaller than1");
        }
        inLayer.feed(in);
        for (int i = 0; i < layers.size() - 1; i++) {
            layers.get(i).send();
        }
        for (int i = 0; i < ret.length; i++) {
            ret[i] = layers.get(layers.size() - 1).getNeuronAt(i).getValue();
        }
        return ret;
    }

    public double[] getDescriptor() {
        return descriptor;
    }

    /**
     * returns the length of the descriptor of a network with the given parameters
     *
     * @param inputSize    size of the input layer
     * @param outputSize   size of the output layer
     * @param hiddenAmount amount of hidden layer
     * @param hiddenSize   size of the hidden layers
     * @return the minimum length a {@link #descriptor} must have
     */
    public static int getDescriptorLength(int inputSize, int outputSize, int hiddenAmount, int[] hiddenSize) {
        int ret = 0;
        if (hiddenAmount > 0) {
            //connections
            ret += inputSize * hiddenSize[0];
            for (int i = 0; i < hiddenAmount; i++) {
                if (i == hiddenAmount - 1) {
                    //connection
                    ret += hiddenSize[hiddenSize.length - 1] * outputSize;
                    //neuron
                    ret += hiddenSize[hiddenSize.length - 1];
                } else {
                    //connections
                    ret += hiddenSize[i] * hiddenSize[i + 1];
                    //neuron
                    ret += hiddenSize[i];
                }
            }
            //neurons
            ret += inputSize;
            ret += outputSize;
        } else {
            ret += inputSize * outputSize;
            ret += inputSize;
            ret += outputSize;
        }
        //anzLayer
        ret++;
        //layer desc
        ret += 2 + hiddenAmount;

        return ret;
    }

    /**
     * Generates the descriptor for this network
     * [0]...layercount
     * [1-layercount]...size of the layers
     *
     * @return {@link #descriptor}
     */
    public double[] generateDescriptor() {
        double[] ret;
        //setup useful variables
        int anzLayer = layers.size();
        int inputSize = layers.get(0).getNeuronCount();
        int outputSize = layers.get(anzLayer - 1).getNeuronCount();
        int hiddenAmount = anzLayer - 2;
        int[] hiddenSize = new int[hiddenAmount];
        for (int i = 0; i < hiddenAmount; i++) {
            hiddenSize[i] = layers.get(i + 1).getNeuronCount();
        }
        //calculate length
        ret = new double[getDescriptorLength(inputSize, outputSize, hiddenAmount, hiddenSize)];
        //write data
        ret[0] = anzLayer;
        /*
            stores the current index in this
        */
        int index = 1;
        //layer description
        for (int i = 0; i < anzLayer; i++) {
            ret[index] = layers.get(i).getNeuronCount();
            index++;
        }
        ArrayList<Neuron> all = getAllNeurons();

        //for all neurons add himself and the connection
        for (int i = 0; i < all.size(); i++) {
            Neuron neuron = all.get(i);
            ret[index] = neuron.getBias();
            index++;
            for (int j = 0; j < neuron.getAxons().size(); j++) {
                Connection con = neuron.getAxons().get(j);
                if (con.isActive())
                    ret[index] = con.getWeight();
                else {
                    ret[index] = Integer.MIN_VALUE + Connection.MAX_WEIGHT - con.getWeight();
                }
                index++;
            }
        }
        return ret;
    }

    /**
     * Returns the total Neurons number contained in this network.Network
     *
     * @return total Neurons number
     */
    public int getTotalNeuronCount() {
        int ret = 0;
        for (Layer layer : layers) {
            ret += layer.getNeuronCount();
        }
        return ret;
    }

    /**
     * Returns all the Neurons contained in this network.Network
     *
     * @return all Neurons
     */
    public ArrayList<Neuron> getAllNeurons() {
        ArrayList<Neuron> ret = new ArrayList<>();
        for (Layer layer : layers) {
            for (int i = 0; i < layer.getNeuronCount(); i++) {
                ret.add(layer.getNeuronAt(i));
            }
        }
        return ret;
    }

    /**
     * Changes the weight of a connection in the Network
     */
    public void mutate(){
        int layer = (int) (Math.random()*(layers.size()-1));
        int neuron = (int) (Math.random()*layers.get(layer).getNeuronCount());
        int connection= (int) (Math.random()*layers.get(layer).getNeuronAt(neuron).getAxons().size());
        Connection c = layers.get(layer).getNeuronAt(neuron).getAxons().get(connection);
        c.setWeight(Math.random()*2-1);
    }
}
