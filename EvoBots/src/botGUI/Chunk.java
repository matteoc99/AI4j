package botGUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Matteo Cosi
 * @since 13.06.2017
 */
public class Chunk extends ImmobileObject {

    public static final String LAND = "res\\gras.png";
    public static final String WATER = "res\\water.png";


    public enum Type {
        LAND, WATER,
    }

    /**
     * Food can go from 0 to 100.
     */
    public int food = 2;

    /**
     * Describes the Type of this Chunk
     */
    Type type = Type.WATER;

    /**
     * describes the position of this Object in the {@link World#map}
     */
    int i, j;

    /**
     * Tells weather the chunck should be updated
     */
    public boolean toUpdate=true;

    /**
     * Construktor for the {@link ImmobileObject}
     */
    public Chunk(int i, int j) throws ClassNotFoundException {
        super(WATER);
        setType(Type.WATER);
        this.i = i;
        this.j = j;
    }

    /**
     * Construktor for the {@link ImmobileObject}
     */
    public Chunk(Type type, int i, int j) throws ClassNotFoundException {
        super(type == Type.LAND ? LAND : WATER);
        setType(type);
        this.i = i;
        this.j = j;
    }

    public void setType(Type type) {
        try {
            if (type != null)
                this.type = type;
            if (type == Type.LAND) {
                newimg(LAND);
            } else {
                newimg(WATER);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * updates the image in relation to the {@link #food}
     */
    public void update() {
        int difference=20;
        if (getNewFood() - difference > food||toUpdate) {
            toUpdate=false;
            int goal = getNewFood() - difference;
            if(type==Type.LAND) {
                if (food < goal)
                    food += 2;
            }
            Color color = null;
            if (type == Type.LAND) {
                color = new Color(97, 55, 25);
                try {
                    newimg(LAND);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                color = new Color(0, 72, 235);
                try {
                    newimg(WATER);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            BufferedImage img = toBufferedImage(image);
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    int ran = (int) (Math.random() * 100) + 1;
                    if (ran > food) {
                        img.setRGB(i, j, color.getRGB());
                    }
                }
            }
            newimg(img);
        }
    }

    /**
     * Calculates a new Fertility for this Object
     *
     * @return a new Ferility
     */
    public int getNewFood() {
        int ret=0;
        for (Chunk c:getNeighbors()){
            if(ret<c.food){
                ret=c.food;
            }
            if(c.type==Type.WATER) {
                ret = 100;
                break;
            }
        }
        return ret;
    }

    public ArrayList<Chunk> getNeighbors() {
        ArrayList<Chunk> ret = new ArrayList<>();
        if (i < World.WORLD_WIDTH - 1)
            ret.add(World.map[i + 1][j]);
        if (j < World.WORLD_HEIGHT - 1)
            ret.add(World.map[i][j + 1]);
        if (i > 0)
            ret.add(World.map[i - 1][j]);
        if (j > 0)
            ret.add(World.map[i][j - 1]);
        return ret;
    }
}
