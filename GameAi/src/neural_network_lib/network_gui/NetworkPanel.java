package neural_network_lib.network_gui;

import neural_network_lib.Connection;
import neural_network_lib.Layer;
import neural_network_lib.Layer.LayerType;
import neural_network_lib.Network;
import neural_network_lib.Neuron;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 * A NetworkPanel contains all components to display a network
 * It is added to the networkContainer od the NetworkGUI
 *
 * @author Maximilian Estfelller
 * @since 18.05.2017
 */
class NetworkPanel extends JPanel {

    /**
     * Reference to the Network of this Panel
     */
    private Network network;

    /**
     * Size of a NetworkPanelNeuron
     */
    private int neuronSize;

    /**
     * This is the standard Border used
     * It is a TitleBorder with a CompoundBorder as Border
     * The CompoundBorder consists of a gray LineBorder (Outer) and a EmptyBoder(Inner) as a placeHolder
     */
    private static final Border NORMAL_BORDER = new TitledBorder(
            new CompoundBorder(
                    new LineBorder(Color.GRAY,3,true),
                    new EmptyBorder(2,2,2,2)),
            "Network-23451", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
            new Font("Arial Black", Font.PLAIN, 14));

    /**
     * This is the Border used, while the mouse is on this JPanel
     * It is a TitleBorder with a CompoundBorder as Border
     * The CompoundBorder consists of a gray LineBorder (Outer) and another gray LineBorder(Inner),
     * so that the Border's size can be increases without increasing the size of this JPanel
     */
    private static final Border SELECTED_BORDER = new TitledBorder(
            new CompoundBorder(
                    new LineBorder(Color.GRAY,3,true),
                    new LineBorder(Color.GRAY,2,false)),
            "Network-23451", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
            new Font("Arial Black", Font.PLAIN, 14));

    private LinkedList<LinkedList<NetworkPanelNeuron>> neuronLayers;

    private LinkedList<NetworkPanelConnection> connections;

    NetworkPanel(Network network) {
        this.network = network;
        // Settings
        this.setBorder(NORMAL_BORDER);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(-1, 400));

        createComponents();

        // Listener
        // MouseListener reacts when the Mouse enters and leaves this NetworkPane
        // Used to adjust Borders
        this.addMouseListener(new NetworkMouseListener());

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                layoutComponents();
            }
        });
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }

    private void createComponents() {
        createNeurons();
        createConnections();
    }

    private void createNeurons() {
        neuronLayers = new LinkedList<>();
        for (Layer layer : network.getLayers()) {
            switch (layer.getType()) {
                case IN:
                    LinkedList<NetworkPanelNeuron> inputLayer = new LinkedList<>();
                    for (int i = 0; i < layer.getNeuronCount(); i++)
                        inputLayer.add(new NetworkPanelNeuron(layer.getNeuronAt(i), LayerType.IN));
                    neuronLayers.add(inputLayer);
                    break;
                case HIDDEN:
                    LinkedList<NetworkPanelNeuron> hiddenLayer = new LinkedList<>();
                    for (int i = 0; i < layer.getNeuronCount(); i++) {
                        hiddenLayer.add(new NetworkPanelNeuron(layer.getNeuronAt(i), LayerType.HIDDEN));
                    }
                    neuronLayers.add(hiddenLayer);
                    break;
                case OUT:
                    LinkedList<NetworkPanelNeuron> outputLayer = new LinkedList<>();
                    for (int i = 0; i < layer.getNeuronCount(); i++)
                        outputLayer.add(new NetworkPanelNeuron(layer.getNeuronAt(i), LayerType.OUT));
                    neuronLayers.add(outputLayer);
                    break;
            }
        }
    }

    private void createConnections() {
        int shouldBe = 0;
        connections = new LinkedList<>();
        for (Neuron neuron : network.getAllNeurons()) {
            for (Connection connection : neuron.getDendrites()) {
                shouldBe++;
                try {
                    connections.add(new NetworkPanelConnection(connection,
                            (NetworkPanelNeuron) findByEquivalent(connection.getFrom()),
                            (NetworkPanelNeuron) findByEquivalent(connection.getTo())));
                } catch (Exception ignored) {}
            }
        }
        System.out.println(shouldBe);
        System.out.println(connections.size());
    }

    private NetworkPanelComponent findByEquivalent(Object equivalent) throws Exception{
        for (LinkedList<NetworkPanelNeuron> neuronLayer : neuronLayers)
            for (NetworkPanelNeuron networkPanelNeuron : neuronLayer)
                if (networkPanelNeuron.getEquivalent().equals(equivalent))
                    return networkPanelNeuron;
        for (NetworkPanelConnection connection : connections)
            if (connection.getEquivalent().equals(equivalent))
                return connection;
        System.out.println("badPeep");
        throw new Exception();
    }

    private void layoutComponents() {
        int longestLineLength = neuronLayers.size();
        for (LinkedList<NetworkPanelNeuron> layer : neuronLayers)
            if (layer.size() > longestLineLength)
                longestLineLength = layer.size();

        neuronSize = getWidth()>getHeight()? (int)(this.getHeight()/(longestLineLength*1.75)) :
                (int)(this.getWidth()/(longestLineLength*1.75));

        double layerGap = (this.getWidth()+0.0)/network.getLayers().size();
        double xToDraw = layerGap/2;

        for (LinkedList<NetworkPanelNeuron> layer : neuronLayers) {
            layoutLayer(layer, (int)xToDraw);
            xToDraw+=layerGap;
        }
    }

    private void layoutLayer(LinkedList<NetworkPanelNeuron> layer, int xToDraw) {
        double neuronGap = (this.getHeight()+0.0)/layer.size();
        double yToDraw = neuronGap/2;

        for (NetworkPanelNeuron aLayer : layer) {
            aLayer.setBounds(xToDraw - neuronSize / 2, (int) yToDraw - neuronSize / 2, neuronSize, neuronSize);
            this.add(aLayer);
            yToDraw += neuronGap;
        }
    }

    private class NetworkMouseListener extends MouseAdapter {
        /**
         * Increases the size of this NetworkPanel significantly
         * and thereby overlaps the other NetworkPanels
         */
        @Override
        public void mousePressed(MouseEvent e) {
        }

        /**
         * Slight changes to the JPanel in order to indicate that
         * this JPanel is clickable
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            // changes the Border
            NetworkPanel.this.setBorder(SELECTED_BORDER);
        }

        /**
         * Restores default
         */
        @Override
        public void mouseExited(MouseEvent e) {
            // changes the Border
            NetworkPanel.this.setBorder(NORMAL_BORDER);
        }
    }
}
