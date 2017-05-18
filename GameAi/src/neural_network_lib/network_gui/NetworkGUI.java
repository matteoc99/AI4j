package neural_network_lib.network_gui;

import neural_network_lib.Network;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;


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
    private class NetworkPanel extends JPanel {

        /**
         * This is the standard Border used
         * It is a TitleBorder with a CompoundBorder as Border
         * The CompoundBorder consists of a gray LineBorder (Outer) and a EmptyBoder(Inner) as a placeHolder
         */
        private Border normalBorder = new TitledBorder(
                new CompoundBorder(
                    new LineBorder(Color.GRAY,3,true),
                    new EmptyBorder(2,2,2,2)),
                "Network-23451", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                new Font("Arial Black", Font.PLAIN, 14));

        /**
         * This is the Border used, while the mouse is on this JPanel
         * It is a TitleBorder with a CompoundBorder as Border
         * The CompoundBorder consists of a gray LineBorder (Outer) and another gray LineBorder(Inner),
         * so that the Border's size can be increases without increasing the size of this JPanel
         */
        private Border selectedBorder = new TitledBorder(
                new CompoundBorder(
                    new LineBorder(Color.GRAY,3,true),
                    new LineBorder(Color.GRAY,2,false)),
                "Network-23451", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                new Font("Arial Black", Font.PLAIN, 14));

        private NetworkPanel(Network network) {
            this.setBorder(normalBorder);
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
                    NetworkPanel.this.setBorder(selectedBorder);
                }

                /**
                 * Restores default
                 */
                @Override
                public void mouseExited(MouseEvent e) {
                    // changes the Border
                    NetworkPanel.this.setBorder(normalBorder);
                }
            });
        }
    }



    public static void main(String[] args) {
        new NetworkGUI();
    }
}
