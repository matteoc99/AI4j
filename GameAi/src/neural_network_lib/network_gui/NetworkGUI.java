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
    private NetworkContainer networkContainer;

    public NetworkGUI(Network... networks) {

        // Settings
        this.setTitle("NetworkDisplay");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Settings end

        // Location and Size
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int)d.getWidth()/4, (int)d.getHeight()/4);
        this.setSize(new Dimension((int)d.getWidth()/2, (int)d.getHeight()/2));
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

        // JPanel containing all NetworkPanels
        networkContainer = new NetworkContainer();

        JScrollPane scroll = new JScrollPane(networkContainer);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // speedUp
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBorder(null);
        scroll.setMinimumSize(new Dimension(520, 280));

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
            networkContainer.addNetwork(network);
        }

        this.pack();
        centerSplitter.setDividerLocation(0.0);
        endSplitter.setDividerLocation(1.0);
        this.setVisible(true);
    }

    public void addNetwork(Network network) {
        networkContainer.addNetwork(network);
    }

    public static void main(String[] args) {
        NetworkGUI g = new NetworkGUI();
        for (int i = 0; i < 23; i++) {
            g.addNetwork(new Network());
        }
    }
}