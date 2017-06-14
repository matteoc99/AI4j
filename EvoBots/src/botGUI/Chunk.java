package botGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
     * Fertility can go from 0 to 100.
     * It describes the speed of the Food regrowth
     */
    public int fertility = 70;

    /**
     * Describes the Type of this Chunk
     */
    Type type = Type.WATER;

    /**
     * describes the position of this Object in the {@link World#map}
     */
    int i,j;

    /**
     * Construktor for the {@link ImmobileObject}
     */
    public Chunk(int i,int j) throws ClassNotFoundException {
        super(WATER);
        setType(Type.WATER);
        this.i=i;
        this.j=j;
    }

    /**
     * Construktor for the {@link ImmobileObject}
     */
    public Chunk(Type type,int i,int j) throws ClassNotFoundException {
        super(type == Type.LAND ? LAND : WATER);
        setType(type);
        this.i=i;
        this.j=j;
    }

    public void setType(Type type) {
        try {
            if (type != null)
                this.type = type;
            if (type == Type.LAND) {
                newimg(LAND);
                fertility=70;
            } else {
                fertility = 95;
                newimg(WATER);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * updates the image in relation to the {@link #fertility}
     */
    public void update() {
        Color color = null;
        if (type == Type.LAND) {
            color = new Color(97, 55, 25);
            try {
                newimg(LAND);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            color = new Color(0, 103, 235);
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
                if (ran > fertility) {
                    img.setRGB(i, j, color.getRGB());
                }
            }
        }
        newimg(img);
    }

}
