package neural_network_lib.network_gui;

import neural_network_lib.Connection;

import java.awt.*;

/**
 * @author Maximilian Estfeller
 * @since 21.05.17
 */
class NetworkPanelConnection extends NetworkPanelComponent{

    double weight = 0;

    NetworkPanelNeuron from;
    NetworkPanelNeuron to;

    public NetworkPanelConnection(Connection equivalent, NetworkPanelNeuron from, NetworkPanelNeuron to) {
        super(equivalent);
        this.from = from;
        this.to = to;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D gAlia = (Graphics2D) g;

    }

    @Override
    NetworkComponentType getNetworkComponentType() {
        return NetworkComponentType.CONNECTION;
    }

    private Color getGradientColor(float value) {
        return null;
    }
}
