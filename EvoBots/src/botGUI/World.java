package botGUI;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by mcosi on 14/06/2017.
 */
public class World extends JFrame {

    //FPS control
    private static int FPS = 1;
    private long timeUntilSleep;

    /**
     * describes the size of a World
     */
    public static final int WORLD_SIZE = 30;

    /**
     * from 1-100 describes the amount of land in the World.
     * 100 is the Maximum
     */
    public static final int LAND_AMOUNT = 6;

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

        map = new Chunk[WORLD_SIZE][WORLD_SIZE];
        generateMap();

    }

    @Override
    public void paint(Graphics g) {
        timeUntilSleep = System.currentTimeMillis();
        if (mapLoaded) {
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
        for (int i = 0; i < WORLD_SIZE; i++) {
            for (int j = 0; j < WORLD_SIZE; j++) {
                try {
                    Chunk c = new Chunk(i,j);
                    c.setLocation(i * c.getWidth(), j * c.getHeight());
                    this.c.add(c);
                    map[i][j] = c;

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (LAND_AMOUNT > (Math.random() * 100)) {
                    islands.add(new Island(WORLD_SIZE / 8, i, j));
                }
            }
        }
        for (Island island : islands) {
            for (int i = 0; i < island.island.length; i++) {
                for (int j = 0; j < island.island[i].length; j++) {
                    if (island.island[i][j] == 1) {
                        int x = i + island.x;
                        int y = j + island.y;
                        if (x < WORLD_SIZE && y < WORLD_SIZE) {
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
