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
        //error because  connect with the same index
        assertEquals(conCount, 33);

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
        int len = Network.getDescriptorLength(3, 4, 1, new int[]{3});
        assertEquals(35, len);
        len = Network.getDescriptorLength(3, 4, 0, null);
        assertEquals(22, len);

        len = Network.getDescriptorLength(3, 4, 3, new int[]{3, 3, 3});
        assertEquals(16 + 39 + 1 + 5, len);

        len = Network.getDescriptorLength(3, 4, 4, new int[]{3, 3, 3, 3});
        assertEquals(19 + 48 + 1 + 6, len);
    }

    @Test
    public void generateDescriptor() {
        Network net = Network.createDFF(3, 4, 2, 3);
        double[] desc = net.generateDescriptor();
        int len = desc.length;

        Network net2 = new Network(desc);
        double[] desc2 = net2.generateDescriptor();
        int len2 = desc.length;

        assertEquals(len, len2);
        assertEquals(desc, desc2);
        assertEquals(net.processData(new double[]{0.5, 0.5, 0.5}), net2.processData(new double[]{0.5, 0.5, 0.5}));

    }

    @Test
    public void getTotalNeuronCount() {
        Network n = new Network(3, 4, 1, new int[]{3});
        assertEquals(3, n.getLayers().size());
        Layer l = new Layer(Layer.LayerType.HIDDEN);
        Neuron neuron = new Neuron(0);
        l.addNeuron(neuron);
        assertEquals(n.getTotalNeuronCount(), 10);
    }

    @Test
    public void getAllNeurons() {
        Network n = new Network(3, 4, 1, new int[]{3});
        assertEquals(3, n.getLayers().size());
        Layer l = new Layer(Layer.LayerType.HIDDEN);
        assertEquals(l.getNeuronCount(), 0);
        Neuron neuron = new Neuron(0);

        l.addNeuron(neuron);
        assertEquals(l.getNeuronCount(), 1);

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
        assertEquals(l.getNeuronCount(), 1);
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
