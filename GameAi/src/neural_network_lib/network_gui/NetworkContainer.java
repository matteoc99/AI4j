package neural_network_lib.network_gui;

import com.sun.istack.internal.NotNull;
import neural_network_lib.Network;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Maximilian Estfelller
 * @since 120.05.2017
 */
class NetworkContainer extends JPanel implements NetworkGUIComponent{

    /**
     * List stores all created NetworkPanels
     */
    private ArrayList<NetworkPanel> networkPanels = new ArrayList<>();

    private boolean developerMode = false;

    NetworkContainer() {
        super();
        this.setBorder(new EmptyBorder(5,20,5,20));

        // creates a GridLayout for the networkContainer
        GridLayout layout = new GridLayout(0, 3);
        layout.setHgap(20);
        layout.setVgap(20);
        this.setLayout(layout);
    }

    /**
     * Adds a Network to this Container
     * A Network is displayed in form of a NetworkPanel
     *
     * @param network to add
     */
    void addNetwork(@NotNull Network network) {
        if (network == null) return;
        NetworkPanel networkPanel = new NetworkPanel(network);
        networkPanels.add(networkPanel);
        this.add(networkPanel);
        repaint();
    }

    @Override
    public Object getEquivalent() {
        // TODO: 23.05.2017
        return null;
    }

    @Override
    public void setEquivalent(Object equivalent) {
        // TODO: 23.05.2017
    }

    public void toggleDeveloperMode() {
        developerMode=!developerMode;
        for (NetworkPanel networkPanel : networkPanels)
            networkPanel.toggleDeveloperMode();
    }

    @Override
    public NetworkGUIComponentType getNetworkGUIComponentType() {
        return NetworkGUIComponentType.NETWORK_CONTAINER;
    }
}
