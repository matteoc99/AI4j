package neural_network_lib;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


/**
 * @author Matteo Cosi
 * @since 21.05.2017
 */
public class LayerTest {


    @Test
    public void removeNeuron() {
        Layer l = new Layer(Layer.LayerType.IN);
        Neuron n = new Neuron(0);
        Neuron n2 = new Neuron(0);
        l.addNeuron(n);
        l.addNeuron(n2);
        l.removeNeuron(n);
        assertEquals(l.getNeuronCount(), 1);
        assertEquals(l.getNeuronAt(0), n2);

    }

    @Test
    public void addNeuron() {

    }

    @Test
    public void addNeurons() {

    }

    @Test
    public void connectWith() {

    }

    @Test
    public void getNeuronCount() {

    }


    @Test
    public void feed() {

    }

    @Test
    public void send() {

    }

    @Test
    public void getNeuronAt() {

    }

}
