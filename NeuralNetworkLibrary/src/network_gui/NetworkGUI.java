package network_gui;

import network.Network;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
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

    public NetworkGUI(Network... networks) {
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

        this.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            centerSplitter.setDividerLocation(.1);
            endSplitter.setDividerLocation(.9);
        });
    }


    public void addNetwork(Network network) {
        SwingUtilities.invokeLater(() -> networkContainer.addNetwork(network));
    }

    public void refreshNetwork(Network network) {
        SwingUtilities.invokeLater(() -> networkContainer.refreshNetwork(network));
    }

    public static void main(String[] args) {
        NetworkGUI g = new NetworkGUI();

        Network network = new Network(2,2,2, new int[]{3,3});
        g.addNetwork(network);

        new Thread(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                network.getAllNeurons().get(0).getAxons().get(0).setWeight(-1);
                g.refreshNetwork(network);
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                network.getAllNeurons().get(0).getAxons().get(0).setWeight(1);
                g.refreshNetwork(network);
            }
        }).start();


        g.addNetwork(new network.Network(1,1,0,new int[]{}));
        g.addNetwork(new network.Network(2,2,0,new int[]{}));
        g.addNetwork(new network.Network(2,3,1,new int[]{2}));
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        g.addNetwork(new network.Network(3,2,3,new int[]{4,2,2}));
        g.addNetwork(new network.Network(2,3,2,new int[]{2,3}));
        g.addNetwork(new network.Network(2,4,2,new int[]{3,2}));
        g.addNetwork(new network.Network(4,1,3,new int[]{2,3,3}));
        g.addNetwork(new network.Network(2,3,3,new int[]{2,1,2}));
        g.addNetwork(new network.Network(3,2,4,new int[]{3,3,3,1}));
        g.addNetwork(new network.Network(2,2,3,new int[]{3,4,4}));
        g.addNetwork(new network.Network(1,3,3,new int[]{2,3,2}));
        g.addNetwork(new network.Network(2,2,2,new int[]{2,2}));
        g.addNetwork(new network.Network(2,3,0,new int[]{}));
        g.addNetwork(new network.Network(2,3,3,new int[]{2,3,3}));
        g.addNetwork(new network.Network(5,2,2,new int[]{4,4}));
        g.addNetwork(new network.Network(3,3,3,new int[]{2,2,4}));
        g.addNetwork(new network.Network(4,3,3,new int[]{4,2,4}));
        g.addNetwork(new network.Network(3,4,2,new int[]{2,3}));
        g.addNetwork(new network.Network(2,2,3,new int[]{2,1,3}));
        g.addNetwork(new network.Network(7,2,4, new int[]{6, 5, 4, 3}));
        g.addNetwork(new network.Network(2,5,3,new int[]{2,1,3}));
    }
}