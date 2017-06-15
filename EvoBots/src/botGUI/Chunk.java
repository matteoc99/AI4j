package botGUI;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
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


    public Color land = new Color(97, 55, 25);
    public Color water = new Color(55, 140, 235);


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
    public boolean toUpdate = true;

    /**
     * Construktor for the {@link ImmobileObject}
     */
    public Chunk(int i, int j) throws ClassNotFoundException {
        this(Type.WATER, i, j);
    }

    /**
     * Construktor for the {@link ImmobileObject}
     */
    public Chunk(Type type, int i, int j) throws ClassNotFoundException {
        setType(type);
        this.i = i;
        this.j = j;
        setSize(World.CHUNK_SIZE, World.CHUNK_SIZE);
    }

    /**
     * sets the Type of a Chunk and automatically changes the Image
     *
     * @param type
     */
    public void setType(Type type) {
        if (type != null)
            this.type = type;
        if (type == Type.LAND)
            food = 5;
        repaint();
    }

    /**
     * returns the Type of this Chunk
     *
     * @return the {@link #type} of this Chunk
     */
    public Type getType() {
        return type;
    }

    /**
     * Setter for the food amount in this {@link Chunk}
     *
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
     * updates only the food without the Grafics
     */
    public void updateFood() {
        if (getNewFood() - World.FOOD_DISTRIBUTION > food || toUpdate) {
            int goal = getNewFood() - World.FOOD_DISTRIBUTION;
            if (type == Type.LAND) {
                food += Math.log10(goal - food) > 1 ? Math.log10(goal - food) : 1;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        if (type == Type.LAND) {
            g.setColor(land);
        } else {
            g.setColor(water);
        }
        g.fillRect(0, 0, getWidth(), getHeight());

    }

    /**
     * updates the image in relation to the {@link #food}
     */
    public void update() {
        if (getNewFood() - World.FOOD_DISTRIBUTION > food || toUpdate) {
            toUpdate = false;
            updateFood();
            updateColor();
        }
    }

    /**
     * updates the color in reference to th fertility
     */
    private void updateColor() {
        if (type == Type.LAND) {
            land = new Color(97 - food / 2 + food / 4, 55 + food * 2 - food / 2, 26 - food / 4 + food / 8);
        }
    }

    /**
     * Calculates a new Fertility for this Object
     *
     * @return a new Ferility
     */
    public int getNewFood() {
        int ret = 0;
        for (Chunk c : getNeighbors()) {
            if (ret < c.food) {
                ret = c.food;
            }
            if (c.type == Type.WATER) {
                ret = 100;
                break;
            }
        }
        return ret;
    }

    /**
     * Returns an {@link ArrayList} with all the Neighbors
     * +
     * +#+
     * +
     * #...the Chunk
     * + the Neighbors
     *
     * @return
     */
    public ArrayList<Chunk> getNeighbors() {
        ArrayList<Chunk> ret = new ArrayList<>();
        try {
            if (i < World.WORLD_WIDTH - 1)
                ret.add(World.map[i + 1][j]);
            if (j < World.WORLD_HEIGHT - 1)
                ret.add(World.map[i][j + 1]);
            if (i > 0)
                ret.add(World.map[i - 1][j]);
            if (j > 0)
                ret.add(World.map[i][j - 1]);
        } catch (Exception e) {
        }
        return ret;
    }

    /**
     * resizes, repaintes and repositions a Chunk
     */
    public void resizeAndReposition() {
        this.setSize(World.CHUNK_SIZE, World.CHUNK_SIZE);
        this.setLocation(i * World.CHUNK_SIZE, j * World.CHUNK_SIZE);
    }

}
