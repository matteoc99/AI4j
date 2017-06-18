package botGUI;


import botGUI.bot.Bot;
import com.sun.prism.PresentableState;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by mcosi on 14/06/2017.
 */
public class World extends JFrame {

    /**
     * describes the width and height of a World
     */
    public static int WORLD_WIDTH = 50;
    public static int WORLD_HEIGHT = 25;
    /**
     * from 1-1000 describes the amount of land in the World.
     * 1000 is the Maximum
     */
    public static int LAND_AMOUNT = 15;
    /**
     * The bigger the value the smaller the islands are
     */
    public static int LAND_SIZE = 3;
    /**
     * Describes the size of the World
     */
    public static int CHUNK_SIZE = 20;

    public static final int MAX_CHUNK_SIZE = 1024;
    /**
     * Describes the distribution of the food
     */
    public static int FOOD_DISTRIBUTION = 15;


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
    Container container;

    /**
     * tells if the Map has finished loading
     */
    private boolean mapLoaded = false;

    /**
     * Contains the World
     */
    JPanel containerPanel = new JPanel();
    /**
     * Contains the Controls and Settings for the World
     */
    JPanel controlPanel = new JPanel();

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
    public static int MIN_POP_SIZE = 1;

    /**
     * listener used for Drag and drop
     */
    DragUndDropListener listener = new DragUndDropListener();

   public static ArrayList<Bot> population = new ArrayList<>();

    public World() {
        setTitle("World");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        CHUNK_SIZE = (int) ((screenSize.getWidth() - controlPanelWidth) / WORLD_WIDTH);
        setBounds(0, 0, screenSize.width, screenSize.height);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);

        container = getContentPane();
        container.setLayout(null);
        container.setBackground(Color.gray);

        containerPanel.setBounds(0, 0, WORLD_WIDTH * CHUNK_SIZE, WORLD_HEIGHT * CHUNK_SIZE);
        containerPanel.setLayout(null);

        controlPanel.setBounds(screenSize.width - controlPanelWidth, 0, controlPanelWidth, getHeight());
        controlPanel.setBackground(Color.white);
        controlPanel.setLayout(null);

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
                        createMap();
                        resizeMap();
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
                    case KeyEvent.VK_P:
                        containerPanel.setLocation(0, 0);
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
                        population.get(0).rotateAndResize(10);
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
                    if (CHUNK_SIZE > 2) {
                        if (resizeCounter <= 0)
                            resizeCounter = 1;
                        CHUNK_SIZE -= e.getWheelRotation() * resizeCounter;
                        resizeCounter = 5;
                        if (CHUNK_SIZE <= 0)
                            CHUNK_SIZE = 1;
                    }
                } else {
                    if (CHUNK_SIZE < MAX_CHUNK_SIZE) {
                        if (resizeCounter <= 0)
                            resizeCounter = 1;
                        CHUNK_SIZE -= e.getWheelRotation() * resizeCounter;

                        resizeCounter = 5;
                    }
                }
            }

        });
        requestFocus();
    }

    /**
     * add all the Controll components to the control JPanel
     */
    private void addControls() {
        JSlider w_width = new JSlider(JSlider.HORIZONTAL, 0, 300, WORLD_WIDTH);
        JSlider w_height = new JSlider(JSlider.HORIZONTAL, 0, 300, WORLD_HEIGHT);
        JSlider land_amount = new JSlider(JSlider.HORIZONTAL, 0, 300, LAND_AMOUNT);
        JSlider land_size = new JSlider(JSlider.HORIZONTAL, 0, 50, LAND_SIZE);
        JSlider chunk_size = new JSlider(JSlider.HORIZONTAL, 0, 200, CHUNK_SIZE);
        JSlider food_distrib = new JSlider(JSlider.HORIZONTAL, 0, 80, FOOD_DISTRIBUTION);
        JSlider food_regrowth = new JSlider(JSlider.HORIZONTAL, 0, 100, FOOD_REGROWTH);//bis hier min +1 weil
        JSlider chunk_refresh = new JSlider(JSlider.HORIZONTAL, 0, 10, CHUNK_REFRESH_TIME);
        JSlider smoothing = new JSlider(JSlider.HORIZONTAL, 0, 10, SMOOTHING_FAKTOR);


        int xOff = 150;

        JLabel[] labels = new JLabel[9];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setBounds(20, (getHeight() / 10) * i, xOff - 10, getHeight() / 10);
            labels[i].setFont(new Font("Times New Roman", 0, 20));
            controlPanel.add(labels[i]);
        }
        labels[0].setText("World Width");
        labels[1].setText("World Height");
        labels[2].setText("Land Amount");
        labels[3].setText("Land Size");
        labels[4].setText("Chunk Size");
        labels[5].setText("Food Distrib.");
        labels[6].setText("Food Regrowth");
        labels[7].setText("Chunk Refresh");
        labels[8].setText("Smoothing");

        w_width.setBounds(xOff, 10, controlPanelWidth - xOff, getHeight() / 10);
        w_height.setBounds(xOff, 10 + (getHeight() / 10), controlPanelWidth - xOff, (getHeight() / 10));
        land_amount.setBounds(xOff, 10 + (getHeight() / 10) * 2, controlPanelWidth - xOff, getHeight() / 10);
        land_size.setBounds(xOff, 10 + (getHeight() / 10) * 3, controlPanelWidth - xOff, getHeight() / 10);
        chunk_size.setBounds(xOff, 10 + (getHeight() / 10) * 4, controlPanelWidth - xOff, getHeight() / 10);
        food_distrib.setBounds(xOff, 10 + (getHeight() / 10) * 5, controlPanelWidth - xOff, getHeight() / 10);
        food_regrowth.setBounds(xOff, 10 + (getHeight() / 10) * 6, controlPanelWidth - xOff, getHeight() / 10);
        chunk_refresh.setBounds(xOff, 10 + (getHeight() / 10) * 7, controlPanelWidth - xOff, getHeight() / 10);
        smoothing.setBounds(xOff, 10 + (getHeight() / 10) * 8, controlPanelWidth - xOff, getHeight() / 10);

        w_width.setPaintLabels(true);
        w_height.setPaintLabels(true);
        land_amount.setPaintLabels(true);
        land_size.setPaintLabels(true);
        chunk_size.setPaintLabels(true);
        food_distrib.setPaintLabels(true);
        food_regrowth.setPaintLabels(true);
        chunk_refresh.setPaintLabels(true);
        smoothing.setPaintLabels(true);

        w_width.setPaintTicks(true);
        w_height.setPaintTicks(true);
        land_amount.setPaintTicks(true);
        land_size.setPaintTicks(true);
        chunk_size.setPaintTicks(true);
        food_distrib.setPaintTicks(true);
        food_regrowth.setPaintTicks(true);
        chunk_refresh.setPaintTicks(true);
        smoothing.setPaintTicks(true);

        w_width.setMajorTickSpacing(50);
        w_height.setMajorTickSpacing(50);
        land_amount.setMajorTickSpacing(50);
        land_size.setMajorTickSpacing(10);
        chunk_size.setMajorTickSpacing(100);
        food_distrib.setMajorTickSpacing(20);
        food_regrowth.setMajorTickSpacing(10);
        chunk_refresh.setMajorTickSpacing(5);
        smoothing.setMajorTickSpacing(5);

        w_width.setMinorTickSpacing(10);
        w_height.setMinorTickSpacing(10);
        land_amount.setMinorTickSpacing(10);
        land_size.setMinorTickSpacing(2);
        chunk_size.setMinorTickSpacing(20);
        food_distrib.setMinorTickSpacing(5);
        food_regrowth.setMinorTickSpacing(2);
        chunk_refresh.setMinorTickSpacing(1);
        smoothing.setMinorTickSpacing(1);


        w_width.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                WORLD_WIDTH = w_width.getValue() + 1;
                requestFocus();
            }
        });
        w_height.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                WORLD_HEIGHT = w_height.getValue() + 1;
                requestFocus();
            }
        });
        land_amount.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                LAND_AMOUNT = land_amount.getValue() + 1;
                requestFocus();
            }
        });
        land_size.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                LAND_SIZE = land_size.getValue() + 1;
                requestFocus();
            }
        });
        chunk_size.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                CHUNK_SIZE = chunk_size.getValue() + 1;
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
        chunk_refresh.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                CHUNK_REFRESH_TIME = chunk_refresh.getValue();
                requestFocus();
            }
        });
        smoothing.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SMOOTHING_FAKTOR = smoothing.getValue();
                requestFocus();
            }
        });

        controlPanel.add(w_width);
        controlPanel.add(w_height);
        controlPanel.add(land_amount);
        controlPanel.add(land_size);
        controlPanel.add(chunk_size);
        controlPanel.add(food_distrib);
        controlPanel.add(food_regrowth);
        controlPanel.add(chunk_refresh);
        controlPanel.add(smoothing);
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
            }
        }).start();
    }

    /**
     * resizes the World to a given chunk size
     */
    private void resizeMap() {
        if (mapLoaded) {
            for(Bot c:population){
                c.kill();
            }
            population.clear();
            int prevWidth = containerPanel.getWidth();
            int prevHeight = containerPanel.getHeight();
            containerPanel.setSize(WORLD_WIDTH * CHUNK_SIZE, WORLD_HEIGHT * CHUNK_SIZE);
            prevWidth -= containerPanel.getWidth();
            prevHeight -= containerPanel.getHeight();
            containerPanel.setLocation(containerPanel.getX() + prevWidth / 2, containerPanel.getY() + prevHeight / 2);
            for (int i = 0; i < WORLD_WIDTH; i++) {
                for (int j = 0; j < WORLD_HEIGHT; j++) {
                    Chunk c = map[i][j];
                    c.resizeAndReposition();
                }
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

        for (int i = 0; i < WORLD_WIDTH; i++) {
            for (int j = 0; j < WORLD_HEIGHT; j++) {
                final Chunk c = map[i][j];
                c.addMouseListener(listener);
                c.addMouseMotionListener(listener);
            }
        }

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
            }
        }
        mapLoaded = true;
    }

    @Override
    public void paint(Graphics g) {
        //add Bots if necessary

        if (population.size() < MIN_POP_SIZE) {
            Bot b = new Bot(null);
            b.setLocation(100, 100);
            population.add(b);
            containerPanel.add(b, 0);
        }
        //FPS control
        timeUntilSleep = System.currentTimeMillis();
        if (fpsCounter > FPS * CHUNK_REFRESH_TIME + FOOD_REGROWTH)
            fpsCounter = 0;
        else
            fpsCounter++;

        //moving things
        if (mapLoaded) {
            Component[] components = containerPanel.getComponents();
            if (components != null && components.length > 0) {
                for (Component c : components) {
                    if (c instanceof Chunk)
                        if (fpsCounter == FPS * CHUNK_REFRESH_TIME)
                            ((Chunk) c).update();
                        else if (fpsCounter % FOOD_REGROWTH == 0)
                            ((Chunk) c).updateFood();
                    if (c instanceof Bot)
                        ((Bot) c).move();
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
        containerPanel.removeAll();
        ArrayList<Island> islands = new ArrayList<>();
        //first fill with Water
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
                    islands.add(new Island(WORLD_WIDTH < WORLD_HEIGHT ? WORLD_WIDTH / LAND_SIZE : WORLD_HEIGHT / LAND_SIZE, i, j));
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
     * start the World generation and in future also the simulation
     *
     * @param args
     */
    public static void main(String[] args) {
        new World();
    }

    class DragUndDropListener extends MouseAdapter {

        int x, y;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getComponent() instanceof Chunk) {
                Chunk c = (Chunk) e.getComponent();
                if (c.type == Chunk.Type.LAND)
                    c.setFood(0);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (e.getComponent() instanceof Chunk) {
                Chunk c = (Chunk) e.getComponent();
                e.translatePoint(containerPanel.getLocation().x - x, containerPanel.getLocation().y - y);
                containerPanel.setLocation(e.getX(), e.getY());
            } else {
                e.translatePoint(e.getComponent().getLocation().x - x, e.getComponent().getLocation().y - y);
                e.getComponent().setLocation(e.getX(), e.getY());
            }
        }
    }

}
