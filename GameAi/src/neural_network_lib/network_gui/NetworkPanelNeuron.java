package neural_network_lib.network_gui;

import java.awt.*;

/**
 * @author Maximilian Estfelller
 * @since 19.05.2017
 */
public class NetworkPanelNeuron extends NetworkPanelComponent{
    /**
     * There are 3 Types of graphical neurons: input,hidden,output
     * They differ in color
     */
    enum NeuronType {
        INPUT,
        HIDDEN,
        OUTPUT
    }

    /**
     * A Neuron is drawn with two different colors, depending on their type
     * The outerColor is the color of the border
     */
    Color innerColor;
    Color outerColor;


    NetworkPanelNeuron(NeuronType type) {
        switch (type) {
            case INPUT:
                innerColor = Color.YELLOW;
                outerColor = Color.ORANGE.darker();
                break;
            case HIDDEN:
                innerColor = Color.BLUE.brighter();
                outerColor = Color.MAGENTA.darker();
                break;
            case OUTPUT:
                innerColor = Color.RED;
                outerColor = Color.BLACK.brighter();
                break;
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        int borderSize = getWidth()/8;
        Graphics2D gAlia = (Graphics2D) g;
        gAlia.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gAlia.setColor(outerColor);
        gAlia.fillOval(0,0, getWidth(), getHeight());
        gAlia.setColor(innerColor);
        gAlia.fillOval(borderSize/2,borderSize/2,getWidth()-borderSize, getHeight()-borderSize);
    }
}
