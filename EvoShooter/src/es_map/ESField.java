package es_map;

import field_library.field.BadFieldException;
import field_library.field.Field;
import field_library.field.FieldSection;
import values.Values;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

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


    public ESField(int width,
                 int height,
                 int horizontalSectionAmount,
                 int verticalSectionAmount) {
        super(width, height, horizontalSectionAmount, verticalSectionAmount, ESFieldSection.class);
    }

    public void addSpawns(Collection<Spawn> spawns) {
        this.spawns.addAll(spawns);
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
}
