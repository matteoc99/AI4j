package field;

import math.Function;
import math.FunctionData;
import math.LineFunction;
import math.Position;
import values.Values;

import java.util.*;

import static math.Function.calcSlopeByDeg;
import static math.Function.calcXRaw;

/**
 * @author Maximilian Estfeller
 * @since 29.08.2017
 */
public class FieldCreationUtils {

    private FieldCreationUtils() {}

    public static Field createRandomField(int width, int height, int wallAmount) {
        try {
            try {
                Field field = new Field(width, height,
                        width  / (4*Values.UNIT_RADIUS),
                        height / (4*Values.UNIT_RADIUS));

                field.addWalls(createRandomWalls(field, wallAmount));
                field.setFlag(createFlag(field));
                field.addSpawns(createSpawns(field));

                return field;
            } catch (BadFieldException e) {
                if (e.isRandom()) return createRandomField(width, height, wallAmount);
                else throw e;
            }
        } catch (StackOverflowError error) {
            throw new BadFieldException("Can't create a Field with given Constants",
                    BadFieldException.BadFieldType.CONST_ERROR);
        }
    }

    public static Collection<FunctionData> createRandomWalls(Field field, int amount) {
        Collection<FunctionData> walls = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            walls.add(createRandomWall(field));
        }
        return walls;
    }

    public static Flag createFlag(Field field) {
        Flag flag;

        // middle Section
        FieldSection section = field.getSections()[field.getSections().length/2][field.getSections()[0].length/2];

        Position center = new Position((section.LEFT+section.RIGHT)/2, (section.TOP+section.BOT)/2);
        flag = new Flag(field, center);

        // check if Position is free, possibly clear it
        field.clearPosition(field.getSections()[field.getSections().length/2][field.getSections()[0].length/2]);

        if (!section.isFreeToMoveOn) throw new BadFieldException("FlagSection not fully cleared",
                BadFieldException.BadFieldType.BUG_ERROR);

        return flag;
    }

    public static Collection<Spawn> createSpawns(Field field) {
        List<Spawn> spawns = new ArrayList<>();

        for (int i = 0; i < Values.POSSIBLE_SPAWN_POSITIONS_COUNT; i++)
            spawns.add(new Spawn(field));

        setStartPosToSpawns(field, spawns, field.getFlag().center);

        // removes all spawns that can't reach the flag
        spawns.removeIf(spawn -> !field.getSectionAt(spawn.center).canReachTheFlag);

        // removes all spawns that can see directly to the flag
        spawns.removeIf(spawn -> {
            LineFunction visionLine = new LineFunction(spawn.center, field.getFlag().center);
            return field.calcCollisionsWithWalls(visionLine).isEmpty();
        });

        // Each pair of spawns, that do not see each other, have a calculated amount of points
        Map<Integer, Collection<Spawn>> spawnPairPoints = getPoints(field, spawns);

        int bestPoints = 0;
        for (Integer points : spawnPairPoints.keySet()) {
            if (points > bestPoints)
                bestPoints = points;
        }

        // remove all other spawns
        Collection<Spawn> bestPair = (spawnPairPoints.get(bestPoints) != null)?
                spawnPairPoints.get(bestPoints) : new ArrayList<>();
        spawns.removeIf(spawn -> !bestPair.contains(spawn));

        if (spawns.size() > 2)
            throw new BadFieldException("Too many spawns", BadFieldException.BadFieldType.BUG_ERROR);
        if (spawns.size() < 2)
            throw new BadFieldException("Not enough spawns", BadFieldException.BadFieldType.RANDOM_ERROR);

        return spawns;
    }

    /**
     * sets different goals for each spawn
     */
    private static void setStartPosToSpawns(Field field, List<Spawn> spawns, Position center) {
        for (int i = 0; i < spawns.size(); i++) {

            Position goal = center.clone();

            // spread spawns in different directions
            double deg = 360.0/spawns.size()*i;
            double k = Function.calcSlopeByDeg(deg);

            // in order to move to the left, the distance must be negative
            double distance = (deg > 90 && deg < 270)? -field.calculateDistance(goal, deg) :
                    field.calculateDistance(goal, deg);

            goal.translateTowards(k, distance*0.8);

            FieldSection section = field.getSectionAt(goal);
            spawns.get(i).center = new Position((section.LEFT+section.RIGHT)/2, (section.TOP+section.BOT)/2);
        }
    }

    /**
     * calculates and returns the points of each spawnPair
     *
     * @return points of each pair in form of a Map
     */
    private static Map<Integer, Collection<Spawn>> getPoints(Field field, List<Spawn> spawns) {
        Map<Integer, Collection<Spawn>> spawnPairPoints = new HashMap<>();

        for (int i = 0; i < spawns.size(); i++) {
            for (int j = i; j < spawns.size(); j++) {
                // same Spawns
                if (spawns.get(i) == spawns.get(j))
                    continue;

                // no entry in case the spawns see each other
                if (field.calcCollisionsWithWalls(
                        new LineFunction(spawns.get(i).center, spawns.get(j).center)).isEmpty())
                    continue;

                Collection<Spawn> pair = Arrays.asList(spawns.get(i), spawns.get(j));

                // entry
                spawnPairPoints.put(getIndividualPoints(field, spawns.get(i), spawns.get(j)), pair);
            }
        }

        return spawnPairPoints;
    }
    /**
     * calculates the points for a pair of spawns
     * points = (Sum)stepsToFlag * distance between the points relative to the map size (0-1)
     *
     * @return points
     */
    private static int getIndividualPoints(Field field, Spawn sp1, Spawn sp2) {
        int stepPoints = field.getSectionAt(sp1.center).stepsToFlag +
                field.getSectionAt(sp2.center).stepsToFlag;

        double distanceFactor = sp1.center.distanceTo(sp2.center) / field.diameter;

        return (int)(stepPoints*distanceFactor);
    }

    public static FunctionData createRandomWall(Field field) {
        FunctionData ret = new FunctionData();

        // degree of the function
        double  deg;
        // offsets to guarantee the function has any Point within this field
        int     xOff;
        int     yOff;
        // length of the visible function
        double  length;

        // generating random values
        deg = (Math.random() * 360);
        xOff = (int) (Math.random() * (field.width-1));
        yOff = (int) (Math.random() * (field.height-1));

        ret.setK(calcSlopeByDeg(deg));

        // deciding length of the visible part of the wall + validation
        do {
            length = (field.width + field.height) / 4 + new Random().nextGaussian() * 10;
        } while (length < (field.width + field.height) / 20 ||
                length > field.width || length > field.height);

        double h = (Math.abs(Math.cos(Math.toRadians(deg))) * length);

        ret.setA(xOff -h/2);
        ret.setB(xOff +h/2);

        // conversion to normal Function format for simpler calculation
        ret.setD(ret.getK() * -xOff + yOff);

        // adjusts a and b to keep EVERY point of the LineFunction within the field
        adjustABValues(field, ret);

        return ret;
    }

    private static void adjustABValues(Field field, FunctionData ret) {
        // adjusting a and b to be within the field
        if (ret.getA() < 0) ret.setA(0);
        if (ret.getB() > field.width-1) ret.setB(field.width-1);

        // adjusting a and b so that their y-value is within the field

        double topOut = calcXRaw(field.height-1, ret);
        double botOut = calcXRaw(0, ret);

        double aOut = (topOut < botOut)? topOut : botOut;
        double bOut = (topOut > botOut)? topOut : botOut;

        if (ret.getA() < aOut) ret.setA(aOut);
        if (ret.getB() > bOut) ret.setB(bOut);
    }
}