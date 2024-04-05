package network_gui;

import network.Network;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Maximilian Estfelller
 * @since 12.05.2017
 */
public class NetworkContainer extends JPanel implements NetworkGUIComponent{

    /**
     * List stores all created NetworkPanels
     */
    private ArrayList<NetworkPanel> networkPanels = new ArrayList<>();

    private boolean developerMode = false;

    public NetworkContainer() {
        super();
        this.setBorder(new EmptyBorder(5,20,5,20));

        // creates a GridLayout for the networkContainer
        GridLayout layout = new GridLayout(0, 3);
        layout.setHgap(20);
        layout.setVgap(20);
        this.setLayout(layout);
    }

    /**
     * Adds a network.Network to this Container
     * A network.Network is displayed in form of a NetworkPanel
     *
     * @param network to add
     */
    public void addNetwork(Network network, String networkName) {
        if (network == null) return;
        for (NetworkPanel networkPanel : networkPanels)
            if (networkPanel.getEquivalent().equals(network))
                return;
        NetworkPanel networkPanel = new NetworkPanel(network, networkName);
        networkPanels.add(networkPanel);
        this.add(networkPanel);
        //todo maybe this call isn't needed
        revalidate();
    }

    void refreshNetwork(Network network) {
        if (network == null)
            return;
        NetworkPanel networkPanel = ((NetworkPanel) findPanelByEquivalent(network));
        if (networkPanel != null)
            networkPanel.refresh();
    }

    NetworkGUIComponent findPanelByEquivalent(Object equivalent){
        for (NetworkPanel networkPanel : networkPanels)
            if (networkPanel.getEquivalent().equals(equivalent))
                return networkPanel;
        return null;
    }

    @Override
    public Object getEquivalent() {
        // There is no equivalent for a NetworkContainer
        return null;
    }

    @Override
    public void setEquivalent(Object equivalent) {
        // There is no equivalent for a NetworkContainer
    }

    public void toggleDeveloperMode() {
        developerMode=!developerMode;
        for (NetworkPanel networkPanel : networkPanels)
            networkPanel.toggleDeveloperMode();
    }

    @Override
    public void refresh() {
        for (NetworkPanel networkPanel : networkPanels)
            networkPanel.refresh();
    }

    @Override
    public void reset() {
        for (NetworkPanel networkPanel : networkPanels)
            networkPanel.reset();
        if (developerMode)
            toggleDeveloperMode();
    }

    @Override
    public NetworkGUIComponentType getNetworkGUIComponentType() {
        return NetworkGUIComponentType.NETWORK_CONTAINER;
    }
}
