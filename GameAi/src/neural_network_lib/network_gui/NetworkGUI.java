package neural_network_lib.network_gui;

import com.sun.istack.internal.NotNull;
import neural_network_lib.Network;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.util.LinkedList;


/**
 * @author Maximilian Estfelller
 * @since 17.05.2017
 */
public class NetworkGUI extends JFrame{
    /**
     * Amount of NetworkPanels displayed horizontally on the screen
     */
    private int horizontalAmountToDisplay;
    /**
     * Amount of NetworkPanels that should be displayed on the screen in possible
     */
    private int preferredHorizontalAmountToDisplay = 3;

    /**
     * List stores all created NetworkPanels
     */
    private LinkedList<NetworkPanel> networkPanels = new LinkedList<>();

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
    private GridLayout networkContainerGridLayout;

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

        container.add(menuBar, BorderLayout.PAGE_START);

        // JSplitPane for splitting the centerSplitter and the bottom JPanel
        JSplitPane endSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        endSplitter.setUI(new BasicSplitPaneUI(){
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this);
            }
        });

        // JSplitPane for splitting the networkContainer and the left JPanel
        JSplitPane centerSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        centerSplitter.setUI(new BasicSplitPaneUI(){
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this);
            }
        });

        // No usage yet
        JLabel westPlaceHolder = new JLabel("Look at me, I'm a placeholder!");
        westPlaceHolder.setPreferredSize(new Dimension(200,(int)(d.getHeight()/4*3)));
        centerSplitter.add(westPlaceHolder);

        // sets the amount of cols within the networkContainer on 1-preferredAmount
        horizontalAmountToDisplay = networks.length<preferredHorizontalAmountToDisplay ?
                networks.length+1: preferredHorizontalAmountToDisplay;
        // creates a GridLayout for the networkContainer
        networkContainerGridLayout = new GridLayout(0, horizontalAmountToDisplay);
        networkContainerGridLayout.setHgap(20);
        networkContainerGridLayout.setVgap(20);
        // JPanel containing all NetworkPanels
        networkContainer = new JPanel(networkContainerGridLayout);
        // margin
        networkContainer.setBorder(new EmptyBorder(5,20,5,20));

        JScrollPane scroll = new JScrollPane(networkContainer);
        // speedUp
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBorder(null);
        centerSplitter.add(scroll);
        endSplitter.add(centerSplitter);


        // No usage yet
        JLabel bottomPlaceHolder = new JLabel("Look at me, I'm a placeholder!", SwingConstants.CENTER);
        bottomPlaceHolder.setPreferredSize(new Dimension(-1,100));
        endSplitter.add(bottomPlaceHolder);

        container.add(endSplitter, BorderLayout.CENTER);
        // components end

        this.getContentPane().add(container);

        // adds the first set of Networks to this NetworkGUI
        for (Network network : networks) {
            this.addNetwork(network);
        }

        this.setVisible(true);
    }



    /**
     * Adds a Network to this JFrame
     * A Network is displayed in form of a NetworkPanel
     *
     * @param network to add
     */
    public void addNetwork(@NotNull Network network) {
        if (network == null) return;
        NetworkPanel networkPanel = new NetworkPanel(network);
        networkPanels.add(networkPanel);
        if (horizontalAmountToDisplay<preferredHorizontalAmountToDisplay &&
                horizontalAmountToDisplay<networkPanels.size()) {
            horizontalAmountToDisplay++;
            networkContainerGridLayout.setColumns(horizontalAmountToDisplay);
        }
        networkContainer.add(networkPanel);
    }

    public static void main(String[] args) {
        NetworkGUI g = new NetworkGUI();
        for (int i = 0; i < 23; i++) {
            g.addNetwork(new Network());
        }
    }
}