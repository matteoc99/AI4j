package field_library.field;

import field_library.math.*;
import values.Values;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

/**
 *
 *
 * @author Maximilian Estfelller
 * @since 18.07.2017
 */
public class Field extends Canvas {

    public final int width;
    public final int height;


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
    protected final FieldSection[][] sections;

    /**
     * storing all fieldLines
     */
    private ArrayList<FieldLine> fieldLines = new ArrayList<>();

    public Field(int width,
                 int height,
                 int horizontalSectionAmount,
                 int verticalSectionAmount) {
        this(width, height, horizontalSectionAmount, verticalSectionAmount, FieldSection.class);
    }

    /**
     * @param width of the Field
     * @param height of the Field
     * @param horizontalSectionAmount tells how many Sections to create horizontally
     * @param verticalSectionAmount tells how many Sections to create vertically
     * @param sectionType specifies the type of FieldSection to fill in the sections[][]
     */
    public Field(int width,
                 int height,
                 int horizontalSectionAmount,
                 int verticalSectionAmount,
                 Class<? extends FieldSection> sectionType) {

        this.width = width;
        this.height = height;
        this.horizontalSectionAmount = horizontalSectionAmount;
        this.verticalSectionAmount = verticalSectionAmount;

        this.sections = new FieldSection[horizontalSectionAmount][verticalSectionAmount];

        createSections(sectionType);

        this.setBounds(0, 0, width, height);
    }

    /**
     * Method fills the sections[][] with FieldSections of the given type
     * @param type of the Sections
     */
    protected void createSections(Class<? extends FieldSection> type) {
        // per index
        int widthPI = width/horizontalSectionAmount;
        int heightPI = height/verticalSectionAmount;

        for (int y = 0; y < verticalSectionAmount; y++)
            for (int x = 0; x < horizontalSectionAmount; x++) {
                createSection(type, widthPI, heightPI, y, x);
            }
    }

    /**
     * Method creates a FieldSection of the given type
     * @param type of FieldSection to create
     * @param widthPI constructorParam
     * @param heightPI constructorParam
     * @param y index of this Section in the sections[][], constructorParam
     * @param x index of this Section in the sections[][], constructorParam
     */
    private void createSection(Class<? extends FieldSection> type, int widthPI, int heightPI, int y, int x) {
        try {
            sections[x][y] = type.getConstructor(Field.class,
                    int.class, int.class,
                    Position.class, Position.class)
                    .newInstance(this, x, y,
                            new Position(x * widthPI, y * heightPI),
                            new Position((x + 1) * widthPI, (y + 1) * heightPI));
        } catch (NoSuchMethodException | IllegalAccessException |
                InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            throw new BadFieldException("Can't create given Class with given Constructor params",
                    BadFieldException.BadFieldType.BUG_ERROR);
        }
    }

    /**
     * Transformers some FunctionData into actual FieldLines and adds them to this Field
     * @param lineData data
     */
    public void addFieldLines(Collection<FunctionData> lineData) {
        for (FunctionData lineDatum : lineData) {
            FieldLine line = new FieldLine(this, lineDatum);
            fieldLines.add(line);
        }
    }

    /**
     * Removes all fieldLines, which are, at any Point, in one of the given Sections
     * @param sections to clear
     */
    public void clearPosition(FieldSection... sections) {
        // gathers all fieldLines in another List (to avoid ConcurrentModificationException)
        List<FieldLine> affectedLines = new ArrayList<>();
        for (FieldSection section : sections)
            affectedLines.addAll(section.getFieldLines());

        // removes all fieldLines from all Sections and this Field
        for (FieldLine affectedLine : affectedLines) {
            for (FieldSection section : affectedLine.getTouchedSections())
                section.removeFieldLine(affectedLine);
            fieldLines.remove(affectedLine);
        }
    }

    /**
     * Method returns the distance from pos to the edge of the map
     * in the given direction deg
     * @param pos Position to start from
     * @param deg degree to go to
     * @return distance to the edge of the map
     */
    public double calculateDistanceToEdge(Position pos, double deg) {
        deg = Degrees.normalizeDeg(deg);

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
     * Checks for fieldLines, that are colliding with the given Circle
     * @param circle looking for its place in life :(
     * @return Map where and whit what
     */
    public HashMap<FieldLine, ArrayList<Position>> calcCollisionsWithLine(Circle circle) {
        HashMap<FieldLine, ArrayList<Position>> ret = new HashMap<>();

        Collection<FieldSection> sectionsToCheck = getTouchedSections(circle);

        // lists fieldLines that have been checked already, to avoid repetition
        ArrayList<FieldLine> checkedLines = new ArrayList<>();

        for (FieldSection fieldSection : sectionsToCheck) {
            fieldSection.getFieldLines().stream().filter(line -> !checkedLines.contains(line)).forEach(line -> {
                Position[] positions = circle.collides(line);
                if (positions != null) ret.put(line, new ArrayList<>(Arrays.asList(positions)));

                checkedLines.add(line);
            });
        }

        return ret;
    }

    /**
     * Method returns the first collision happening, starting from pos and going in the direction
     * specified by deg
     *
     * @param pos to start from
     * @param deg to go to
     * @return The Position of the collision and the Object collided with
     */
    public Pair<Position, FieldLine> calcFirstCollisionWithLine(Position pos, double deg) {
        deg = Degrees.normalizeDeg(deg);

        Position firstCollision = pos.clone();
        firstCollision.translateTowards(deg, calculateDistanceToEdge(pos, deg));

        // representing every possible Position, where a collision can happen
        LineFunction f = new LineFunction(pos, firstCollision);

        HashMap<FieldLine, Position> collisions = calcCollisionsWithLine(f);

        FieldLine collisionObject = null;

        if (deg > 90 && deg <= 270) { // towards left
            for (FieldLine fieldLine : collisions.keySet()) {
                if (collisions.get(fieldLine).getX() >= firstCollision.getX()) {
                    firstCollision = collisions.get(fieldLine);
                    collisionObject = fieldLine;
                }
            }
        } else { // towards right
            for (FieldLine fieldLine : collisions.keySet()) {
                if (collisions.get(fieldLine).getX() <= firstCollision.getX()) {
                    firstCollision = collisions.get(fieldLine);
                    collisionObject = fieldLine;
                }
            }
        }

        return new Pair<>(firstCollision, collisionObject);
    }

    /**
     * Method calculates the Collision of a given LineFunction f with every line of this Field
     *
     * Since both Functions are LineFunctions the can also be no Collision between them
     * @param f to collide with
     * @return Map describing every Collision; collisionObject and collisionPosition
     */
    public HashMap<FieldLine, Position> calcCollisionsWithLine(LineFunction f) {
        HashMap<FieldLine, Position> ret = new HashMap<>();

        for (FieldLine line : fieldLines) {
            Position pos = line.collides(f);
            if (pos != null) ret.put(line, pos);
        }

        return ret;
    }

        /**
         * Method returns collisions of the given Function f with a line, listed
         * in one of the Sections to check.
         *
         * As the calculation of touched Sections is quite expensive, this method should only be used
         * for Fields with a very large amount of fieldLines.
         *
         * ATTENTION: Method also returns CollisionPoints, which are not within one of the given Sections,
         * as far as they are within the specified range of a LineFunction
         *
         * @param f to calculate with
         * @return Map where and with what
         */
    public HashMap<FieldLine, Position> calcCollisionsWithLine(LineFunction f,
                                                               Collection<FieldSection> sectionsToCheck) {
        HashMap<FieldLine, Position> ret = new HashMap<>();

        // lists fieldLines that have been checked already, to avoid repetition
        ArrayList<FieldLine> checkedLines = new ArrayList<>();

        for (FieldSection fieldSection : sectionsToCheck) {
            fieldSection.getFieldLines().stream().filter(line -> !checkedLines.contains(line)).forEach(line -> {
                Position pos = line.collides(f);
                if (pos != null) ret.put(line, pos);

                checkedLines.add(line);
            });
        }

        return ret;
    }

    /**
     * Method returns all Sections that are within a square around the Circle
     * @param circle to calculate with
     * @return touched FieldSections
     */
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

                // calculating the height at which this FieldLine enters a new Section
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

                // calculating the xPos at which this FieldLine enters a new Section
                double xRes = f.calcX(botLine);
                ret.add(getSectionAt(new Position(xRes, botLine)));
            }
        }

        return ret;
    }

    public ArrayList<FieldLine> getFieldLines() {
        return fieldLines;
    }

    /**
     * Method returns the Section containing the given Position
     * @param pos to check
     * @return FieldSection at Position pos
     */
    public FieldSection getSectionAt(Position pos) {
        int xPos = (int)pos.getX();
        int yPos = (int)pos.getY();

        if (xPos == width) xPos --;
        if (yPos == height) yPos--;

        return sections
                [xPos / (width  / horizontalSectionAmount)]
                [yPos / (height / verticalSectionAmount)];
    }

    public FieldSection[][] getSections() {
        return sections;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);
        // fieldLines
        for (FieldLine line : fieldLines)
            line.paint(g2);
    }

    private class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            if (!key.equals(pair.key)) return false;
            return value.equals(pair.value);
        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }
}