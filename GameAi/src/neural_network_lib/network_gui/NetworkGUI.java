package neural_network_lib.network_gui;

import com.sun.istack.internal.NotNull;
import neural_network_lib.Network;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;


/**
 * @author Maximilian Estfelller
 * @since 17.05.2017
 */
public class NetworkGUI extends JFrame{
    /**
     * Amount of NetworkPanels displayed horizontally on the screen
     */
    private int horizontalAmountToDisplay = 3;

    /**
     * Container for all components
     */
    private JPanel container;

    /**
     * MenuBar holdings all Menus
     */
    private JMenuBar menuBar;

    /**
     * Container for all NetworkPanels
     */
    private JPanel networkContainer;

    public NetworkGUI(Network... networks) {

        // Settings
        this.setTitle("NetworkDisplay");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Settings end

        // Location and Size
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0,0, (int)d.getWidth(), (int)d.getHeight());
        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Location and Size end

        // components
        BorderLayout containerBorderLayout = new BorderLayout();
        container = new JPanel(containerBorderLayout);
        container.setBounds(0,0,getWidth(), getHeight());

        // menuBar is fixed at the top the JFrame
        menuBar = new JMenuBar();

        // adds the fileMenu to the menuBar and fills it with Items
        JMenu file = new JMenu("File");
        JMenuItem settings = new JMenuItem("Settings");
        file.add(settings);
        JMenuItem synchronize = new JMenuItem("Synchronize");
        file.add(synchronize);
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        menuBar.add(file);

        // adds the editMenu to the menuBar and fills it with Items
        JMenu edit = new JMenu("Edit");
        JMenuItem restore = new JMenuItem("Restore NetworkPanels");
        edit.add(restore);
        menuBar.add(edit);

        // adds the menuBar to the container, page-start
        container.add(menuBar, BorderLayout.PAGE_START);

        // creates a GridLayout for the networkContainer
        GridLayout networkContainerGridLayout = new GridLayout(0, horizontalAmountToDisplay);
        // gaps
        networkContainerGridLayout.setHgap(20);
        networkContainerGridLayout.setVgap(20);
        // JPanel containing all NetworkPanels
        networkContainer = new JPanel(networkContainerGridLayout);
        // margin
        networkContainer.setBorder(new EmptyBorder(5,20,5,20));

        // adds the networkContainer to a JScrollPanel
        JScrollPane scroll = new JScrollPane(networkContainer);
        // speedUp
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        // adds the JScrollPanel to the container, page-center
        container.add(scroll, BorderLayout.CENTER);

        // No usage yet
        JLabel placeHolder = new JLabel("Look at me, I'm a placeholder!");
        placeHolder.setPreferredSize(new Dimension(-1,50));
        container.add(placeHolder, BorderLayout.PAGE_END);
        // components end

        // adds the parent of all components to the contentPane
        this.getContentPane().add(container);

        // adds the first set of Networks to this NetworkGUI
        for (Network network : networks) {
            this.addNetwork(network);
        }

        // sets visible after everything is set up
        this.setVisible(true);
    }



    /**
     * Adds a Network to this JFrame
     * A Network is displayed in form of a NetworkPanel
     *
     * @param network to add
     */
    public void addNetwork(@NotNull Network network) {
        //if (network == null) return;
        networkContainer.add(new NetworkPanel(network));
    }

    public static void main(String[] args) {
        NetworkGUI g = new NetworkGUI();
        for (int i = 0; i < 16; i++) {
            g.addNetwork(new Network());
        }
    }
}

// TODO: 19.05.2017 Fix bug when less networks than cols are added