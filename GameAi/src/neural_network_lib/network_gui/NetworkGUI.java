package neural_network_lib.network_gui;

import neural_network_lib.Network;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


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
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));
        networkContainer.add(new NetworkPanel(null));



        JScrollPane scroll = new JScrollPane(networkContainer);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        container.add(scroll, BorderLayout.CENTER);

        JLabel placeHolder = new JLabel("Look at me, I'm a placeholder!");
        placeHolder.setPreferredSize(new Dimension(-1,50));
        container.add(placeHolder, BorderLayout.PAGE_END);
        // components end

        // adds the parent of all components to the contentPane
        this.getContentPane().add(container);

        // sets visible after everything is set up
        this.setVisible(true);
    }

    /**
     * Adds a Network to this JFrame
     * A Network is displayed in form of a NetworkPanel
     *
     * @param network to add
     */
    public void addNetwork(Network network) {
        networkContainer.add(new NetworkPanel(network));
    }

    /**
     * A NetworkPanel contains all components to display a network
     * It is added to the networkContainer od the NetworkGUI
     */
    private class NetworkPanel extends JPanel{

        private NetworkPanel(Network network) {
            this.setBorder(new TitledBorder(new LineBorder(Color.GRAY,3,true),
                    "Network-23451", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                    new Font("Arial Black", Font.PLAIN, 14)));
            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    // Keeps this JPanel in a 16:9 resolution
                    if (NetworkPanel.this.getWidth()>=240)
                        NetworkPanel.this.setPreferredSize(new Dimension(-1, NetworkPanel.this.getWidth()/16*9));
                    else
                        NetworkPanel.this.setPreferredSize(new Dimension(240,135));
                }
            });
            this.addMouseListener(new MouseAdapter() {
                /**
                 * Increases the size of this NetworkPanel significantly
                 * and thereby overlaps the other NetworkPanels
                 */
                @Override
                public void mousePressed(MouseEvent e) {
                }

                /**
                 * Slight changes to the JPanel in order to indicate that
                 * this JPanel is clickable
                 */
                @Override
                public void mouseEntered(MouseEvent e) {
                    // changes the Border
                    NetworkPanel.this.setBorder(new TitledBorder(new LineBorder(Color.GRAY.darker(), 5, true),
                            "Network-23451", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                            new Font("Arial Black", Font.PLAIN, 14)));
                }

                /**
                 * Restores default
                 */
                @Override
                public void mouseExited(MouseEvent e) {
                    NetworkPanel.this.setBorder(new TitledBorder(new LineBorder(Color.GRAY,3,true),
                            "Network-23451", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                            new Font("Arial Black", Font.PLAIN, 14)));
                }
            });
        }
    }

    public static void main(String[] args) {
        new NetworkGUI();
    }
}
