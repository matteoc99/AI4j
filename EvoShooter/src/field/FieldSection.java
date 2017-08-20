package field;

import math.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 */
public class FieldSection {

    private ArrayList<WallFunction> walls = new ArrayList<>();

    public final int X;
    public final int Y;

    public final int TOP;
    public final int BOT;
    public final int LEFT;
    public final int RIGHT;


    public FieldSection(int x, int y, Position topLeft, Position botRight) {
        X = x;
        Y = y;
        TOP = topLeft.y;
        BOT = botRight.y;
        LEFT = topLeft.x;
        RIGHT = botRight.x;
    }

    void addWall(WallFunction wallFunction) {
        if (!walls.contains(wallFunction))
            ;
        else
            System.out.println("Meh! AddWallTwice");

        walls.add(wallFunction);
    }

    public ArrayList<WallFunction> getWalls() {
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

    private String getWallIDs() {
        if (walls.size() == 0) return "none";

        String ret = "";
        for (WallFunction wall : walls) {
            ret+=wall.iD+", ";
        }
        return ret.substring(0, ret.length()-2);
    }
}
