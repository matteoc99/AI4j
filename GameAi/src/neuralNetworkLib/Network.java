package neuralNetworkLib;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Network is a class that combines the Layers and offers some utilities
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
     * Create an empty Network
     */
    public Network() {
        layers = new ArrayList<>();
    }

    /**
     * Create a Network with some Layers
     *
     * @param layers
     */
    public Network(ArrayList<Layer> layers) {
        this();
        if (layers != null)
            this.layers = layers;
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
    public Network(int inputSize, int outputSize, int hiddenAmount, int... hiddenSize) {
        this();
        if (hiddenSize.length != hiddenAmount)
            throw new IllegalArgumentException("hiddenSize count not rigth");
        if (hiddenAmount < 0 || outputSize <= 0 || inputSize <= 0)
            throw new IllegalArgumentException("all Sizes must be >0");

        for (int i = 0; i < hiddenAmount; i++) {
            if (hiddenSize[i] <= 0)
                throw new IllegalArgumentException("all Sizes must be >0");
        }

        //IN Layer setup
        Layer inLayers = new Layer(LayerType.IN);

        //IN Neuron setup&initialization
        Neuron[] inNeurons = new Neuron[inputSize];
        for (int i = 0; i < inNeurons.length; i++) {
            inNeurons[i] = new Neuron(i, Math.random());
        }
        Layer[] hiddenLayers;
        Neuron[][] hiddenNeurons;
        if (hiddenAmount > 0) {
            //HIDDN Layer setup
            hiddenLayers = new Layer[hiddenAmount];

            //HIDDN Neuron setup
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
        Neuron[] outNeurons = new Neuron[hiddenSize[outputSize];
        for (int i = 0; i < outNeurons.length; i++) {
            outNeurons[i] = new Neuron(i, Math.random());
        }
        //connecting all the layers
        if(hiddenAmount>0){
            connect(inLayers,hiddenLayers[0]);
            for (int i = 0; i < hiddenAmount; i++) {
                if(i==hiddenAmount-1){
                    connect(hiddenLayers[i],outLayers);
                }else{
                    connect(hiddenLayers[i],hiddenLayers[i+i]);
                }
            }
        }else{
            connect(inLayers,outLayers);
        }

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
    public Network createDFF(int inputSize, int outputSize, int hiddenAmount, int hiddenSize) {
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
     * Connects the first Layer with the second
     * from layer1 -> layer2 axon
     * from layer1 <- layer2 dendrites
     *
     * @param layer1 first Layer
     * @param layer2 second Layer
     */
    public void connect(Layer layer1, Layer layer2) {
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

}
