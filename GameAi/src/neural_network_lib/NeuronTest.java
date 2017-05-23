package neural_network_lib;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Matteo Cosi
 * @since 21.05.2017
 */
public class NeuronTest {

    Neuron neuronToTest;
    Neuron neuronToTestWith;

    Network networkOfNeuron;
    Function functionOfNeuron;

    @Before
    public void construct() {
        networkOfNeuron = new Network(1,1,0, new int[]{});
        functionOfNeuron = val -> val*2/3;
        neuronToTest = new Neuron(0, 1, networkOfNeuron.getLayerByIndex(0), functionOfNeuron);
        neuronToTestWith = new Neuron(0, 1, networkOfNeuron.getLayerByIndex(1), functionOfNeuron);
        assert (neuronToTest.getIndex() == 0);
        assert (neuronToTest.getBias() == 1);
        assertEquals(networkOfNeuron.getLayerByIndex(0), neuronToTest.getMyLayer());
        assertEquals(neuronToTest.getFunction(), functionOfNeuron);
    }

    @Test
    public void setMyLayer() {
        neuronToTest.setMyLayer(networkOfNeuron.getLayerByIndex(1));
        assertEquals(networkOfNeuron.getLayerByIndex(1), neuronToTest.getMyLayer());
        neuronToTest.setMyLayer(null);
        assertEquals(networkOfNeuron.getLayerByIndex(1), neuronToTest.getMyLayer());
    }

    @Test
    public void setAxons() {
        Connection connection = new Connection(neuronToTest, neuronToTestWith, 1, true);
        assertEquals(neuronToTest.getAxonByToIndex(0), connection);
    }

    @Test
    public void setDendrites() {
        Connection connection = new Connection(neuronToTest, neuronToTestWith, 1, true);
        assertEquals(neuronToTestWith.getDendriteByFromIndex(0), connection);
    }


    @Test
    public void getValue() {

    }

    @Test
    public void setValue() {

    }

    @Test
    public void setBias() {

    }

    @Test
    public void send() {

    }

    @Test
    public void addAxon() {

    }

    @Test
    public void addDendrite( ) {

    }

    @Test
    public void removeAxon() {

    }

    @Test
    public void removeDendrite() {

    }

    @Test
    public void getAxonByToIndex() {

    }

    @Test
    public void getDendriteByFromIndex() {

    }

    @Test
    public void toggleAxonByToIndex() {

    }

    @Test
    public void toggleDendriteByFromIndex() {

    }

    @Test
    public void receive() {

     }

    @Test
    public void getAxonAt() {
    }

    @Test
    public void getDendriteAt() {
    }

    @Test
    public void containsAxon() {
    }

    @Test
    public void containsDendrite() {
    }

    @Test
    public void containsConnection() {
    }
}
