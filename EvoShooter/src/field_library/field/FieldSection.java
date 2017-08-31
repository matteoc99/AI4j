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
     * List storing all WallFunctions of this Section
     *
     * ATTENTION: Don't change the accessibility, and don't add items directly to this value
     * unless you have spoken to the author.
     * Every addition and deletion can also change specific values of this or other Objects
     */
    private ArrayList<WallFunction> walls = new ArrayList<>();

    /**
     * Reference to the parent Field
     */
    private Field parent;

    /**
     * A Section is free to move on as long as addWall has not been called.
     * @see #addWall(WallFunction)
     *
     * Walls that have been added directly to the List do not change this value.
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

    void addWall(WallFunction wallFunction) {
        freeToMoveOn = false;
        if (!walls.contains(wallFunction))
            walls.add(wallFunction);
        else {
            for (FieldSection fieldSection : getNeighbours()) {
                fieldSection.walls.remove(wallFunction);
                fieldSection.walls.add(wallFunction);
                wallFunction.getTouchedSections().add(fieldSection);
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

    void removeWall(WallFunction wallFunction) {
        walls.remove(wallFunction);
        if (walls.isEmpty()) freeToMoveOn = true;
    }

    ArrayList<WallFunction> getWalls() {
        return walls;
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
                ", Walls:[" + getWallIDs() +
                "]}";
    }

    public boolean isFreeToMoveOn() {
        return freeToMoveOn;
    }

    /**
     * Returns the IDs of the stored wall as a String.
     * @return wallIDs
     */
    private String getWallIDs() {
        if (walls.size() == 0) return "none";

        StringBuilder ret = new StringBuilder();
        for (WallFunction wall : walls) {
            ret.append(wall.getId()).append(", ");
        }
        return ret.substring(0, ret.length()-2);
    }
}
