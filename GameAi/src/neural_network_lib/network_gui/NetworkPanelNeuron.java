package neural_network_lib.network_gui;

import java.awt.*;

import neural_network_lib.Layer.LayerType;
import neural_network_lib.Neuron;

import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * @author Maximilian Estfelller
 * @since 19.05.2017
 */
class NetworkPanelNeuron extends JComponent implements NetworkGUIComponent{

    private boolean developerMode = false;

    /**
     * A Neuron is drawn with two different colors, depending on their type
     * The outerColor is the color of the border
     */
    private Color innerColor;
    private Color outerColor;
    private RadialGradientPaint paint;

    private LayerType type;

    private Object equivalent;

    NetworkPanelNeuron(Neuron equivalent, LayerType type) {
        this.setEquivalent(equivalent);
        this.type = type;
        switch (type) {
            case IN:
                innerColor = new Color(250,250,0).brighter();
                outerColor = new Color(250,250,0);
                break;
            case HIDDEN:
                innerColor = new Color(125,250,0).brighter();
                outerColor = new Color(125,250,0);
                break;
            case OUT:
                innerColor = new Color(250,125,0).brighter();
                outerColor = new Color(250,125,0);
                break;
        }
    }

    public LayerType getLayerType() {
        return this.type;
    }

    @Override
    public Object getEquivalent() {
        return this.equivalent;
    }

    @Override
    public void setEquivalent(Object equivalent) {
        this.equivalent = equivalent;
    }

    public void toggleDeveloperMode() {
        developerMode=!developerMode;
        if (developerMode)
            this.setBorder(new LineBorder(Color.RED, 1));
        else
            this.setBorder(null);
    }

    @Override
    public NetworkGUIComponentType getNetworkGUIComponentType() {
        return NetworkGUIComponentType.NEURON;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int outRad = (getWidth()-getWidth()/10)/2;
        int inRad = outRad-getWidth()/10;

        float[] dist = {0.1f, 1f};
        paint = new RadialGradientPaint(getWidth()/2, getHeight()/2, inRad+1, dist,
                new Color[]{innerColor, outerColor}, MultipleGradientPaint.CycleMethod.REPEAT);

        Graphics2D gAlia = (Graphics2D) g;

        gAlia.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gAlia.setPaint(outerColor);
        gAlia.fillOval(getWidth()/2-outRad, getWidth()/2-outRad, outRad*2, outRad*2);
        gAlia.setPaint(Color.BLACK);
        gAlia.drawOval(getWidth()/2-outRad, getWidth()/2-outRad, outRad*2, outRad*2);
        gAlia.setPaint(paint);
        gAlia.fillOval(getWidth()/2-inRad, getWidth()/2-inRad, inRad*2, inRad*2);
    }
}
