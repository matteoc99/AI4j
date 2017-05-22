package neural_network_lib;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author Matteo Cosi
 * @since 21.05.2017
 */
public class NetworkTest {

    @Test
    public void addLayer() {
        Network n = new Network(3, 4, 1, new int[]{3});
        assertEquals(3, n.getLayers().size());
        Layer l = new Layer(Layer.LayerType.HIDDEN);
        n.addLayer(l);
        assertEquals(4, n.getLayers().size());

    }

    @Test
    public void removeLayer() {
        Network n = new Network(3, 4, 1, new int[]{3});
        assertEquals(3, n.getLayers().size());
        Layer l = n.getLayerByIndex(1);
        n.removeLayer(l);
        assertEquals(2, n.getLayers().size());
    }


    @Test
    public void getLayerByIndex() {
        Network n = new Network(3, 4, 1, new int[]{3});
        assertEquals(3, n.getLayers().size());
        assertEquals(Layer.LayerType.HIDDEN, n.getLayerByIndex(1).getType());
        assertEquals(Layer.LayerType.OUT, n.getLayerByIndex(2).getType());
    }

    @Test
    public void connect() {
        Network n = new Network(3, 4, 1, new int[]{3});
        int count = 0;
        int conCount = 0;
        for (Neuron neuron : n.getAllNeurons()) {
            for (Connection con : neuron.getAxons()) {
                conCount++;
            }
            count++;
        }
        assertEquals(count, 10);
        assertEquals(conCount, 21);

        n.connect(n.getLayerByIndex(0), n.getLayerByIndex(2));

        count = 0;
        conCount = 0;
        for (Neuron neuron : n.getAllNeurons()) {
            for (Connection con : neuron.getAxons()) {
                conCount++;
            }
            count++;
        }
        assertEquals(count, 10);
        assertEquals(conCount, 24);

    }

    @Test
    public void getIndexOfLayer() {
        Network n = new Network(3, 4, 1, new int[]{3});
        assertEquals(3, n.getLayers().size());
        Layer l = new Layer(Layer.LayerType.HIDDEN);
        n.addLayer(l);
        assertEquals(4, n.getLayers().size());
        assertEquals(3, n.getIndexOfLayer(l));
    }
    @Test
    public void processData() {

    }


    @Test
    public void getDescriptorLength() {

    }

    @Test
    public void generateDescriptor() {

    }

    @Test
    public void getTotalNeuronCount() {

    }

    @Test
    public void getAllNeurons() {
        Network n = new Network(3, 4, 1, new int[]{3});
        assertEquals(3, n.getLayers().size());
        Layer l = new Layer(Layer.LayerType.HIDDEN);
        Neuron neuron = new Neuron(0);
        l.addNeuron(neuron);
        assertEquals(n.getAllNeurons().size(), 10);

        int count = 0;
        int conCount = 0;
        for (Neuron neu : n.getAllNeurons()) {
            for (Connection con : neu.getAxons()) {
                conCount++;
            }
            count++;
        }
        assertEquals(count, 10);
        assertEquals(conCount, 21);

        n.addLayer(l);
        assertEquals(n.getAllNeurons().size(), 11);

        n.connect(n.getLayerByIndex(2), l);
        count = 0;
        conCount = 0;
        for (Neuron neu : n.getAllNeurons()) {
            for (Connection con : neu.getAxons()) {
                conCount++;
            }
            count++;
        }
        assertEquals(count, 11);
        assertEquals(conCount, 25);
    }
}
