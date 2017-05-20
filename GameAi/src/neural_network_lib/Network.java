package neural_network_lib;

import java.util.ArrayList;


/**
 * Network is a class that combines the Layers and offers some utilities
 * <p>
 * TODO training and learning. for now it only processes some values
 *
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class Network {

    /**
     * the {@link Layer} of witch the Network is build
     */
    private ArrayList<Layer> layers;

    /**
     * used for storing the current Network data
     */
    private int inputSize;
    private int outSize;
    private int hiddenAmount;
    private int hiddenSize[];


    /**
     * used to describe a Network. The length equals {@link #getDescriptorLength(int, int, int, int[])}.
     * it consists of a Network bias and all the connection weights for all the Neurons
     */
    private double[] descriptor;



    /**
     * Create an empty Network
     */
    public Network() {
        layers = new ArrayList<>();
    }

    /**
     * Create a Network with some Layers and connects them in the order they arrive
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
     * Create a Custom Network with everything set up.
     * hiddensize.length() has to be equal to hiddenAmount
     *
     * @param inputSize    the preferred size of
     * @param outputSize   the preferred size of output layers
     * @param hiddenAmount the preferred ammount of hidden layers
     * @param hiddenSize   the preferred size of the hidden layers for every hidden layer
     */
    public Network(int inputSize, int outputSize, int hiddenAmount, int[] hiddenSize) {

        this();
        if (hiddenSize.length != hiddenAmount)
            throw new IllegalArgumentException("hiddenSize count not rigth");
        if (hiddenAmount < 0 || outputSize <= 0 || inputSize <= 0)
            throw new IllegalArgumentException("all Sizes must be >0");

        for (int i = 0; i < hiddenAmount; i++) {
            if (hiddenSize[i] <= 0)
                throw new IllegalArgumentException("all Sizes must be >0");
        }
        this.hiddenAmount=hiddenAmount;
        this.hiddenSize=hiddenSize;
        this.inputSize=inputSize;
        this.outSize=outputSize;

        //IN Layer setup
        Layer inLayers = new Layer(LayerType.IN);

        //IN Neuron setup&initialization
        Neuron[] inNeurons = new Neuron[inputSize];
        for (int i = 0; i < inNeurons.length; i++) {
            inNeurons[i] = new Neuron(i, Math.random());
        }
        Layer[] hiddenLayers = null;
        Neuron[][] hiddenNeurons = null;
        if (hiddenAmount > 0) {

            //HIDDEN Layer setup
            hiddenLayers = new Layer[hiddenAmount];


            //HIDDEN Neuron setup
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

        //OUT Layer setup
        Layer outLayers = new Layer(LayerType.OUT);

        //OUT Neuron setup&initialization
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
                    connect(hiddenLayers[i], hiddenLayers[i + i]);
                }
            }
        } else {
            connect(inLayers, outLayers);
        }
        //adding The Layers to the Network used to calculate later
        layers.add(inLayers);
        for (int i = 0; i < hiddenAmount; i++) {
            layers.add(hiddenLayers[i]);
        }
        layers.add(outLayers);
    }

    /**
     * Create a Deep Feed Forward Network with everything set up.
     *
     * @param inputSize    the preferred size of
     * @param outputSize   the preferred size of output layers
     * @param hiddenAmount the preferred ammount of hidden layers
     * @param hiddenSize   the preferred size of the hidden layers for every hidden layer
     * @return a DFF Network with everything set up.
     */
    public static Network createDFF(int inputSize, int outputSize, int hiddenAmount, int hiddenSize) {
        int[] hidden = new int[hiddenAmount];
        for (int i = 0; i < hiddenAmount; i++) {
            hidden[i] = hiddenSize;
        }
        return new Network(inputSize, outputSize, hiddenAmount, hidden);
    }

    /**
     * Adds a layer to {@link Network#layers}
     *
     * @param layer {@link Layer} to add to {@link #layers}
     * @return true if the Layer was added. Otherwise false
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
     * @return true if the Layer was removed. Otherwise false
     */
    public boolean removeLayer(Layer layer) {
        if (layers.contains(layer))
            layers.remove(layer);
        else
            return false;
        return true;
    }

    /**
     * clears layers and fills it with the Layers in the Parameter.
     * @param layers {@link #layers}
     */
    public void setLayers(ArrayList<Layer> layers) {
        this.layers = layers;
    }

    /**
     * Returns the size of the input nodes
     * @return {@link #inputSize}
     */
    public int getInputSize() {
        return inputSize;
    }

    /**
     * Returns the size of the output nodes
     * @return {@link #outSize}
     */
    public int getOutSize() {
        return outSize;
    }

    /**
     * Returns the amount of the hidden nodes
     * @return {@link #hiddenAmount}
     */
    public int getHiddenAmount() {
        return hiddenAmount;
    }


    /**
     * Returns the size of all the hidden node sizes
     * @return {@link #hiddenSize}
     */
    public int[] getHiddenSize() {
        return hiddenSize;
    }

    /**
     * Returns the Layers of this Network
     * @return {@link #layers}
     */
    public ArrayList<Layer> getLayers() {
        return layers;
    }

    /**
     * Returns the Layer at a given index
     * @param index index of the Layer
     * @return the requested Layer
     */
    public Layer getLayerByIndex(int index) {
        if (index<layers.size())
            return layers.get(index);
        throw new NullPointerException("index greater than layer size");
    }

    /**
     * Connects the first Layer with the second
     * from layer1 -> layer2 axon
     * from layer1 <- layer2 dendrites
     *
     * @param layer1 first Layer
     * @param layer2 second Layer
     */
    public void connect(Layer layer1, Layer layer2) {
        if (layer1 == null || layer2 == null)
            throw new NullPointerException("layer==null");
        layer1.connectWith(layer2);
    }

    /**
     * Returns the index of a layer in {@link #layers}
     *
     * @param layer The {@link Layer} whose index is requested
     * @return -1 if the index was not found. Otherwise the index of the Layer
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
     * @param in Data for the Input Layer
     * @return the Data that the Network Processes
     */
    public double[] processData(double[] in) {
        double[] ret = new double[layers.get(layers.size() - 1).getNeuronCount()];

        if (layers.size() <= 0)
            throw new IllegalStateException("Network is still empty, cant process Data");
        if (in.length != layers.get(0).getNeuronCount())
            throw new IllegalArgumentException("input size not rigth");
        Layer inLayer = layers.get(0);
        if (inLayer.getType() != LayerType.IN)
            throw new IllegalStateException("cant find the in-Layer");
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

    /**
     * returns the length of the descriptor of a network with the given parameters
     * @param inputSize {@link #inputSize}
     * @param outputSize {@link #outSize}
     * @param hiddenAmount {@link #hiddenAmount}
     * @param hiddenSize {@link #hiddenSize}
     * @return the minimum length a descriptor must have
     */
    public static int getDescriptorLength(int inputSize, int outputSize, int hiddenAmount, int[] hiddenSize) {
        int ret=0;
        if (hiddenAmount>0) {
            ret += inputSize * hiddenSize[0];
            ret+=inputSize;
            for (int i = 0; i < hiddenSize.length-1; i++) {
                ret+=hiddenSize[i]*hiddenSize[i+1];
                ret+=hiddenSize.length;
            }
            ret+=hiddenSize[hiddenSize.length-1]*outputSize;
            ret+=outputSize;
        }else {
            ret += inputSize * outputSize;
        }
        return ret;
    }
}
