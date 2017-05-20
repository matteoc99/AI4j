package neural_network_lib.network_gui;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import neural_network_lib.Network;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

/**
 * A NetworkPanel contains all components to display a network
 * It is added to the networkContainer od the NetworkGUI
 *
 * @author Maximilian Estfelller
 * @since 18.05.2017
 */
class NetworkPanel extends JPanel {

    /**
     * This is the standard Border used
     * It is a TitleBorder with a CompoundBorder as Border
     * The CompoundBorder consists of a gray LineBorder (Outer) and a EmptyBoder(Inner) as a placeHolder
     */
    private Border normalBorder = new TitledBorder(
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
    private Border selectedBorder = new TitledBorder(
            new CompoundBorder(
                    new LineBorder(Color.GRAY,3,true),
                    new LineBorder(Color.GRAY,2,false)),
            "Network-23451", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
            new Font("Arial Black", Font.PLAIN, 14));

    NetworkPanel(Network network) {
        // Settings
        this.setBorder(normalBorder);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(-1, 180));


        // Listener
        // MouseListener reacts when the Mouse enters and leaves this NetworkPane
        // Used to adjust Borders
        this.addMouseListener(new NetworkMouseListener());

        //int layerCount = network.getHiddenAmount()+2;
        //int neuronSize = getWidth()/(layerCount+1);

        //NetworkPanelNeuron neuron = new NetworkPanelNeuron(NetworkPanelNeuron.NeuronType.OUTPUT);
        //neuron.setBounds(30,30,neuronSize,neuronSize);
        //add(neuron);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
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
            NetworkPanel.this.setBorder(selectedBorder);
        }

        /**
         * Restores default
         */
        @Override
        public void mouseExited(MouseEvent e) {
            // changes the Border
            NetworkPanel.this.setBorder(normalBorder);
        }
    }
}
