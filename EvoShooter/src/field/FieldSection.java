package field;

import math.Position;

import java.util.ArrayList;


/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 */
class FieldSection {

    /**
     * Index in the Section[][] of a Field
     */
    public final int X;
    public final int Y;

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
    boolean isFreeToMoveOn = true;

    /**
     * A Section is reachable, when there is a path from the flag to this Section.
     * A Section which isn't free to move on can never be reachable.
     */
    boolean canReachTheFlag = false;

    /**
     * Amount of steps it takes to get from this Section to the Section the flag is located in
     */
    int stepsToFlag = Integer.MAX_VALUE;


    FieldSection(Field parent, int x, int y, Position topLeft, Position botRight) {
        this.parent = parent;
        X           = x;
        Y           = y;
        TOP         = (int) topLeft.getY();
        BOT         = (int) botRight.getY();
        LEFT        = (int) topLeft.getX();
        RIGHT       = (int) botRight.getX();
    }

    void addWall(WallFunction wallFunction) {
        isFreeToMoveOn = false;
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

    /**
     * Tells this Section, that it can reach the flag.
     *
     * Then also tells every neighbour, which is free to move on, that it reaches the flag too
     */
    void reachesFlag(int steps) {
        if (!canReachTheFlag) { // avoids infinite loops
            canReachTheFlag = true;
            stepsToFlag = steps;
            for (FieldSection fieldSection : getNeighbours())
                if (fieldSection.isFreeToMoveOn) fieldSection.reachesFlag(steps+1);
        }
    }

    private FieldSection[] getNeighbours() {
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
        if (walls.isEmpty()) isFreeToMoveOn = true;
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
