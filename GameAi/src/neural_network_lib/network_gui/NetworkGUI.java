package neural_network_lib.network_gui;

import neural_network_lib.Network;

import javax.swing.*;
import java.awt.*;


/**
 * @author Maximilian Estfelller
 * @since 17.05.2017
 */
public class NetworkGUI {
    /**
     * Amount of NetworkPanels displayed horizontally
     */
    private int horizontalAmountToDisplay = 1;
    /**
     * Amount of NetworkPanels displayed vertically
     */
    private int verticalAmountToDisplay = 1;
    private JPanel container;
    private JToolBar menuBar;
    private JPanel networkContainer;

    private JFrame frame;

    public NetworkGUI(Network... networks) {
        // Settings
        frame = new JFrame("NetworkDisplay");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Settings end

        // Location and Size
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(0,0, (int)d.getWidth(), (int)d.getHeight());
        frame.setLocationRelativeTo(null);

        // Location and Size end

        // menuBar

        //menuBar.add(new MenuItem("ClickMe", new MenuShortcut(1, true)));

        // menuBar end

        // adds the parent to the contentPane
        frame.getContentPane().add(container);

        // sets visible after everything is set up
        frame.setVisible(true);
    }

    JFrame getFrame() {
        return frame;
    }
    /**
     * Adds a Network to this JFrame
     *
     * @param network to add
     */
    public void addNetwork(Network network) {

    }
}
