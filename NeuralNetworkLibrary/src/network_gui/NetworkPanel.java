package network_gui;

import network.Connection;
import network.Layer;
import network.Network;
import network.Neuron;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A NetworkPanel contains all components to display a network
 * It is added to the networkContainer od the NetworkGUI
 *
 * @author Maximilian Estfelller
 * @since 18.05.2017
 */
class NetworkPanel extends JPanel implements NetworkGUIComponent{

    private final int rightPadding = 8;
    private final int leftPadding = 8;
    private final int topPadding = 18;
    private final int botPadding = 8;

    private int width;
    private int height;


    /**
     * Reference to the network.Network of this Panel
     */
    private Network network;

    /**
     * Size of a NetworkPanelNeuron
     */
    private int neuronSize;

    private boolean developerMode = false;

    private boolean focusMode = false;

    /**
     * This is the standard Border used
     * It is a TitleBorder with a CompoundBorder as Border
     * The CompoundBorder consists of a gray LineBorder (Outer) and a EmptyBorder(Inner) as a placeHolder
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

    private ArrayList<ArrayList<NetworkPanelNeuron>> neuronLayers;

    private ArrayList<NetworkPanelConnection> connections;

    NetworkPanel(Network network) {
        this.network = network;
        // Settings
        this.setBorder(NORMAL_BORDER);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(-1, 400));

        createComponents();
        layoutComponents();

        // Listener
        // MouseListener reacts when the Mouse enters and leaves this NetworkPane
        // Used to adjust Borders
        this.addMouseListener(new MyMouseListener());

        // ComponentListener reacts to resizing in order to reposition components
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
        neuronLayers = new ArrayList<>();
        for (Layer layer : network.getLayers()) {
            switch (layer.getType()) {
                case IN:
                    ArrayList<NetworkPanelNeuron> inputLayer = new ArrayList<>();
                    for (int i = 0; i < layer.getNeuronCount(); i++)
                        inputLayer.add(new NetworkPanelNeuron(layer.getNeuronAt(i), Layer.LayerType.IN));
                    neuronLayers.add(inputLayer);
                    break;
                case HIDDEN:
                    ArrayList<NetworkPanelNeuron> hiddenLayer = new ArrayList<>();
                    for (int i = 0; i < layer.getNeuronCount(); i++) {
                        hiddenLayer.add(new NetworkPanelNeuron(layer.getNeuronAt(i), Layer.LayerType.HIDDEN));
                    }
                    neuronLayers.add(hiddenLayer);
                    break;
                case OUT:
                    ArrayList<NetworkPanelNeuron> outputLayer = new ArrayList<>();
                    for (int i = 0; i < layer.getNeuronCount(); i++)
                        outputLayer.add(new NetworkPanelNeuron(layer.getNeuronAt(i), Layer.LayerType.OUT));
                    neuronLayers.add(outputLayer);
                    break;
            }
        }
    }

    private void createConnections() {
        connections = new ArrayList<>();
        for (Neuron neuron : network.getAllNeurons()) {
            connections.addAll(neuron.getDendrites().stream().map(connection -> new NetworkPanelConnection(connection,
                    (NetworkPanelNeuron) findByEquivalent(connection.getFrom()),
                    (NetworkPanelNeuron) findByEquivalent(connection.getTo()))).collect(Collectors.toList()));
        }
    }

    private void layoutComponents() {
        width = getWidth()-rightPadding-leftPadding;
        height = getHeight()-topPadding-botPadding;
        layoutNeurons();
        layoutConnections();
    }

    private void layoutNeurons() {

        int longestLineLength = neuronLayers.size();
        for (ArrayList<NetworkPanelNeuron> layer : neuronLayers)
            if (layer.size() > longestLineLength)
                longestLineLength = layer.size();

        neuronSize = width>height? (int)(height/(longestLineLength*1.75)) :
                (int)(width/(longestLineLength*1.75));

        double layerGap = (width+0.0)/network.getLayers().size();
        double xToDraw = layerGap/2;

        for (ArrayList<NetworkPanelNeuron> layer : neuronLayers) {
            layoutLayer(layer, (int)xToDraw);
            xToDraw+=layerGap;
        }
    }

    private void layoutLayer(ArrayList<NetworkPanelNeuron> layer, int xToDraw) {
        double neuronGap = (height+0.0)/layer.size();
        double yToDraw = neuronGap/2;

        for (NetworkPanelNeuron aLayer : layer) {
            aLayer.setBounds(xToDraw - neuronSize / 2 + leftPadding,
                    (int) yToDraw - neuronSize / 2 + topPadding,
                    neuronSize, neuronSize);
            this.add(aLayer);
            yToDraw += neuronGap;
        }
    }

    private void layoutConnections() {
        for (NetworkPanelConnection connection : connections) {
            int[] layoutData = createConnectionLayoutData(connection.getFrom().getX()+connection.getFrom().getWidth()/2,
                    connection.getFrom().getY()+connection.getFrom().getHeight()/2,
                    connection.getTo().getX()+connection.getTo().getWidth()/2,
                    connection.getTo().getY()+connection.getTo().getHeight()/2);
            connection.setBounds(layoutData[0], layoutData[1], layoutData[2], layoutData[3]);
            connection.setLineDrawOrientation(layoutData[4]);
            this.add(connection);
        }
    }

    private int[] createConnectionLayoutData(int x1, int y1, int x2, int y2) {
        int[] ret = new int[5];
        if (x1<x2) {
            ret[0] = x1;
            ret[2] = x2-x1;
            if (y1<y2)
                ret[4] = NetworkPanelConnection.DOWNWARDS;
            else if (y1>y2)
                ret[4] = NetworkPanelConnection.UPWARDS;
        } else if (x1>x2) {
            ret[0] = x2;
            ret[2] = x1-x2;
            if (y1>y2)
                ret[4] = NetworkPanelConnection.DOWNWARDS;
            else if (y1<y2)
                ret[4] = NetworkPanelConnection.UPWARDS;
        } else {
            ret[0] = x1-5;
            ret[2] = 10;
            ret[4] = NetworkPanelConnection.VERTICAL;
        }

        if (y1<y2) {
            ret[1] = y1;
            ret[3] = y2-y1;
        } else if (y1>y2) {
            ret[1] = y2;
            ret[3] = y1-y2;
        } else {
            ret[1] = y1-5;
            ret[3] = 10;
            ret[4] = NetworkPanelConnection.HORIZONTAL;
        }
        return ret;
    }

    boolean isFocusMode() {
        return this.focusMode;
    }

    void setFocusMode(boolean focus) {
        this.focusMode = focus;
    }

    void tryDeactivateFocusMode() {
        for (ArrayList<NetworkPanelNeuron> neuronLayer : neuronLayers)
            for (NetworkPanelNeuron neuron : neuronLayer)
                if (neuron.getFocusState() != NetworkPanelNeuron.FocusState.NONE)
                    return;
        for (ArrayList<NetworkPanelNeuron> neuronLayer : neuronLayers)
            for (NetworkPanelNeuron neuron : neuronLayer)
                neuron.setFocusState(NetworkPanelNeuron.FocusState.ALL);
        focusMode = false;
        repaint();
    }

    @Override
    public Object getEquivalent() {
        return this.network;
    }

    @Override
    public void setEquivalent(Object equivalent) {
        // Can't change Equivalent of a NetworkPanel
    }

    public ArrayList<ArrayList<NetworkPanelNeuron>> getNeuronLayers() {
        return neuronLayers;
    }

    public ArrayList<NetworkPanelConnection> getConnections() {
        return connections;
    }

    public void toggleDeveloperMode() {
        developerMode=!developerMode;

        for (ArrayList<NetworkPanelNeuron> neuronLayer : neuronLayers)
            neuronLayer.forEach(NetworkPanelNeuron::toggleDeveloperMode);
        connections.forEach(NetworkPanelConnection::toggleDeveloperMode);
    }

    @Override
    public void refresh() {
        for (ArrayList<NetworkPanelNeuron> neuronLayer : neuronLayers)
            neuronLayer.forEach(NetworkPanelNeuron::refresh);
        connections.forEach(NetworkPanelConnection::refresh);
        repaint();
    }

    @Override
    public NetworkGUIComponentType getNetworkGUIComponentType() {
        return NetworkGUIComponentType.NETWORK_PANEL;
    }

    private NetworkGUIComponent findByEquivalent(Object equivalent){
        for (ArrayList<NetworkPanelNeuron> neuronLayer : neuronLayers)
            for (NetworkPanelNeuron networkPanelNeuron : neuronLayer)
                if (networkPanelNeuron.getEquivalent().equals(equivalent))
                    return networkPanelNeuron;
        for (NetworkPanelConnection connection : connections)
            if (connection.getEquivalent().equals(equivalent))
                return connection;
        return null;
    }

    private class MyMouseListener extends MouseAdapter {
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
            if (NetworkPanel.this.contains(e.getPoint()))
                return;
            // changes the Border
            NetworkPanel.this.setBorder(NORMAL_BORDER);
        }
    }
}
