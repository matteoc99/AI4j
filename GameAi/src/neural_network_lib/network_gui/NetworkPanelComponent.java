package neural_network_lib.network_gui;

import javax.swing.*;
import java.util.LinkedList;

/**
 * @author Maximilian Estfelller
 * @since 19.05.2017
 */
abstract class NetworkPanelComponent extends JComponent {

    enum NetworkComponentType {
        NEURON,
        CONNECTION
    }

    private final Object equivalent;

    NetworkPanelComponent(Object equivalent) {
        this.equivalent = equivalent;
    }

    Object getEquivalent() {
        return equivalent;
    }

    abstract NetworkComponentType getNetworkComponentType();
}
