package neural_network_lib.network_gui;

import neural_network_lib.Network;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


/**
 * @author Maximilian Estfelller
 * @since 17.05.2017
 */
public class NetworkGUI extends JFrame{
    /**
     * Amount of NetworkPanels displayed horizontally on the screen
     */
    private int horizontalAmountToDisplay = 4;

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

        // adds the fileMenu to the menuBar and fills it with Items
        JMenu edit = new JMenu("Edit");
        JMenuItem restore = new JMenuItem("Restore NetworkPanels");
        edit.add(restore);
        menuBar.add(edit);

        // adds the menuBar at the top of the container
        container.add(menuBar, BorderLayout.PAGE_START);

        GridLayout networkContainerGridLayout = new GridLayout(0, horizontalAmountToDisplay);
        networkContainerGridLayout.setHgap(20);
        networkContainerGridLayout.setVgap(20);
        networkContainer = new JPanel(networkContainerGridLayout);
        networkContainer.setBorder(new EmptyBorder(5,20,5,20));


        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));



        container.add(new JScrollPane(networkContainer), BorderLayout.CENTER);
        // components end

        // adds the parent of all components to the contentPane
        this.getContentPane().add(container);

        // sets visible after everything is set up
        this.setVisible(true);
    }

    /**
     * Adds a Network to this JFrame
     *
     * @param network to add
     */
    public void addNetwork(Network network) {

    }

    /**
     * A NetworkPanel contains all components to display a network
     * It is added to the networkContainer od the NetworkGUI
     */
    private class NetworkPanel extends JPanel{

        private NetworkPanel(Network network) {
            //this.setBorder(new );
            this.setBackground(Color.BLUE);
            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    System.out.println(NetworkGUI.this.getClass());
                    super.componentResized(e);
                    // Keeps this JPanel in a 16:9 resolution
                    double prefWidth = (NetworkGUI.this.getWidth()-(horizontalAmountToDisplay+1)*20) /
                            (horizontalAmountToDisplay+1);
                    if (prefWidth<160) prefWidth=160;
                    double prefHeight = prefWidth/16.0*9.0;
                    NetworkPanel.this.setPreferredSize(new Dimension((int)prefWidth, (int)prefHeight));
                    System.out.println(NetworkPanel.this.getWidth());
                    System.out.println(NetworkPanel.this.getHeight());
                }
            });
        }
    }

    public static void main(String[] args) {
        new NetworkGUI();
    }
}
