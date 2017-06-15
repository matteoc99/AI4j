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
    private int food = 2;

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
        this(Type.WATER,i,j);
    }

    /**
     * Construktor for the {@link ImmobileObject}
     */
    public Chunk(Type type, int i, int j) throws ClassNotFoundException {
        super(type == Type.LAND ? LAND : WATER);
        setType(type);
        this.i = i;
        this.j = j;
        setSize(World.CHUNK_SIZE,World.CHUNK_SIZE);
    }

    /**
     * sets the Type of a Chunk and automatically changes the Image
     * @param type
     */
    public void setType(Type type) {
        try {
            if (type != null)
                this.type = type;
            if (type == Type.LAND) {
                newimg(LAND);
                food=5;
            } else {
                newimg(WATER);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns the Type of this Chunk
     * @return the {@link #type} of this Chunk
     */
    public Type getType() {
        return type;
    }

    /**
     * Setter for the food amount in this {@link Chunk}
     * @param food {@link #food} to set
     */
    public void setFood(int food) {
        this.food = food;
    }

    /**
     * @return the amount of food in this square
     */
    public int getFood() {
        return food;
    }

    /**
     * @return if the Chunk should be updated or not
     */
    public boolean isToUpdate() {
        return toUpdate;
    }

    /**
     * updates the image in relation to the {@link #food}
     */
    public void update() {
        if (getNewFood() -  World.FOOD_DISTRIBUTION > food||toUpdate) {
            toUpdate=false;
            int goal = getNewFood() - World.FOOD_DISTRIBUTION;
            if(type==Type.LAND) {
                    food += Math.log10(goal-food)>1? Math.log10(goal-food):1;
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
        repaint();
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

    /**
     * Returns an {@link ArrayList} with all the Neighbors
     *     +
     *    +#+
     *     +
     * #...the Chunk
     * + the Neighbors
     * @return
     */
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

    /**
     * resizes, repaintes and repositions a Chunk
     */
    public void resizeAndReposition() {
        this.setSize(World.CHUNK_SIZE,World.CHUNK_SIZE);
        this.setLocation(i*World.CHUNK_SIZE,j*World.CHUNK_SIZE);
    }

}
