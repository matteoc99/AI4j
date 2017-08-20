package field;

import math.Function;
import math.LineFunction;
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

    /* Const */

    /**
     * ATTENTION: Do not increase this number over 2000 unless you have spoken to the author!
     */
    final int width = 500;
    final int height = 500;


    /**
     * ATTENTION: The following values, modulus the width or height, have to be 0!
     */
    public final int horizontalSectionAmount = 5;
    public final int verticalSectionAmount = 5;

    /**
     * amount of mapTicks per second
     */
    private final int tickRate = 60;

    /**
     * amount of walls initially created in this field
     */
    private final int wallAmount;

    /**
     * storing all sections
     * a Section is a specified area of this Field
     */
    public FieldSection[][] sections = new FieldSection[horizontalSectionAmount][verticalSectionAmount];

    /**
     * storing all walls
     */
    private ArrayList<WallFunction> walls = new ArrayList<>();

    public Field() {
        this(10, new ArrayList<>());
    }

    public Field(int additionWallAmount) {
        this(additionWallAmount, new ArrayList<>());
    }

    public Field(int additionWallAmount, ArrayList<WallFunction> walls) {
        this.wallAmount = additionWallAmount;
        this.walls.addAll(walls);

        createSections();
        createWalls();
        createSpawns();
    }

    private void createSections() {
        // per index
        int widthPI = width/horizontalSectionAmount;
        int heightPI = height/verticalSectionAmount;

        for (int y = 0; y < verticalSectionAmount; y++)
            for (int x = 0; x < horizontalSectionAmount; x++)
                sections[x][y] = new FieldSection(x, y,
                        new Position(x*widthPI,       y*heightPI),
                        new Position((x+1)*widthPI-1, (y+1)*heightPI-1)
                );
    }

    private void createWalls() {
        for (int i = 0; i < wallAmount; i++) {
            walls.add(new WallFunction(this));
        }
    }

    private void createSpawns() {
        // generate 8 spawns in the middle of the map
        Position center = new Position(width/2, height/2);
        Spawn[] spawns = new Spawn[8];
        for (int i = 0; i < spawns.length; i++)
            spawns[i] = new Spawn(this, center.clone(), Values.unitWidth);

        // check if Position is free, possibly clear it
        for (WallFunction wall : walls) {
            if (spawns[0].collides(wall) != null)
                walls.remove(wall);
        }

        // calculate and set goal for each spawn
        setGoalsToSpawns(spawns);
    }

    /**
     * sets different goals for each spawn
     */
    private void setGoalsToSpawns(Spawn[] spawns) {
        for (int i = 0; i < spawns.length; i++) {
            Position goal = spawns[i].center.clone();

            // spread spawns in different directions
            double deg = 360.0/spawns.length*i;
            double k = Function.calcSlopeByDeg(deg);

            // in order to move to the left, the distance must be negative
            double distance = (deg > 90 && deg < 270)? -calculateDistance(goal, deg) : calculateDistance(goal, deg);

            goal.translateTowards(k, distance);
            spawns[i].setGoal(goal);
        }
    }

    /**
     * Method returns the distance from pos to the edge of the map
     * in the given direction deg
     * @param pos Position to start from
     * @param deg degree to go to
     * @return distance to the edge of the map
     */
    private double calculateDistance(Position pos, double deg) {
        // TODO: 19.08.2017 calculate distance
        int less = (width < height)? width : height;
        return less/3;
    }

    /**
     * Method returns every Position in which the given LineFunciton collides
     * with a WallFunction on this Field
     * @param f to calculate with
     * @return collision Points
     */
    public ArrayList<Position> calcCollisionsWithWalls(LineFunction f) {
        ArrayList<Position> ret = new ArrayList<>();

        ArrayList<FieldSection> sectionsToCheck = getTouchedSections(f);

        // lists walls that have been checked already, to avoid repetition
        ArrayList<WallFunction> checkedWalls = new ArrayList<>();

        for (FieldSection fieldSection : sectionsToCheck) {
            fieldSection.getWalls().stream().filter(wall -> !checkedWalls.contains(wall)).forEach(wall -> {
                Position pos = wall.collides(f);
                if (pos != null) ret.add(pos);

                checkedWalls.add(wall);
            });
        }

        return ret;
    }

    /**
     * Method returns all Sections touched by the given LineFunction
     * @param f to calculate with
     * @return touched FieldSections
     */
    public ArrayList<FieldSection> getTouchedSections(LineFunction f) {
        ArrayList<FieldSection> ret = new ArrayList<>();
        int startY = (int)f.calcY(f.getA());
        int endY = (int)f.calcY(f.getB());

        FieldSection startSection = getSectionAt(new Position(f.getA(), startY));
        FieldSection endSection = getSectionAt(new Position(f.getB(), endY));

        //start and end
        ret.add(startSection);
        //ret.add(endSection);

        // Finding affected Sections between startSection and endSection

        // Horizontal
        if (startSection.X != endSection.X) {
            for (int i = startSection.X; i < endSection.X; i++) {
                // line indicating the start of a new Section
                int rightLine = getSections()[i][0].RIGHT + 1; // add 1 to get to the next Section

                // calculating the height at which this WallFunction enters a new Section
                int yRes = (int)f.calcY(rightLine);

                ret.add(getSectionAt(new Position(rightLine, yRes)));
            }
        }

        // Vertical
        if (startSection.Y != endSection.Y) {
            // order
            FieldSection smallerY_Section = (startSection.Y < endSection.Y)? startSection:endSection;
            FieldSection biggerY_Section = (startSection.Y > endSection.Y)? startSection:endSection;

            for (int i = smallerY_Section.Y; i < biggerY_Section.Y; i++) {
                // line indicating the start of a new Section
                int botLine = getSections()[0][i].BOT;
                // FIXME: 20.08.2017 !!!!!!!
                botLine += (f.getK() < 0)? 0:1; // add 1 to get to the next Section

                // calculating the width at which this WallFunction enters a new Section
                int xRes = (int)f.calcX(botLine);
                try {
                    ret.add(getSectionAt(new Position(xRes, botLine)));
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(botLine);
                    System.out.println(xRes);
                    System.out.println(f);
                    throw e;
                }
            }
        }

        return ret;
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

    FieldSection[][] getSections() {
        return sections;
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

        // sections

        g2.setColor(Color.RED.brighter());
        g2.setStroke(new BasicStroke(1));
        for (int i = 0; i <= horizontalSectionAmount; i++)
            g2.drawLine(i*width/horizontalSectionAmount, 0, i*width/horizontalSectionAmount, height);
        for (int i = 0; i <= verticalSectionAmount; i++)
            g2.drawLine(0, i*height/verticalSectionAmount, width, i*height/verticalSectionAmount);


        return bufferedImage;
    }
}