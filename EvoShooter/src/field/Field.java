package field;

import math.Position;
import values.Values;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 */
public class Field extends Container {

    /**
     * Tells if this is a valid random generated Field
     */
    public boolean valid = true;

    /** Const */

    final int width = 500;
    final int height = 500;


    /**
     * ATTENTION: The following values modulus the width or height has to be 0!
     */
    public final int horizontalSectionAmount = 10;
    public final int verticalSectionAmount = 10;

    /**
     * amount of mapTicks per second
     */
    private final int tickrate = 60;

    /**
     * amount of walls initially created in this field
     */
    private static final int wallAmount = 10;

    /**
     * storing all sections
     * a Section is a specific area of this Field
     */
    private FieldSection[][] sections = new FieldSection[horizontalSectionAmount][verticalSectionAmount];

    /**
     * storing all walls
     */
    private ArrayList<WallFunction> walls = new ArrayList<>();

    public Field() {
        createSections();
        createWalls();
        createSpawns();
    }

    private void createSections() {
        for (int y = 0; y < verticalSectionAmount; y++)
            for (int x = 0; x < horizontalSectionAmount; x++)
                sections[x][y] = new FieldSection();
    }

    private void createWalls() {
        for (int i = 0; i < wallAmount; i++) {
            walls.add(new WallFunction(this));
        }
    }

    private void createSpawns() {
        // generate 8 spawn in the middle of the map
        Position center = new Position(width/2, height/2);
        Spawn[] spawns = new Spawn[8];
        for (int i = 0; i < spawns.length; i++)
            spawns[i] = new Spawn(((Position) center.clone()), Values.unitWidth);

        // check if Position is free
        for (WallFunction wall : walls) {
            if (spawns[0].collides(wall) != null)
                walls.remove(wall);
        }

        
    }

    /**
     * Method returns the Section containing the given Position
     * @param pos to check
     * @return FieldSection at Position pos
     */
    FieldSection getSectionAt(Position pos) {
        if (pos.x == width) pos.x -=1;
        if (pos.y == height) pos.y -=1;

        int x = pos.x / (width / horizontalSectionAmount);
        int y = pos.y / (height / verticalSectionAmount);

        return sections[x][y];
    }

    BufferedImage createMapImage() {
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