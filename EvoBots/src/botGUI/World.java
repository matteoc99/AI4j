package botGUI;


import javax.swing.*;
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
    public static final int WORLD_WIDTH = 100;
    public static final int WORLD_HEIGHT = 50;
    /**
     * from 1-1000 describes the amount of land in the World.
     * 1000 is the Maximum
     */
    public static final int LAND_AMOUNT = 15;
    /**
     * The bigger the value the smaller the islands are
     */
    public static final int LAND_SIZE = WORLD_HEIGHT / 8;
    /**
     * Describes the size of the World
     */
    public static int CHUNK_SIZE = 20;

    public static final int MAX_CHUNK_SIZE = 256;
    /**
     * Describes the distribution of the food
     */
    public static final int FOOD_DISTRIBUTION = 15;


    /**
     * Describes the speed of the Food regrowth
     */
    private static final int FOOD_REGROWTH= 10;

    /**
     * describes how often Chunks are repainted 0-10
     * 0 very often
     * 1 slow
     */
    private static final int CHUNK_REFRESH_TIME =0 ;

    /**
     * Describes how smooth the Islands are
     * 0: smoothing off
     * 1: soft smoothing
     * 1<: strong smoothing
     */
    public static final int SMOOTHING_FAKTOR = 2;

    /**
     * Describes the speed of the moving map
     */
    private static final int MOVE_SPEED = 20;




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
     * used to resize all at once
     */
    public int resizeCounter=0;



    public World() {
        setTitle("World");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        CHUNK_SIZE= (int) (screenSize.getWidth()/WORLD_WIDTH);
        setBounds(0, 0, screenSize.width, screenSize.height);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);

        container = getContentPane();
        container.setLayout(null);
        container.setBackground(Color.gray);
        containerPanel.setBounds(0, 0, WORLD_WIDTH * CHUNK_SIZE, WORLD_HEIGHT * CHUNK_SIZE);
        containerPanel.setLayout(null);
        container.add(containerPanel);
        setVisible(true);


        createMap();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_R:
                        dispose();
                        new World();
                        break;
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        containerPanel.setLocation(containerPanel.getX(), containerPanel.getY() - MOVE_SPEED);
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        containerPanel.setLocation(containerPanel.getX(), containerPanel.getY() + MOVE_SPEED);
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        containerPanel.setLocation(containerPanel.getX() - MOVE_SPEED, containerPanel.getY());
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        containerPanel.setLocation(containerPanel.getX() + MOVE_SPEED, containerPanel.getY());
                        break;

                    case KeyEvent.VK_CONTROL:
                        Point p = MouseInfo.getPointerInfo().getLocation();
                        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        int xOff= (int) (p.x-screenSize.getWidth()/2);
                        int yOff= (int) (p.y-screenSize.getHeight()/2);
                        containerPanel.setLocation(containerPanel.getX()+xOff/8,containerPanel.getY()+yOff/8);
                        break;
                    case KeyEvent.VK_P:
                        containerPanel.setLocation(0,0);
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
                        if(resizeCounter<=0)
                            resizeCounter=1;
                        CHUNK_SIZE -= e.getWheelRotation()*resizeCounter;
                        resizeCounter=5;
                        if(CHUNK_SIZE<=0)
                            CHUNK_SIZE=1;
                    }
                } else {
                    if (CHUNK_SIZE < MAX_CHUNK_SIZE) {
                        if(resizeCounter<=0)
                            resizeCounter=1;
                        CHUNK_SIZE -= e.getWheelRotation()*resizeCounter;

                        resizeCounter=5;
                    }
                }
            }

        });

    }

    private void dynamicResizer() {
        new Thread(() -> {

            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
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
        if(mapLoaded) {
            int prevWidth=containerPanel.getWidth();
            int prevHeight=containerPanel.getHeight();
            containerPanel.setSize(WORLD_WIDTH * CHUNK_SIZE, WORLD_HEIGHT * CHUNK_SIZE);
            prevWidth-=containerPanel.getWidth();
            prevHeight-=containerPanel.getHeight();
            containerPanel.setLocation(containerPanel.getX()+prevWidth/2,containerPanel.getY()+prevHeight/2);
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
        mapLoaded=false;

        map = new Chunk[WORLD_WIDTH][WORLD_HEIGHT];
        generateMap();

        for (int i = 0; i < WORLD_WIDTH; i++) {
            for (int j = 0; j < WORLD_HEIGHT; j++) {
                final Chunk c = map[i][j];
                c.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (c.type == Chunk.Type.LAND)
                            c.setFood(0);
                    }
                });
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
        timeUntilSleep = System.currentTimeMillis();
         if (fpsCounter > FPS*CHUNK_REFRESH_TIME+FOOD_REGROWTH)
             fpsCounter = 0;
         else
            fpsCounter++;
        if (mapLoaded) {
            Component[] components = containerPanel.getComponents();
            if (components != null && components.length > 0) {
                for (Component c : components) {
                    if (c instanceof Chunk)
                        if (fpsCounter == FPS*CHUNK_REFRESH_TIME)
                        ((Chunk) c).update();
                    else if (fpsCounter%FOOD_REGROWTH==0)
                            ((Chunk) c).updateFood();
                }
            }
        }
        super.paint(g);
        long passedTime = System.currentTimeMillis() - timeUntilSleep;
        if (passedTime < 1000.0 / FPS) {
            try {
                Thread.sleep((long) (1000.0 / FPS - passedTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        containerPanel.repaint();
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
                    islands.add(new Island(WORLD_WIDTH < WORLD_HEIGHT ? WORLD_WIDTH : WORLD_HEIGHT / LAND_SIZE, i, j));
                }
            }
        }
        for (Island island : islands) {
            for (int i = 0; i < island.island.length; i++) {
                for (int j = 0; j < island.island[i].length; j++) {
                    if (island.island[i][j] == 1) {
                        int x = i + island.x;
                        int y = j + island.y;
                        if (x < WORLD_WIDTH && y < WORLD_HEIGHT && x > 0 && y > 0) {
                            Chunk c = map[x][y];
                            c.setType(Chunk.Type.LAND);
                        }
                    }
                }
            }
        }
        repaint();
        containerPanel.repaint();
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

}
