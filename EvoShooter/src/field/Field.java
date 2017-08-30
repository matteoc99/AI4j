package field;

import math.*;
import values.Values;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 */
public class Field extends Canvas {

    /* Const */

    /**
     * ATTENTION: Do not increase this number over 2000 unless you have spoken to the author!
     */
    public final int width;
    public final int height;
    // for code readability, optimization should be done by automatically anyways
    public final double diameter;


    /**
     * ATTENTION: the height or width, modulus the following values, must be 0!
     * ATTENTION: the width or height, divided by the following values, must be greater or equal 2*Values.UNIT_RADIUS
     */
    public final int horizontalSectionAmount;
    public final int verticalSectionAmount;

    /**
     * storing all sections
     * a Section is a specified area of this Field
     */
    private final FieldSection[][] sections;

    /**
     * storing all walls
     */
    private ArrayList<WallFunction> walls = new ArrayList<>();

    private ArrayList<Spawn> spawns = new ArrayList<>();

    /**
     * No usage yet
     *
     * Later represents the goal of the game.
     */
    private Flag flag;

    Field(int width,
                 int height,
                 int horizontalSectionAmount,
                 int verticalSectionAmount) {

        this.width = width;
        this.height = height;
        this.horizontalSectionAmount = horizontalSectionAmount;
        this.verticalSectionAmount = verticalSectionAmount;

        validationClassVariables();

        this.diameter = Math.sqrt(width*width+height*height);
        this.sections = new FieldSection[horizontalSectionAmount][verticalSectionAmount];

        createSections();

        this.setBounds(0, 0, width, height);
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

    public void addWalls(Collection<FunctionData> wallData) {
        for (FunctionData wallDatum : wallData) {
            WallFunction wall = new WallFunction(this, wallDatum);
            walls.add(wall);
        }
    }

    public void addSpawns(Collection<Spawn> spawns) {
        this.spawns.addAll(spawns);
    }

    public void clearPosition(FieldSection... sections) {
        // gathers all walls in another List (to avoid ConcurrentModificationException)
        List<WallFunction> affectedWalls = new ArrayList<>();
        for (FieldSection section : sections)
            affectedWalls.addAll(section.getWalls());

        // removes all walls from all Sections and this Field
        for (WallFunction affectedWall : affectedWalls) {
            for (FieldSection section : affectedWall.getTouchedSections())
                section.removeWall(affectedWall);
            walls.remove(affectedWall);
        }
    }

    /**
     * Method returns the distance from pos to the edge of the map
     * in the given direction deg
     * @param pos Position to start from
     * @param deg degree to go to
     * @return distance to the edge of the map
     */
    public double calculateDistance(Position pos, double deg) {
        deg = Function.normalizeDeg(deg);

        // Δx:Δy ratio equals Δx:factor*Δx (tangents), can be infinitive
        double horizontalFactor = Math.abs(1/Math.tan(Math.toRadians(deg)));
        // Δx:Δy ratio equals factor*Δy:Δy (cotangents), can be infinitive
        double verticalFactor   = Math.abs(Math.tan(Math.toRadians(deg)));

        // calculated towards top ([0°,180°[) or bot ([180°,360°[) edge
        double katA1 = (deg < 180)? height-pos.getX() : pos.getX();
        double katA2 = katA1*horizontalFactor;

        // calculated towards right ([0°,90°[ | [270°,360°[) or left ([90°,270°[) edge
        double katB1 = (deg < 90 || deg > 270)? width-pos.getY() : pos.getY();
        double katB2 = katB1*verticalFactor;

        // calculating the hypotenuse
        double hypA = Math.sqrt(katA1*katA1 + katA2*katA2);
        double hypB = Math.sqrt(katB1*katB1 + katB2*katB2);

        return (hypA < hypB)? hypA : hypB;
    }

    /**
     * Checks for walls, that are colliding with the given Circle
     * @param circle looking for its place in life :(
     * @return Map where and whit what
     */
    public HashMap<WallFunction, ArrayList<Position>> calcCollisionsWithWalls(Circle circle) {
        HashMap<WallFunction, ArrayList<Position>> ret = new HashMap<>();

        Collection<FieldSection> sectionsToCheck = getTouchedSections(circle);

        // lists walls that have been checked already, to avoid repetition
        ArrayList<WallFunction> checkedWalls = new ArrayList<>();

        for (FieldSection fieldSection : sectionsToCheck) {
            fieldSection.getWalls().stream().filter(wall -> !checkedWalls.contains(wall)).forEach(wall -> {
                Position[] positions = circle.collides(wall);
                if (positions != null) ret.put(wall, new ArrayList<>(Arrays.asList(positions)));

                checkedWalls.add(wall);
            });
        }

        return ret;
    }

    public HashMap<WallFunction, Position> calcCollisionsWithWalls(LineFunction f) {
        return calcCollisionsWithWalls(f, getTouchedSections(f));
    }

        /**
         * Method returns collisions of the given Function f with a wall, listed
         * in one of the Sections to check
         * @param f to calculate with
         * @return Map where and with what
         */
    public HashMap<WallFunction, Position> calcCollisionsWithWalls(LineFunction f,
                                                                   Collection<FieldSection> sectionsToCheck) {
        HashMap<WallFunction, Position> ret = new HashMap<>();

        // lists walls that have been checked already, to avoid repetition
        ArrayList<WallFunction> checkedWalls = new ArrayList<>();

        for (FieldSection fieldSection : sectionsToCheck) {
            fieldSection.getWalls().stream().filter(wall -> !checkedWalls.contains(wall)).forEach(wall -> {
                Position pos = wall.collides(f);
                if (pos != null) ret.put(wall, pos);

                checkedWalls.add(wall);
            });
        }

        return ret;
    }

    public Collection<FieldSection> getTouchedSections(Circle circle) {
        // TODO: 28/08/2017 Not tested yet
        ArrayList<FieldSection> ret = new ArrayList<>();

        FieldSection topLeft = getSectionAt(
                new Position(circle.center.getX()-circle.radius,
                        circle.center.getY()+circle.radius
                ));
        FieldSection botRight = getSectionAt(
                new Position(circle.center.getX()+circle.radius,
                        circle.center.getY()-circle.radius
                ));

        for (int xIndex = topLeft.X; xIndex <= botRight.X; xIndex++)
            for (int yIndex = topLeft.Y; yIndex <= topLeft.Y; yIndex++)
                ret.add(getSections()[xIndex][yIndex]);

        return ret;
    }

    /**
     * Method returns all Sections touched by the given LineFunction
     * @param f to calculate with
     * @return touched FieldSections
     */
    public Collection<FieldSection> getTouchedSections(LineFunction f) {
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

    public void setFlag(Flag flag) {
        this.flag = flag;

        // method gets automatically called on all Sections of this Field, that can reach the flag
        getSectionAt(flag.center).reachesFlag(1);

        validationPlayableMapSize();
    }

    public Flag getFlag() {
        return flag;
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
                [xPos / (width  / horizontalSectionAmount)]
                [yPos / (height / verticalSectionAmount)];
    }

    FieldSection[][] getSections() {
        return sections;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.translate(0, height);
        g2.scale(1.0, -1.0);

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);
        // walls
        for (WallFunction wall : walls)
            wall.paint(g2);

        // flag
        g.setColor(Color.GREEN.darker());
        flag.paint(g2);

        // spawns
        g.setColor(Color.BLUE);
        for (Spawn spawn : spawns)
            spawn.paint(g2);
    }


    @SuppressWarnings("all")
    private void validationClassVariables() {
        if (width % horizontalSectionAmount != 0 || height % verticalSectionAmount != 0)
            throw new BadFieldException("Bad Width or Height", BadFieldException.BadFieldType.CONST_ERROR);
        if (width / horizontalSectionAmount < 2 * Values.UNIT_RADIUS ||
                height / verticalSectionAmount < 2 * Values.UNIT_RADIUS)
            throw new BadFieldException("Sections too small", BadFieldException.BadFieldType.CONST_ERROR);
    }

    private void validationPlayableMapSize() {
        double totalSections = horizontalSectionAmount*verticalSectionAmount;

        // all Sections, that can reach the flag
        int sectionsToPlayWith = 0;

        for (FieldSection[] fieldSections : getSections())
            for (FieldSection fieldSection : fieldSections)
                if (fieldSection.canReachTheFlag)
                    sectionsToPlayWith++;

        int percent = (int)(sectionsToPlayWith/totalSections*100);

        if (percent < Values.REACH_FLAG_MINIMUM_PERCENTAGE)
            throw new BadFieldException("Only "+percent+"% of the map can reach the flag",
                    BadFieldException.BadFieldType.RANDOM_ERROR);
    }
}