package field;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 */
public class Field extends Container {

    /** Const */

    final int width = 500;
    final int height = 500;

    private final int horizontalSectionAmount = 10;
    private final int verticalSectionAmount = 10;

    /**
     * amount of mapTicks per second
     */
    private final int tickrate = 60;

    /**
     * amount of walls initially created in this field
     */
    private static final int wallAmount = 1;

    /**
     * storing all sections
     * a Section is a part of this field
     */
    private ArrayList<FieldSection> sections = new ArrayList<>();

    /**
     * storing all walls
     */
    private ArrayList<WallFunction> walls = new ArrayList<>();

    public Field() {
        for (int y = 0; y < verticalSectionAmount; y++)
            for (int x = 0; x < horizontalSectionAmount; x++)
                sections.add(new FieldSection(x,y));
        createWalls();
    }

    void createWalls() {
        for (int i = 0; i < wallAmount; i++) {
            walls.add(new WallFunction(this));
        }
    }

    BufferedImage createWallImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = ((Graphics2D) bufferedImage.getGraphics());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        //https://stackoverflow.com/a/6297069
        //clear
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2.fillRect(0,0,width,height);

        //reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        g2.setColor(Color.BLACK);
        g2.translate(0, height);
        g2.scale(1.0, -1.0);
        for (WallFunction wall : walls) {
            wall.paint(g2);
        }
        return bufferedImage;
    }
}