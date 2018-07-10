package es_map;

import bot.Unit;
import field_library.field.BadFieldException;
import field_library.field.Field;
import field_library.field.FieldSection;
import values.Values;

import java.awt.*;
import java.util.*;

/**
 * @author Maximilian Estfeller
 * @since 31.08.2017
 */
public class ESField extends Field{

    private ArrayList<Spawn> spawns = new ArrayList<>();

    /**
     * No usage yet
     *
     * Later represents the goal of the game.
     */
    private Flag flag;

    private int nextTeamIndex = 0;

    private Map<Integer, ArrayList<Unit>> teams = new HashMap<>();

    public ESField(int width,
                 int height,
                 int horizontalSectionAmount,
                 int verticalSectionAmount) {
        super(width, height, horizontalSectionAmount, verticalSectionAmount, ESFieldSection.class);
        validationClassVariables();
    }

    public void addSpawns(Collection<Spawn> spawns) {
        this.spawns.addAll(spawns);
        for (Spawn spawn : spawns) {
            spawn.setTeamIndex(nextTeamIndex);
            teams.putIfAbsent(nextTeamIndex, new ArrayList<>());
            nextTeamIndex++;
        }
    }

    public void addUnits(Unit... units) {
        if (teams.isEmpty())
            throw new BadFieldException("Set teams before adding Units", BadFieldException.BadFieldType.BUG_ERROR);

        for (Unit unit : units) {
            Integer smallestTeam = 0;

            // find smallest team
            for (Integer integer : teams.keySet())
                if (teams.get(integer).size() < teams.get(smallestTeam).size())
                    smallestTeam = integer;

            teams.get(smallestTeam).add(unit);
            unit.setTeamIndex(smallestTeam);
        }
    }

    public void setFlag(Flag flag) {
        this.flag = flag;

        // method gets automatically called on all Sections of this Field, that can reach the flag
        ((ESFieldSection) getSectionAt(flag.center)).reachesFlag(1);

        validationPlayableMapSize();
    }

    public Flag getFlag() {
        return flag;
    }

    private void validationPlayableMapSize() {
        double totalSections = horizontalSectionAmount*verticalSectionAmount;

        // all Sections, that can reach the flag
        int sectionsToPlayWith = 0;

        for (FieldSection[] fieldSections : getSections())
            for (FieldSection fieldSection : fieldSections)
                if (((ESFieldSection) fieldSection).canReachTheFlag)
                    sectionsToPlayWith++;

        int percent = (int)(sectionsToPlayWith/totalSections*100);

        if (percent < Values.REACH_FLAG_MINIMUM_PERCENTAGE)
            throw new BadFieldException("Only "+percent+"% of the map can reach the flag",
                    BadFieldException.BadFieldType.RANDOM_ERROR);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // flag
        g.setColor(Color.GREEN.darker());
        flag.paint(g);

        // spawns
        g.setColor(Color.BLUE);
        for (Spawn spawn : spawns)
            spawn.paint(g);
    }

    @SuppressWarnings("all")
    private void validationClassVariables() {
        if (width % horizontalSectionAmount != 0 || height % verticalSectionAmount != 0)
            throw new BadFieldException("Bad Width or Height", BadFieldException.BadFieldType.CONST_ERROR);
        if (width / horizontalSectionAmount < 2 * Values.UNIT_RADIUS ||
                height / verticalSectionAmount < 2 * Values.UNIT_RADIUS)
            throw new BadFieldException("Sections too small", BadFieldException.BadFieldType.CONST_ERROR);
    }
}
