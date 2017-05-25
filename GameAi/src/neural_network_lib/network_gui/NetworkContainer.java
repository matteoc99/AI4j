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
        for (NetworkPanel networkPanel : networkPanels)
            if (networkPanel.getEquivalent().equals(network))
                return;
        NetworkPanel networkPanel = new NetworkPanel(network);
        networkPanels.add(networkPanel);
        this.add(networkPanel);
        repaint();
    }

    void refreshNetwork(@NotNull Network network) {
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
    public NetworkGUIComponentType getNetworkGUIComponentType() {
        return NetworkGUIComponentType.NETWORK_CONTAINER;
    }
}
