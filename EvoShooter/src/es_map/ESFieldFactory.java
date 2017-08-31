package es_map;

import field_library.field.BadFieldException;
import field_library.field.FieldFactory;
import field_library.field.FieldSection;
import field_library.math.Function;
import field_library.math.LineFunction;
import field_library.math.Position;
import values.Values;

import java.util.*;

/**
 * @author Maximilian Estfeller
 * @since 31.08.2017
 */
public final class ESFieldFactory {

    private ESFieldFactory() {}

    public static ESField createRandomESField(int width, int height, int wallAmount) {
        try {
            try {
                ESField field = FieldFactory.createRandomField(width, height, wallAmount, ESField.class);

                field.setFlag(createFlag(field));
                field.addSpawns(createSpawns(field));

                return field;
            } catch (BadFieldException e) {
                if (e.isRandom()) return createRandomESField(width, height, wallAmount);
                else throw e;
            }
        } catch (StackOverflowError error) {
            throw new BadFieldException("Can't create a Field with given Constants",
                    BadFieldException.BadFieldType.CONST_ERROR);
        }
    }

    public static Flag createFlag(ESField field) {
        Flag flag;

        // middle Section
        FieldSection section = field.getSections()[field.getSections().length/2][field.getSections()[0].length/2];

        Position center = new Position((section.LEFT+section.RIGHT)/2, (section.TOP+section.BOT)/2);
        flag = new Flag(field, center);

        // check if Position is free, possibly clear it
        field.clearPosition(field.getSections()[field.getSections().length/2][field.getSections()[0].length/2]);

        if (!section.isFreeToMoveOn()) throw new BadFieldException("FlagSection not fully cleared",
                BadFieldException.BadFieldType.BUG_ERROR);

        return flag;
    }

    public static Collection<Spawn> createSpawns(ESField field) {
        List<Spawn> spawns = new ArrayList<>();

        for (int i = 0; i < Values.POSSIBLE_SPAWN_POSITIONS_COUNT; i++)
            spawns.add(new Spawn(field));

        setStartPosToSpawns(field, spawns, field.getFlag().center);

        // removes all spawns that can't reach the flag
        spawns.removeIf(spawn -> !((ESFieldSection) field.getSectionAt(spawn.center)).canReachTheFlag);

        // removes all spawns that can see directly to the flag
        spawns.removeIf(spawn -> {
            LineFunction visionLine = new LineFunction(spawn.center, field.getFlag().center);
            return field.calcCollisionsWithWalls(visionLine).isEmpty();
        });

        // Each pair of spawns, that do not see each other, have a calculated score
        Map<Integer, Collection<Spawn>> spawnPairPoints = getScore(field, spawns);

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
    private static void setStartPosToSpawns(ESField field, List<Spawn> spawns, Position center) {
        for (int i = 0; i < spawns.size(); i++) {

            Position goal = center.clone();

            // spread spawns in different directions
            double deg = 360.0/spawns.size()*i;
            double k = Function.calcSlopeByDeg(deg);

            // in order to move to the left, the distance must be negative
            double distance = (deg > 90 && deg < 270)? -field.calculateDistanceToEdge(goal, deg) :
                    field.calculateDistanceToEdge(goal, deg);

            goal.translateOnLine(k, distance*0.8);

            FieldSection section = field.getSectionAt(goal);
            spawns.get(i).center = new Position((section.LEFT+section.RIGHT)/2, (section.TOP+section.BOT)/2);
        }
    }

    /**
     * calculates and returns the points of each spawnPair
     *
     * @return points of each pair in form of a Map
     */
    private static Map<Integer, Collection<Spawn>> getScore(ESField field, List<Spawn> spawns) {
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
                spawnPairPoints.put(getIndividualScore(field, spawns.get(i), spawns.get(j)), pair);
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
    private static int getIndividualScore(ESField field, Spawn sp1, Spawn sp2) {
        int stepPoints = ((ESFieldSection) field.getSectionAt(sp1.center)).stepsToFlag +
                ((ESFieldSection) field.getSectionAt(sp2.center)).stepsToFlag;

        double fieldDiameter = Math.sqrt(field.width*field.width + field.height*field.height);
        double distanceFactor = sp1.center.distanceTo(sp2.center) / fieldDiameter;

        return (int)(stepPoints*distanceFactor);
    }
}