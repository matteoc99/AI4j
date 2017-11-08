package field_library.field;

import field_library.math.Position;

import java.util.ArrayList;


/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 */
public class FieldSection {

    /**
     * Index in the Section[][] of a Field
     */
    final int X;
    final int Y;

    /**
     * X or Y Values of where this Section ends
     */
    public final double TOP;
    public final double BOT;
    public final double LEFT;
    public final double RIGHT;

    /**
     * List storing all FieldLines of this Section
     *
     * ATTENTION: Don't change the accessibility, and don't add items directly to this value
     * unless you have spoken to the author.
     * Every addition and deletion can also change specific values of this or other Objects
     */
    private ArrayList<FieldLine> fieldLines = new ArrayList<>();

    /**
     * Reference to the parent Field
     */
    private Field parent;

    /**
     * A Section is free to move on as long as addFieldLine has not been called.
     * @see #addFieldLine(FieldLine)
     *
     * FieldLines that have been added directly to the List do not change this value.
     */
    private boolean freeToMoveOn = true;

    public FieldSection(Field parent, int x, int y, Position topLeft, Position botRight) {
        this.parent = parent;
        X           = x;
        Y           = y;
        TOP         = (int) topLeft.getY();
        BOT         = (int) botRight.getY();
        LEFT        = (int) topLeft.getX();
        RIGHT       = (int) botRight.getX();
    }

    void addFieldLine(FieldLine fieldLine) {
        freeToMoveOn = false;
        if (!fieldLines.contains(fieldLine))
            fieldLines.add(fieldLine);
        else {
            for (FieldSection fieldSection : getNeighbours()) {
                fieldSection.fieldLines.remove(fieldLine);
                fieldSection.fieldLines.add(fieldLine);
                fieldLine.getTouchedSections().add(fieldSection);
            }
        }
    }

    protected FieldSection[] getNeighbours() {
        FieldSection[] ret;
        ArrayList<FieldSection> neighbours = new ArrayList<>();

        FieldSection[][] sections = parent.getSections();
        if (X != 0)
            neighbours.add(sections[X-1][Y]);
        if (Y != 0)
            neighbours.add(sections[X][Y-1]);
        if (X != parent.horizontalSectionAmount-1)
            neighbours.add(sections[X+1][Y]);
        if (Y != parent.verticalSectionAmount-1)
            neighbours.add(sections[X][Y+1]);

        ret = new FieldSection[neighbours.size()];
        return neighbours.toArray(ret);
    }

    void removeFieldLine(FieldLine fieldLine) {
        fieldLines.remove(fieldLine);
        if (fieldLines.isEmpty()) freeToMoveOn = true;
    }

    ArrayList<FieldLine> getFieldLines() {
        return fieldLines;
    }

    @Override
    public String toString() {
        return "FieldSection{" +
                "X=" + X +
                ", Y=" + Y +
                ", TOP=" + TOP +
                ", BOT=" + BOT +
                ", LEFT=" + LEFT +
                ", RIGHT=" + RIGHT +
                ", Lines:[" + getLineIDs() +
                "]}";
    }

    public boolean isFreeToMoveOn() {
        return freeToMoveOn;
    }

    /**
     * Returns the IDs of the stored FieldLines as a String.
     * @return lineIDs
     */
    private String getLineIDs() {
        if (fieldLines.size() == 0) return "none";

        StringBuilder ret = new StringBuilder();
        for (FieldLine line : fieldLines) {
            ret.append(line.getId()).append(", ");
        }
        return ret.substring(0, ret.length()-2);
    }
}
