package network;

import network.Layer.LayerType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * network.Network is a class that combines the Layers and offers some utilities
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
    private double[] descriptor = null;


    /**
     * Create an empty network.Network
     */
    public Network() {
        layers = new ArrayList<>();
    }

    // public Neuron bias = new Neuron(0);

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
     * Creates a Network from a String that contains the descriptor
     * { val1, val2, etc...}
     *
     * @param descriptor
     */
    public Network(String descriptor) {
        this(stringToDescriptor(descriptor));
    }

    /**
     * Create a network.Network given the descriptor of another {@link Network}
     *
     * @param descriptor {@link #descriptor}
     */
    public Network(double[] descriptor) {
        this.descriptor = descriptor;

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
                    for (int k = 0; k < layerhid[i].getNeuronAt(j).getAxons().size(); k++) {
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

        //bias
      /*  for (int i = 1; i < layers.size(); i++) {
            for (int j = 0; j < layers.get(i).getNeuronCount(); j++) {
                new Connection(bias, layers.get(i).getNeuronAt(j), descriptor[index]);
                index++;
            }
        }
*/
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
            inNeurons[i] = new Neuron(i);
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
                    hiddenNeurons[i][j] = new Neuron(j);
                }
            }
        }

        //OUT network.Layer setup
        Layer outLayers = new Layer(LayerType.OUT);

        //OUT network.Neuron setup&initialization
        Neuron[] outNeurons = new Neuron[outputSize];
        for (int i = 0; i < outNeurons.length; i++) {
            outNeurons[i] = new Neuron(i);
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

        //bias
       /* bias.setValue(1);
        for (int i = 1; i < layers.size(); i++) {
            for (int j = 0; j < layers.get(i).getNeuronCount(); j++) {
                new Connection(bias, layers.get(i).getNeuronAt(j));
            }
        }*/
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
    public void connect(Layer layer1, Layer layer2) {
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

        //todo fire bias Neuron

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
                throw new IllegalArgumentException("Inputs have to be smaller than at index: " + i + " value: " + in[i]);
        }
        inLayer.feed(in);
        for (int i = 0; i < layers.size() - 1; i++) {
            layers.get(i).send();
        }
        for (int i = 0; i < ret.length; i++) {
            ret[i] = layers.get(layers.size() - 1).getNeuronAt(i).getValue();
            layers.get(layers.size() - 1).getNeuronAt(i).setValue(0);
        }
        return ret;
    }


    /**
     * returns the Descriptor or creates a new one in case it does not exist
     *
     * @return the descriptor for this Network
     */
    public double[] getDescriptor() {
        if (descriptor == null)
            descriptor = generateDescriptor();
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

                } else {
                    //connections
                    ret += hiddenSize[i] * hiddenSize[i + 1];

                }
            }
        } else {
            ret += inputSize * outputSize;
        }
        //anzLayer
        ret++;
        //layer desc
        ret += 2 + hiddenAmount;

        //bias
        /*ret+=outputSize;
        for (int i = 0; i < hiddenAmount; i++) {
            ret+=hiddenSize[i];
        }*/
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

        //bias
       /* for (int i = 0; i < bias.getAxons().size(); i++) {
            ret[index]=bias.getAxons().get(i).getWeight();
            index++;
        }*/
        return ret;
    }

    /**
     * Creates a descriptor from a given String
     *
     * @param descriptor the string to transform
     * @return a Double Array
     */
    public static double[] stringToDescriptor(String descriptor) {
        ArrayList<Double> desc = new ArrayList<>();
        int index = descriptor.indexOf("}");
        descriptor = descriptor.substring(0, index);
        index = descriptor.indexOf("{");
        descriptor = descriptor.substring(index + 1);
        while (true) {
            index = descriptor.indexOf(",");
            if (index == -1) {
                desc.add(Double.valueOf(descriptor));
                break;
            }
            String subData = descriptor.substring(0, index);
            desc.add(Double.valueOf(subData));
            descriptor = descriptor.substring(index + 1);
        }
        double[] ret = new double[desc.size()];
        for (int i = 0; i < desc.size(); i++) {
            ret[i] = desc.get(i);
        }
        return ret;
    }


    /**
     * Returns this Network in the form of a readable String
     *
     * @return Network as a String
     */
    public String toString() {
        String ret = "";
        for (int i = 0; i < layers.size(); i++) {
            ret += "  Layer:" + i + "\n  " + layers.get(i).toString() + "\n";
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
    public void mutateHard(int howOften) {
        for (int i = 0; i < howOften; i++) {
            int layer = (int) (Math.random() * (layers.size() - 1));
            int neuron = (int) (Math.random() * layers.get(layer).getNeuronCount());
            int connection = (int) (Math.random() * layers.get(layer).getNeuronAt(neuron).getAxons().size());
            Connection c = layers.get(layer).getNeuronAt(neuron).getAxons().get(connection);
            c.setWeight(Math.random() * 2 - 1);
            //TODO bias mutation

        }
    }

    /**
     * Changes the weight of a connection in the Network
     */
    public void mutateSoft(int howOften, double strength) {
        for (int i = 0; i < howOften; i++) {
            int layer = (int) (Math.random() * (layers.size() - 1));
            int neuron = (int) (Math.random() * layers.get(layer).getNeuronCount());
            int connection = (int) (Math.random() * layers.get(layer).getNeuronAt(neuron).getAxons().size());
            Connection c = layers.get(layer).getNeuronAt(neuron).getAxons().get(connection);
            c.setWeight(c.getWeight() + Math.random() * strength - strength / 2);

            //TODO bias mutation

            if (c.getWeight() > 1)
                c.setWeight(1);
            if (c.getWeight() < -1)
                c.setWeight(-1);
        }
    }


    /**
     * first implementation of backpropagation
     *
     * @param rate
     * @param error the error of the net
     */
    public void propagateBack(double rate, double error[]) {
        if (layers.get(layers.size() - 1).getNeuronCount() != error.length)
            System.out.println("ERR");

        for (int i = 0; i < layers.get(layers.size() - 1).getNeuronCount(); i++) {
            Neuron n = layers.get(layers.size() - 1).getNeuronAt(i);
            n.setError(error[i]);
            n.processError(rate);
        }
        for (int i = layers.size() - 2; i >= 0; i--) {
            for (int j = 0; j < layers.get(i).getNeuronCount(); j++) {
                Neuron n = layers.get(i).getNeuronAt(j);
                n.processError(rate);
            }
        }
    }

}
