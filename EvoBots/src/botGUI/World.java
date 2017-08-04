package botGUI;


import agent.Agent;
import agent.cosi.CosiAgent;
import botGUI.bot.Body;
import botGUI.bot.Bot;
import botGUI.bot.DragAndDropListener;
import botGUI.bot.Sensor;
import de.craften.ui.swingmaterial.MaterialButton;
import de.craften.ui.swingmaterial.MaterialPanel;
import de.craften.ui.swingmaterial.MaterialProgressSpinner;
import de.craften.ui.swingmaterial.MaterialWindow;
import network.Network;
import network_gui.NetworkContainer;
import network_gui.NetworkGUI;
import network_gui.NetworkPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by mcosi on 14/06/2017.
 */
public class World extends JFrame {


    public enum Optimisation {
        MIN, MEDIUM, MAX
    }


    /**
     * describes the width and height of a World
     */
    public static int WORLD_WIDTH = 100;
    public static int WORLD_HEIGHT = 75;
    /**
     * from 1-1000 describes the amount of land in the World.
     * 1000 is the Maximum
     */
    public static int LAND_AMOUNT = 15;
    /**
     * The bigger the value the smaller the islands are
     */
    public static int LAND_SIZE = 10;
    /**
     * Describes the size of the World
     */
    public static int CHUNK_SIZE = 20;

    public static final int MAX_CHUNK_SIZE = 1024;
    /**
     * Describes the distribution of the food
     */
    public static int FOOD_DISTRIBUTION = 10;


    /**
     * Describes the speed of the Food regrowth
     * smaller value = faster
     */
    private static int FOOD_REGROWTH = 10;

    /**
     * describes how often Chunks are repainted 0-10
     * 0 very often
     * 1 slow
     */
    private static int CHUNK_REFRESH_TIME = 0;

    /**
     * Describes how smooth the Islands are
     * 0: smoothing off
     * 1: soft smoothing
     * 1<: strong smoothing
     * better <10
     */
    public static int SMOOTHING_FAKTOR = 2;

    /**
     * Describes the speed of the moving map
     */
    private static final int MOVE_SPEED = 40;


    //FPS control
    private static int FPS = 60;
    private long timeUntilSleep;
    private int fpsCounter;
    /**
     * The Map of {@link Chunk}s
     */
    public static Chunk[][] map = null;

    /**
     * The container of the World Container
     */
    public Container container;

    /**
     * tells if the Map has finished loading
     */
    private boolean mapLoaded = false;

    /**
     * Contains the World
     */
    public JPanel containerPanel = new JPanel();
    /**
     * Contains the Controls and Settings for the World
     */
    public JPanel controlPanel = new JPanel();

    /**
     * used to resize all at once
     */
    public int resizeCounter = 0;

    /**
     * describes the size of the control Panel
     */
    public int controlPanelWidth = 400;

    /**
     * describes the min Population size, if the value drops below a new {@link Bot} is created
     */
    public static int MIN_POP_SIZE = 0;

    /**
     * Contains the Population
     */
    public ArrayList<Bot> population = new ArrayList<>();

    /**
     * currently selected Bot
     */
    public Bot selectedBot = null;


    /**
     * Listener used to drag the map
     */
    public static DragAndDropListener listener;

    public static Optimisation performance = Optimisation.MIN;

    // public static NetworkGUI networkGUI;

    public boolean pause = false;

    public String currenDateiname = null;

    JProgressBar progressBar;

    public World() {
        setTitle("World");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        CHUNK_SIZE = (int) ((screenSize.getWidth() - controlPanelWidth) / WORLD_WIDTH);
        setBounds(0, 0, screenSize.width, screenSize.height);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);

        // networkGUI = new NetworkGUI();

        listener = new DragAndDropListener(this);

        container = getContentPane();
        container.setLayout(null);
        container.setBackground(Color.gray);

        containerPanel.setBounds(0, 0, WORLD_WIDTH * CHUNK_SIZE, WORLD_HEIGHT * CHUNK_SIZE);
        containerPanel.setLayout(null);

        controlPanel = addControls();

        containerPanel.addMouseListener(listener);
        containerPanel.addMouseMotionListener(listener);


        container.add(controlPanel);
        container.add(containerPanel);

        addControls();

        setVisible(true);

        createMap();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_R:
                        generateNewDialog();
                        break;

                    case KeyEvent.VK_UP:
                        containerPanel.setLocation(containerPanel.getX(), containerPanel.getY() - MOVE_SPEED);
                        break;
                    case KeyEvent.VK_DOWN:
                        containerPanel.setLocation(containerPanel.getX(), containerPanel.getY() + MOVE_SPEED);
                        break;
                    case KeyEvent.VK_LEFT:
                        containerPanel.setLocation(containerPanel.getX() - MOVE_SPEED, containerPanel.getY());
                        break;
                    case KeyEvent.VK_RIGHT:
                        containerPanel.setLocation(containerPanel.getX() + MOVE_SPEED, containerPanel.getY());
                        break;
                    case KeyEvent.VK_H:
                    case KeyEvent.VK_CONTROL:
                        Point p = MouseInfo.getPointerInfo().getLocation();
                        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        screenSize.width -= controlPanelWidth;
                        int xOff = (int) (p.x - screenSize.getWidth() / 2);
                        int yOff = (int) (p.y - screenSize.getHeight() / 2);
                        containerPanel.setLocation(containerPanel.getX() + xOff / 8, containerPanel.getY() + yOff / 8);
                        break;
                    case KeyEvent.VK_L:
                        containerPanel.setLocation(0, 0);
                        break;
                    case KeyEvent.VK_P:
                        pause = !pause;
                        break;

                    //player steuerung for test
                    case KeyEvent.VK_W:
                        population.get(0).yDir = -2;

                        break;
                    case KeyEvent.VK_S:
                        population.get(0).yDir = +2;

                        break;
                    case KeyEvent.VK_A:
                        population.get(0).xDir = -2;

                        break;
                    case KeyEvent.VK_D:
                        population.get(0).xDir = +2;
                        break;
                    case KeyEvent.VK_E:
                        population.get(0).eat();

                        break;
                    case KeyEvent.VK_X:
                        population.get(0).rotateAndResize(-10);
                        break;
                    case KeyEvent.VK_C:
                        population.get(0).sensorRotation = 180;
                        population.get(0).rotateAndResize();
                        break;

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        population.get(0).yDir = 0;

                        break;
                    case KeyEvent.VK_S:
                        population.get(0).yDir = 0;

                        break;
                    case KeyEvent.VK_A:
                        population.get(0).xDir = 0;

                        break;
                    case KeyEvent.VK_D:
                        population.get(0).xDir = 0;

                        break;
                }
            }
        });

        dynamicResizer();

        addMouseWheelListener(new MouseAdapter() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    if (CHUNK_SIZE > 8) {
                        if (resizeCounter <= 0)
                            resizeCounter = 1;
                        CHUNK_SIZE -= e.getWheelRotation() * resizeCounter;
                        resizeCounter = 3;
                        if (performance == Optimisation.MAX)
                            resizeCounter *= 2;
                        if (CHUNK_SIZE <= 0)
                            CHUNK_SIZE = 1;
                    }
                } else {
                    if (CHUNK_SIZE < MAX_CHUNK_SIZE) {
                        if (resizeCounter <= 0)
                            resizeCounter = 1;
                        CHUNK_SIZE -= e.getWheelRotation() * resizeCounter;

                        resizeCounter = 3;
                        if (performance == Optimisation.MAX)
                            resizeCounter *= 2;

                    }
                }
            }

        });
        requestFocus();
    }

    public JPanel addBotStats(Bot b) {
        JPanel ret = new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ret.setBounds(screenSize.width - controlPanelWidth, 0, controlPanelWidth, screenSize.height - 400);
        ret.setBackground(Color.white);
        ret.setLayout(null);


        JLabel[] labels = new JLabel[6];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setBounds(20, 60 * i + 20, 300, 50);
            labels[i].setFont(new Font("Times New Roman", 0, 20));
            ret.add(labels[i]);
        }
        labels[0].setText("Health:" + b.hp);
        labels[1].setText("Age: " + b.age);
        labels[2].setText("Generation: " + b.generation);
        labels[3].setText("Color: " + b.red + " " + b.green + " " + b.blue);
        labels[3].setBackground(new Color(b.red, b.green, b.blue));
        labels[3].setOpaque(true);
        labels[4].setText("Cur. Color: " + b.body.getBodyColor().getRed() + " " + b.body.getBodyColor().getGreen() + " " + b.body.getBodyColor().getBlue());
        labels[4].setBackground(b.body.getBodyColor());
        labels[4].setOpaque(true);

        NetworkPanel con = new NetworkPanel(b.agent.getNet());
        con.setBounds(20, 60 * labels.length, 350, 300);
        ret.add(con);
        con.refresh();
        return ret;
    }


    /**
     * add all the Controls components on a JPanel
     */
    public JPanel addControls() {
        JPanel ret = new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ret.setBounds(screenSize.width - controlPanelWidth, 0, controlPanelWidth, getHeight());
        ret.setBackground(Color.white);
        ret.setLayout(null);


        JSlider fps = new JSlider(JSlider.HORIZONTAL, 0, 300, FPS);
        JSlider food_distrib = new JSlider(JSlider.HORIZONTAL, 0, 80, FOOD_DISTRIBUTION);
        JSlider food_regrowth = new JSlider(JSlider.HORIZONTAL, 0, 100, FOOD_REGROWTH);//bis hier min +1 weil
        JSlider populationSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, MIN_POP_SIZE);


        int xOff = 150;

        JLabel[] labels = new JLabel[4];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setBounds(20, (getHeight() / 12) * i, xOff - 10, getHeight() / 10);
            labels[i].setFont(new Font("Times New Roman", 0, 20));
            ret.add(labels[i]);
        }

        labels[0].setText("FPS");
        labels[1].setText("Food Distrib.");
        labels[2].setText("Food Regrowth");
        labels[3].setText("Population");


        fps.setBounds(xOff, 10 + (getHeight() / 12) * 0, controlPanelWidth - xOff - 20, getHeight() / 10);
        food_distrib.setBounds(xOff, 10 + (getHeight() / 12) * 1, controlPanelWidth - xOff - 20, getHeight() / 10);
        food_regrowth.setBounds(xOff, 10 + (getHeight() / 12) * 2, controlPanelWidth - xOff - 20, getHeight() / 10);
        populationSlider.setBounds(xOff, 10 + (getHeight() / 12) * 3, controlPanelWidth - xOff - 20, getHeight() / 10);


        fps.setPaintLabels(true);
        food_distrib.setPaintLabels(true);
        food_regrowth.setPaintLabels(true);
        populationSlider.setPaintLabels(true);


        fps.setBackground(Color.white);
        food_distrib.setBackground(Color.white);
        food_regrowth.setBackground(Color.white);
        populationSlider.setBackground(Color.white);


        fps.setPaintTicks(true);
        food_distrib.setPaintTicks(true);
        food_regrowth.setPaintTicks(true);
        populationSlider.setPaintTicks(true);


        fps.setMajorTickSpacing(100);
        food_distrib.setMajorTickSpacing(20);
        food_regrowth.setMajorTickSpacing(10);
        populationSlider.setMajorTickSpacing(50);

        fps.setMinorTickSpacing(20);
        food_distrib.setMinorTickSpacing(5);
        food_regrowth.setMinorTickSpacing(2);
        populationSlider.setMinorTickSpacing(10);
        fps.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                FPS = fps.getValue() + 1;
                requestFocus();
            }
        });
        food_distrib.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                FOOD_DISTRIBUTION = food_distrib.getValue() + 1;
                requestFocus();
            }
        });
        food_regrowth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                FOOD_REGROWTH = food_regrowth.getValue() + 1;
                requestFocus();
            }
        });
        populationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MIN_POP_SIZE = populationSlider.getValue();
                requestFocus();
            }
        });

        ret.add(fps);
        ret.add(food_distrib);
        ret.add(food_regrowth);
        ret.add(populationSlider);

        MaterialButton save = new MaterialButton();
        save.setText("SAVE");
        MaterialButton load = new MaterialButton();
        load.setText("LOAD");
        MaterialButton generate = new MaterialButton();
        generate.setText("GENERATE A NEW WORLD");

        save.setType(MaterialButton.Type.RAISED);
        load.setType(MaterialButton.Type.RAISED);
        generate.setType(MaterialButton.Type.RAISED);

        save.setBounds(10, 10 + (getHeight() / 12) * 10, 180, 70);
        load.setBounds(200, 10 + (getHeight() / 12) * 10, 180, 70);

        generate.setBounds(20, 10 + (getHeight() / 12) * 5, controlPanelWidth - 40, getHeight() / 16);


        ret.add(save);
        ret.add(load);
        ret.add(generate);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                load();
            }
        });
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateNewDialog();
            }
        });
        return ret;
    }

    /**
     * creates a dialog for the new world generation
     */
    private void generateNewDialog() {


        JPanel dialog = new JPanel();
        dialog.setLayout(null);
        dialog.addMouseListener(listener);
        dialog.addMouseMotionListener(listener);

        JPanel top = new JPanel();
        top.setBackground(Color.ORANGE);
        top.setLayout(null);
        top.setBounds(0, 0, 480, 60);

        dialog.setBounds(getWidth() / 2 - 240 - 200, getHeight() / 2 - 360, 480, 720);
        dialog.setBackground(Color.WHITE);


        JLabel exit = new JLabel("X");
        exit.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        exit.setBounds(430, 0, 60, 60);
        exit.setHorizontalTextPosition(SwingConstants.CENTER);
        exit.setVerticalTextPosition(SwingConstants.TOP);
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                container.remove(dialog);
            }
        });


        JSlider w_width = new JSlider(JSlider.HORIZONTAL, 0, 400, WORLD_WIDTH);
        JSlider w_height = new JSlider(JSlider.HORIZONTAL, 0, 400, WORLD_HEIGHT);
        JSlider land_amount = new JSlider(JSlider.HORIZONTAL, 0, 100, LAND_AMOUNT);
        JSlider land_size = new JSlider(JSlider.HORIZONTAL, 0, 100, LAND_SIZE);
        JSlider smoothing = new JSlider(JSlider.HORIZONTAL, 0, 10, SMOOTHING_FAKTOR);

        int xOff = 190;

        w_width.setBounds(xOff, 10 + 70, 480 - xOff - 20, 50);
        w_height.setBounds(xOff, 10 + 60 + 70, 480 - xOff - 20, 50);
        land_amount.setBounds(xOff, 10 + 60 * 2 + 70, 480 - xOff - 20, 50);
        land_size.setBounds(xOff, 10 + 60 * 3 + 70, 480 - xOff - 20, 50);
        smoothing.setBounds(xOff, 10 + 60 * 4 + 70, 480 - xOff - 20, 50);


        w_width.setPaintLabels(true);
        w_height.setPaintLabels(true);
        land_amount.setPaintLabels(true);
        land_size.setPaintLabels(true);
        smoothing.setPaintLabels(true);

        w_width.setBackground(Color.white);
        w_height.setBackground(Color.white);
        land_amount.setBackground(Color.white);
        land_size.setBackground(Color.white);
        smoothing.setBackground(Color.white);

        w_width.setPaintTicks(true);
        w_height.setPaintTicks(true);
        land_amount.setPaintTicks(true);
        land_size.setPaintTicks(true);
        smoothing.setPaintTicks(true);

        w_width.setMajorTickSpacing(100);
        w_height.setMajorTickSpacing(100);
        land_amount.setMajorTickSpacing(25);
        land_size.setMajorTickSpacing(25);
        smoothing.setMajorTickSpacing(5);

        w_width.setMinorTickSpacing(20);
        w_height.setMinorTickSpacing(20);
        land_amount.setMinorTickSpacing(5);
        land_size.setMinorTickSpacing(5);
        smoothing.setMinorTickSpacing(1);

        dialog.add(w_width);
        dialog.add(w_height);
        dialog.add(land_amount);
        dialog.add(land_size);
        dialog.add(smoothing);

        JLabel[] labels = new JLabel[5];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setBounds(22, 60 * i + 70, 150, 50);
            labels[i].setFont(new Font("Times New Roman", 0, 20));
            dialog.add(labels[i]);
        }
        labels[0].setText("World Width");
        labels[1].setText("World Height");
        labels[2].setText("Land Amount");
        labels[3].setText("Land Size");
        labels[4].setText("Smoothing");


        container.add(dialog, 0);
        dialog.add(top);
        top.add(exit);

        MaterialButton generate = new MaterialButton();
        generate.setText("GENERATE");
        generate.setBounds(20, 60 * 5 + 90, 440, 80);
        generate.setBackground(Color.ORANGE);

        dialog.add(generate);

        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WORLD_WIDTH = w_width.getValue();
                WORLD_HEIGHT = w_height.getValue();
                LAND_AMOUNT = land_amount.getValue();
                LAND_SIZE = land_size.getValue();
                SMOOTHING_FAKTOR = smoothing.getValue();

                if (population != null) {
                    for (int i = 0; i < population.size(); i++) {
                        Bot b = population.get(i);
                        b.kill();
                        population.remove(b);
                    }
                    population.clear();
                }
                map = null;

                containerPanel.removeAll();
                containerPanel.update(containerPanel.getGraphics());

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                CHUNK_SIZE = (int) ((screenSize.getWidth() - controlPanelWidth) / WORLD_WIDTH);
                if (CHUNK_SIZE < 8)
                    CHUNK_SIZE = 8;
                createMap();
                resizeMap();
                currenDateiname = null;
                container.remove(dialog);

            }

        });


    }

    private void dynamicResizer() {
        new Thread(() -> {

            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                resizeCounter--;
                if (resizeCounter == 0) {
                    resizeCounter = 0;
                    resizeMap();
                }
                if (resizeCounter == Integer.MIN_VALUE)
                    resizeCounter = -1;
            }
        }).start();
    }

    /**
     * resizes the World to a given chunk size
     */
    private void resizeMap() {
        if (mapLoaded) {
            double prevWidth = containerPanel.getWidth();
            double prevHeight = containerPanel.getHeight();
            containerPanel.setSize(WORLD_WIDTH * CHUNK_SIZE, WORLD_HEIGHT * CHUNK_SIZE);
            prevWidth -= containerPanel.getWidth();
            prevHeight -= containerPanel.getHeight();
            containerPanel.setLocation(containerPanel.getX() + (int) (prevWidth / 2), containerPanel.getY() + (int) (prevHeight / 2));
            for (int i = 0; i < WORLD_WIDTH; i++) {
                for (int j = 0; j < WORLD_HEIGHT; j++) {
                    Chunk c = map[i][j];
                    c.resizeAndReposition();
                }
            }

            if (population != null)
                for (int i = 0; i < population.size(); i++) {
                    Bot b = population.get(i);
                    b.resizeAndRelocate();
                }
        }
    }

    /**
     * Method that creates a new Map
     */
    private void createMap() {
        mapLoaded = false;
        population.clear();
        map = new Chunk[WORLD_WIDTH][WORLD_HEIGHT];
        generateMap();

        //Uniformize map
        if (SMOOTHING_FAKTOR == 1) {
            uniformieze();
        } else {
            for (int i = 0; i < SMOOTHING_FAKTOR - 1; i++) {
                uniformieze();
                containerPanel.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < WORLD_WIDTH; i++) {
            for (int j = 0; j < WORLD_HEIGHT; j++) {
                Chunk c = map[i][j];
                c.toUpdate = true;
                c.addMouseListener(listener);
                c.addMouseMotionListener(listener);
            }
        }
        mapLoaded = true;
    }

    @Override
    public void paint(Graphics g) {
        //add Bots if necessary
        if (mapLoaded && population.size() < MIN_POP_SIZE) {
            Bot b = null;
           /* int neu = (int) (Math.random() * 4);
            int hiddAmm = (int) (Math.random() * 2);
            int[] hidd = new int[hiddAmm];
            for (int k = 0; k < hiddAmm; k++) {
                hidd[k] = (int) (Math.random() * 10 + 1);
            }*/
            /**
             * 2 in 100
             */

            if (Bot.useMemorie) {
                b = new Bot(new CosiAgent(new Network(14, 4, 2, new int[]{14, 8})), this, 0);
            } else {
                b = new Bot(new CosiAgent(new Network(5, 4, 1, new int[]{8})), this, 0);
            }

            population.add(b);
            containerPanel.add(b, 0);
            //random Location
            int ranX = (int) (Math.random() * CHUNK_SIZE * WORLD_WIDTH);
            int ranY = (int) (Math.random() * CHUNK_SIZE * WORLD_HEIGHT);
            b.setLocation(ranX, ranY);
            while (!(b.getChunkUnder(ranX, ranY, b.body).getType() == Chunk.Type.LAND)) {
                ranX = (int) (Math.random() * CHUNK_SIZE * WORLD_WIDTH);
                ranY = (int) (Math.random() * CHUNK_SIZE * WORLD_HEIGHT);
                b.setLocation(ranX, ranY);
            }
            final Bot bf = b;
            bf.addMouseListener(listener);
        }

        //refresh bot panel
        if (performance == Optimisation.MEDIUM || performance == Optimisation.MIN) {
            if (selectedBot != null) {
                if (selectedBot.hp > 0) {
                    controlPanel.removeAll();
                    container.remove(controlPanel);
                    controlPanel = new JPanel();
                    controlPanel = addBotStats(selectedBot);
                    container.add(controlPanel, 0);
                } else {
                    selectedBot = null;
                    controlPanel.removeAll();
                    container.remove(controlPanel);
                    controlPanel = new JPanel();
                    controlPanel = addControls();
                    container.add(controlPanel, 0);
                }
            }
        }
        //FPS control
        timeUntilSleep = System.currentTimeMillis();
        if (fpsCounter > FPS * CHUNK_REFRESH_TIME + FOOD_REGROWTH)
            fpsCounter = 0;
        else
            fpsCounter++;

        //moving things
        if (mapLoaded && !pause) {
            Component[] components = containerPanel.getComponents();
            if (components != null && components.length > 0) {
                for (Component c : components) {

                    if (c instanceof Chunk) {

                        if (fpsCounter == FPS * CHUNK_REFRESH_TIME)
                            ((Chunk) c).update();

                        if (fpsCounter % FOOD_REGROWTH == 0)
                            ((Chunk) c).updateFood();

                        if (performance == Optimisation.MAX)
                            CHUNK_REFRESH_TIME = 3;
                        else if (performance == Optimisation.MEDIUM)
                            CHUNK_REFRESH_TIME = 1;
                        else
                            CHUNK_REFRESH_TIME = 0;
                    }
                    if (c instanceof Bot) {
                        Bot b = (Bot) c;
                        b.move();
                    }
                }
            }
        }
        super.paint(g);
        //sleep to match FPS
        long passedTime = System.currentTimeMillis() - timeUntilSleep;
        if (passedTime < 1000.0 / FPS) {
            try {
                Thread.sleep((long) (1000.0 / FPS - passedTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        repaint();
    }


    /**
     * method used to generate the Map
     */
    private void generateMap() {
        ArrayList<Island> islands = new ArrayList<>();
        //first fill with Water


        progressBar = new JProgressBar();
        progressBar.setBounds(getWidth() / 2 - 80 - controlPanelWidth / 2, getHeight() / 2 - 30, 160, 60);
        progressBar.setMaximum(WORLD_WIDTH);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        CHUNK_SIZE = (int) ((screenSize.getWidth() - controlPanelWidth) / WORLD_WIDTH);
        containerPanel.setBounds(0, 0, WORLD_WIDTH * CHUNK_SIZE, WORLD_HEIGHT * CHUNK_SIZE);

        containerPanel.removeAll();
        containerPanel.repaint();
        containerPanel.add(progressBar);
        containerPanel.update(containerPanel.getGraphics());

        for (int i = 0; i < WORLD_WIDTH; i++) {
            for (int j = 0; j < WORLD_HEIGHT; j++) {
                try {
                    Chunk c = new Chunk(i, j);
                    c.setLocation(i * c.getWidth(), j * c.getHeight());
                    containerPanel.add(c);
                    map[i][j] = c;

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (LAND_AMOUNT > (Math.random() * 1000)) {
                    islands.add(new Island(LAND_SIZE, i, j));
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i % (WIDTH / 50) == 0) {
                progressBar.setValue((int) ((i + 0.0) / WORLD_WIDTH * 100));
                progressBar.update(progressBar.getGraphics());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Island island : islands) {
            for (int i = 0; i < island.island.length; i++) {
                for (int j = 0; j < island.island[i].length; j++) {
                    if (island.island[i][j] == 1) {
                        int x = i + island.x;
                        int y = j + island.y;
                        if (x < WORLD_WIDTH && y < WORLD_HEIGHT && x >= 0 && y >= 0) {
                            Chunk c = map[x][y];
                            c.setType(Chunk.Type.LAND);
                        }
                    }
                }
            }
        }
        containerPanel.remove(progressBar);
        repaint();
    }

    /**
     * make uniform
     * if Water chunk has more Land arround than Water, transform to land.
     */
    public void uniformieze() {
        for (int i = 0; i < WORLD_WIDTH; i++) {
            for (int j = 0; j < WORLD_HEIGHT; j++) {
                //Count water vs land Neighbors
                Chunk c = map[i][j];
                int waterCount = 0;
                int landCount = 0;
                for (Chunk n : c.getNeighbors()) {
                    if (n.getType() == Chunk.Type.LAND)
                        landCount++;
                    else
                        waterCount++;
                }
                //valuate the  water vs land count
                if (c.getType() == Chunk.Type.LAND) {
                    if (SMOOTHING_FAKTOR != 1 ? landCount < waterCount : landCount == 0) {
                        c.setType(Chunk.Type.WATER);
                    }
                } else {
                    if (SMOOTHING_FAKTOR != 1 ? landCount > waterCount : waterCount == 0) {
                        c.setType(Chunk.Type.LAND);
                    }
                }
                c.update();
            }
        }
    }


    /**
     * Load a previous World
     */
    public void load() {

        File file = new File("C:\\EvoBots");
        // Reading directory contents
        File[] files = file.listFiles();
        String worlds[] = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            worlds[i] = files[i].getName();
        }

        String selectedWorld = "";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(file);

        int n = fileChooser.showOpenDialog(World.this);
        if (n == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            selectedWorld = f.getName();


            containerPanel.removeAll();

            String path = "C:\\EvoBots\\" + selectedWorld;

            currenDateiname = selectedWorld.substring(0, selectedWorld.indexOf('.'));

            String data = "";

            BufferedReader br = null;
            try {
                br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(path)));
                String line;
                while ((line = br.readLine()) != null) {
                    data += line;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int indexOfSemiColum = data.indexOf(';');

            int[] setupData = new int[12];

            for (int i = 0; i < setupData.length; i++) {
                String subData = data.substring(0, indexOfSemiColum);
                setupData[i] = Integer.parseInt(subData);
                data = data.substring(indexOfSemiColum + 1);
                indexOfSemiColum = data.indexOf(';');
            }

            containerPanel.setLocation(setupData[0], setupData[1]);

            WORLD_WIDTH = setupData[2];
            WORLD_HEIGHT = setupData[3];
            LAND_AMOUNT = setupData[4];
            LAND_SIZE = setupData[5];
            CHUNK_SIZE = setupData[6];
            FOOD_DISTRIBUTION = setupData[7];
            FOOD_REGROWTH = setupData[8];
            SMOOTHING_FAKTOR = setupData[9];
            FPS = setupData[10];
            MIN_POP_SIZE = setupData[11];




       /* TODO performance
        subData = data.substring(0, indexOfSemiColum);
        performance
                indexOfSemiColum = data.indexOf(';');
 */
            data = data.substring(indexOfSemiColum + 1);
            indexOfSemiColum = data.indexOf(';');

            String subData;

            map = new Chunk[WORLD_WIDTH][WORLD_HEIGHT];

            for (int i = 0; i < WORLD_WIDTH; i++) {
                for (int j = 0; j < WORLD_HEIGHT; j++) {
                    Chunk c = null;
                    try {
                        c = new Chunk(i, j);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    subData = data.substring(0, indexOfSemiColum);
                    int food = Integer.parseInt(subData);
                    data = data.substring(indexOfSemiColum + 1);
                    indexOfSemiColum = data.indexOf(';');


                    subData = data.substring(0, indexOfSemiColum);
                    data = data.substring(indexOfSemiColum + 1);
                    indexOfSemiColum = data.indexOf(';');
                    if (subData.equals("LAND"))
                        c.setType(Chunk.Type.LAND);
                    c.setFood(food);

                    c.setLocation(i * CHUNK_SIZE, j * CHUNK_SIZE);
                    map[i][j] = c;
                    containerPanel.add(c);
                }
            }
            subData = data.substring(0, indexOfSemiColum);
            int botCount = Integer.parseInt(subData);
            data = data.substring(indexOfSemiColum + 1);
            indexOfSemiColum = data.indexOf(';');
            //  System.out.println("bCount" + botCount);


            if (population != null) {
                for (int i = 0; i < population.size(); i++) {
                    Bot b = population.get(i);
                    b.kill();
                }
                population.clear();
            }

            for (int j = 0; j < botCount; j++) {
                Bot b = new Bot(null, null, 0);
                indexOfSemiColum = data.indexOf(';');

                int dat[] = new int[15];
                for (int k = 0; k < 15; k++) {
                    subData = data.substring(0, indexOfSemiColum);
                    dat[k] = Integer.parseInt(subData);
                    data = data.substring(indexOfSemiColum + 1);
                    indexOfSemiColum = data.indexOf(';');
                }
                int x, y;

                x = dat[0];
                y = dat[1];
                b.setLocation(x, y);
                b.age = dat[2];
                b.ageingCounter = dat[3];
                b.sensorRotation = dat[4];

                b.xDir = dat[5];
                b.yDir = dat[6];
                b.hp = dat[7];
                b.red = dat[8];
                b.blue = dat[9];

                b.green = dat[10];
                b.makeChildren = dat[11];
                b.generation = dat[12];
                b.memRefresh = dat[13];
                b.memRefreshCounter = dat[14];

                b.world = this;
                //memorie
                for (int k = 0; k < b.memorie.size(); k++) {
                    for (int l = 0; l < b.memorie.get(k).length; l++) {
                        subData = data.substring(0, indexOfSemiColum);
                        b.memorie.get(k)[l] = Double.parseDouble(subData);
                        data = data.substring(indexOfSemiColum + 1);
                        indexOfSemiColum = data.indexOf(';');
                    }
                }

                //agent
                data = data.replace('[', '{');
                data = data.replace(']', '}');

                //TODO in data bleiben 3 überflüssige werte übrig kp was läuft

                subData = data.substring(0, data.indexOf('}') + 1);
                b.agent = new CosiAgent(new Network(subData));

                b.addMouseListener(listener);

                data = data.substring(data.indexOf('}') + 1);

                population.add(b);
                containerPanel.add(b, 0);
            }
            repaint();
            resizeMap();
            containerPanel.setLocation(setupData[0], setupData[1]);
            //refresh panel
            selectedBot = null;
            controlPanel.removeAll();
            container.remove(controlPanel);
            controlPanel = new JPanel();
            controlPanel = addControls();
            container.add(controlPanel, 0);
            requestFocus();
        }
    }

    /**
     * Save the current state of the World
     */
    public void save() {

        File theDir = new File("C:\\EvoBots");
        if (!theDir.exists()) {
            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                se.printStackTrace();
                System.out.println("ERROR SAVE");
            }

        }


        String data = "" + containerPanel.getX() + ";" + containerPanel.getY() + ";" + WORLD_WIDTH + ";" + WORLD_HEIGHT + ";" + LAND_AMOUNT + ";" + LAND_SIZE + ";" + CHUNK_SIZE + ";" + FOOD_DISTRIBUTION
                + ";" + FOOD_REGROWTH + ";" + SMOOTHING_FAKTOR + ";" + FPS + ";" + MIN_POP_SIZE + ";" + performance + ";";
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                Chunk c = map[i][j];
                data += "" + c.getFood() + ";" + c.getType() + ";";
            }
        }
        data += population.size() + ";";
        for (int i = 0; i < population.size(); i++) {
            Bot b = population.get(i);
            data += "" + b.getX() + ";" + b.getY() + ";" + b.age + ";" + b.ageingCounter + ";"
                    + b.sensorRotation + ";" + b.xDir + ";" + b.yDir + ";" + b.hp + ";"
                    + b.red + ";" + b.blue + ";" + b.green + ";" + b.makeChildren + ";"
                    + b.generation + ";" + b.memRefresh + ";" + b.memRefreshCounter + ";";
            for (int j = 0; j < b.memorie.size(); j++) {
                for (int k = 0; k < b.memorie.get(j).length; k++) {
                    data += b.memorie.get(j)[k] + ";";
                }
            }
            data += "" + Arrays.toString(b.agent.getNet().getDescriptor()) + "\n";
        }

        String path;
        if (currenDateiname != null) {
            path = JOptionPane.showInputDialog("Dateiname Angeben", currenDateiname);
        } else {
            path = JOptionPane.showInputDialog("Dateiname Angeben");

        }
        if (path != null && path.length() > 0) {
            currenDateiname = path;
            try {
                PrintWriter writer = new PrintWriter("c:\\EvoBots\\" + path + ".txt", "UTF-8");
                writer.println(data);
                writer.close();
            } catch (IOException e) {
                System.out.println("ERROR SAVE 2");
            }
        }
        requestFocus();
    }


    /**
     * start the World generation and in future also the simulation
     *
     * @param args
     */
    public static void main(String[] args) {
        new World();
    }

}
