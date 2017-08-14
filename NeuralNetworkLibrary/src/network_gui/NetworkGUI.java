package network_gui;

import network.Network;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;


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

    public NetworkGUI() {
        // KeyListener
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F6 && networkContainer != null)
                    networkContainer.toggleDeveloperMode();
            }
        });

        // Settings
        this.setTitle("NetworkDisplay");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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

        JMenuItem refresh = new JMenuItem("Refresh All");
        refresh.addActionListener((event) -> networkContainer.refresh());
        file.add(refresh);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener((event) -> System.exit(0));
        file.add(exit);

        menuBar.add(file);

        // adds the editMenu to the menuBar and fills it with Items
        JMenu edit = new JMenu("Edit");

        JMenuItem reset = new JMenuItem("Restore Default");
        reset.addActionListener((event) -> networkContainer.reset());
        edit.add(reset);

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
        JLabel leftPlaceHolder = new JLabel("Look at me, I'm a placeholder!");
        leftPlaceHolder.setPreferredSize(new Dimension(200,(int)(d.getHeight()/4*3)));
        centerSplitter.add(leftPlaceHolder);

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

        container.add(scroll, BorderLayout.CENTER);
        // components end

        this.getContentPane().add(container);

        this.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            centerSplitter.setDividerLocation(.1);
            endSplitter.setDividerLocation(.9);
        });
    }


    public void addNetwork(Network network, String networkName) {
        SwingUtilities.invokeLater(() -> networkContainer.addNetwork(network, networkName));
    }

    public void refreshNetwork(Network network) {
        SwingUtilities.invokeLater(() -> networkContainer.refreshNetwork(network));
    }

    public static void main(String[] args) {
        NetworkGUI g = new NetworkGUI();

        Network network = new Network(3, 2, 2, new int[]{6, 3});
        g.addNetwork(network, "NameName");
        g.addNetwork(new Network(3, 2, 1, new int[]{2}), "123");
        g.addNetwork(new Network(6, 3, 3, new int[]{8, 4, 5}), "455");
        g.addNetwork(new Network(3, 2, 1, new int[]{2}), "jwg");

        try {
            while(true) {
                TimeUnit.MILLISECONDS.sleep(500);
                network.mutateHard(10);
            }
        } catch (InterruptedException e) {

        }
    }
}