package botGUI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by mcosi on 14/06/2017.
 */
public class World extends JFrame {

    /**
     * describes the width and height of a World
     */
    public static final int WORLD_WIDTH = 60;
    public static final int WORLD_HEIGHT = 40;
    /**
     * from 1-100 describes the amount of land in the World.
     * 100 is the Maximum
     */
    public static final int LAND_AMOUNT = 2;
    /**
     * The bigger the value the smaller the islands are
     */
    public static final int LAND_SIZE= 4;


    public static final int CHUNK_SIZE=16;

    //FPS control
    private static int FPS = 60;
    private long timeUntilSleep;
    private int fpsCounter;
    /**
     * The Map of {@link Chunk}s
     */
    public static Chunk[][] map = null;

    /**
     * The container on the {@link JFrame}
     */
    Container c;

    private boolean mapLoaded = false;

    public World() {
        setTitle("World");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);

        c = getContentPane();
        setVisible(true);

        map = new Chunk[WORLD_WIDTH][WORLD_HEIGHT];
        generateMap();

        for (int i = 0; i < WORLD_WIDTH; i++) {
            for (int j = 0; j < WORLD_HEIGHT; j++) {
                final Chunk c = map[i][j];
                c.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (c.type == Chunk.Type.LAND)
                            c.food = 0;
                    }
                });
            }
        }

    }

    @Override
    public void paint(Graphics g) {
        timeUntilSleep = System.currentTimeMillis();
        if (fpsCounter > FPS/2)
            fpsCounter = 0;
        else
            fpsCounter++;

        if (mapLoaded && fpsCounter == FPS/2 ) {
            Component[] components = this.getContentPane().getComponents();
            if (components != null && components.length > 0) {
                for (Component c : components) {
                    if (c instanceof Chunk)
                        ((Chunk) c).update();
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

        repaint();
    }

    /**
     * method used to generate the Map
     */
    private void generateMap() {
        ArrayList<Island> islands = new ArrayList<>();
        //first fill with Water
        for (int i = 0; i < WORLD_WIDTH; i++) {
            for (int j = 0; j < WORLD_HEIGHT; j++) {
                try {
                    Chunk c = new Chunk(i, j);
                    c.setLocation(i * c.getWidth(), j * c.getHeight());
                    this.c.add(c);
                    map[i][j] = c;

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (LAND_AMOUNT > (Math.random() * 100)) {
                    islands.add(new Island(WORLD_WIDTH<WORLD_HEIGHT?WORLD_WIDTH:WORLD_HEIGHT / LAND_SIZE, i, j));
                }
            }
        }
        for (Island island : islands) {
            for (int i = 0; i < island.island.length; i++) {
                for (int j = 0; j < island.island[i].length; j++) {
                    if (island.island[i][j] == 1) {
                        int x = i + island.x;
                        int y = j + island.y;
                        if (x < WORLD_WIDTH && y < WORLD_HEIGHT&&x>0&&y>0) {
                            Chunk c = map[x][y];
                            c.setType(Chunk.Type.LAND);
                        }
                    }
                }
            }
        }
        mapLoaded = true;
        repaint();
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
