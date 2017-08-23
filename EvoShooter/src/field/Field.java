package field;

import math.*;
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
    private final int width  = 500;
    private final int height = 500;


    /**
     * ATTENTION: the height or width, modulus the following values, must be 0!
     * ATTENTION: the width or height, divided by the following values, must be greater or equal 2*Values.unitRadius
     */
    final int horizontalSectionAmount = width  / (4*Values.unitRadius);
    final int verticalSectionAmount   = height / (4*Values.unitRadius);

    /**
     * storing all sections
     * a Section is a specified area of this Field
     */
    private FieldSection[][] sections = new FieldSection[horizontalSectionAmount][verticalSectionAmount];

    /**
     * amount of mapTicks per second
     */
    private final int tickRate = 60;

    /**
     * amount of walls initially created in this field
     */
    private final int wallAmount;

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

    public Field(int additionWallAmount, ArrayList<FunctionData> wallData) {
        // Validation of class variables
        if (width % horizontalSectionAmount != 0 || height % verticalSectionAmount != 0)
            throw new BadFieldException("Bad Width or Height");
        if (width / horizontalSectionAmount < 2 * Values.unitRadius ||
                height / verticalSectionAmount < 2 * Values.unitRadius)
            throw new BadFieldException("Sections too small");

        this.wallAmount = additionWallAmount;

        // creates Map
        createSections();
        createWalls();
        addWalls(wallData);
        createSpawns();
    }

    private void createSections() {
        // per index
        int widthPI = width/horizontalSectionAmount;
        int heightPI = height/verticalSectionAmount;

        for (int y = 0; y < verticalSectionAmount; y++)
            for (int x = 0; x < horizontalSectionAmount; x++)
                sections[x][y] = new FieldSection(this, x, y,
                        new Position(x*widthPI,       y*heightPI),
                        new Position((x+1)*widthPI, (y+1)*heightPI)
                );
    }

    private void createWalls() {
        for (int i = 0; i < wallAmount; i++) {
            walls.add(new WallFunction(this));
        }
    }

    private void addWalls(ArrayList<FunctionData> wallData) {
        if (wallData == null) return;
        for (FunctionData wallDatum : wallData) {
            WallFunction wall = new WallFunction(this, wallDatum);
            wall.register();
            walls.add(wall);
        }
    }

    private void createSpawns() {
        // generate 8 spawns in the middle of the map
        Position center = new Position(width/2, height/2);
        Spawn[] spawns = new Spawn[8];
        for (int i = 0; i < spawns.length; i++)
            spawns[i] = new Spawn(this, center.clone(), Values.unitRadius);

        // check if Position is free, possibly clear it
        clearPosition(spawns[0]);

        // calculate and set goal for each spawn
        setGoalsToSpawns(spawns);
    }

    /**
     * Clears a part of the map from all walls
     * @param circle defining position to clear
     */
    private void clearPosition(Circle circle) {
        for (WallFunction wallFunction : getWallsCollidingWith(circle)) {

            // removing walls from Sections
            for (FieldSection fieldSection : wallFunction.getTouchedSections())
                fieldSection.removeWall(wallFunction);

            // removing walls from Field
            walls.remove(wallFunction);
        }
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
     * Checks for walls, that are colliding with the given Circle
     * @param circle looking for its place in life
     * @return walls blocking the Position
     */
    public ArrayList<WallFunction> getWallsCollidingWith(Circle circle) {
        ArrayList<WallFunction> ret = new ArrayList<>();

        for (WallFunction wall : walls)
            if (circle.collides(wall) != null)
                ret.add(wall);

        return ret;
    }

    /**
     * Method returns every Position in which the given LineFunction collides
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
        double startY = f.calcY(f.getA());
        double endY = f.calcY(f.getB());

        FieldSection startSection = getSectionAt(new Position(f.getA(), startY));
        FieldSection endSection = getSectionAt(new Position(f.getB(), endY));

        ret.add(startSection);

        // Finding affected Sections between startSection and endSection
        // Horizontal
        ret.addAll(touchHorizontal(f, startSection, endSection));
        // Vertical
        ret.addAll(touchVertical(f, startSection, endSection));

        return ret;
    }

    private ArrayList<FieldSection> touchHorizontal(LineFunction f,
                                                    FieldSection startSection,
                                                    FieldSection endSection) {
        ArrayList<FieldSection> ret = new ArrayList<>();
        if (startSection.X != endSection.X) {
            for (int i = startSection.X; i < endSection.X; i++) {
                // line indicating the start of a new Section
                double rightLine = getSections()[i][0].RIGHT;

                // calculating the height at which this WallFunction enters a new Section
                double yRes = f.calcY(rightLine);

                ret.add(getSectionAt(new Position(rightLine, yRes)));
            }
        }

        return ret;
    }

    private ArrayList<FieldSection> touchVertical(LineFunction f,
                                                  FieldSection startSection,
                                                  FieldSection endSection) {
        ArrayList<FieldSection> ret = new ArrayList<>();
        if (startSection.Y != endSection.Y) {
            // order
            FieldSection smallerY_Section = (startSection.Y < endSection.Y)? startSection:endSection;
            FieldSection biggerY_Section = (startSection.Y > endSection.Y)? startSection:endSection;

            for (int i = smallerY_Section.Y; i < biggerY_Section.Y; i++) {
                // line indicating the start of a new Section
                double botLine = getSections()[0][i].BOT;
                if (f.getK() < 0)
                    botLine = Math.nextDown(Math.nextDown(botLine)); // decrement for a minimal amount

                // calculating the xPos at which this WallFunction enters a new Section
                double xRes = f.calcX(botLine);
                ret.add(getSectionAt(new Position(xRes, botLine)));
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
        int xPos = (int)pos.getX();
        int yPos = (int)pos.getY();

        if (xPos == width) xPos --;
        if (yPos == height) yPos--;

        return sections
                [xPos / (width / horizontalSectionAmount)]
                [yPos / (height / verticalSectionAmount)]
                ;
    }

    FieldSection[][] getSections() {
        return sections;
    }

    public ArrayList<WallFunction> getWalls() {
        return walls;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    /**
     * In order to safe on pp, walls are only painted once and
     * stored within a BufferedImage
     *
     * @return Image representing the map
     */
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