package botGUI;

import javax.swing.*;

/**
 * @author Matteo Cosi
 * @since 13.06.2017
 */
public class Chunk extends ImmobileObject {

    public enum Type {
        LAND, WATER,
    }

    /**
     * Fertility can go from 0 to 100.
     * It describes the speed of the Food regrowth
     */
    public int fertility = 0;


    /**
     * Construktor for the {@link ImmobileObject}
     *
     * @param dateiname image to load in the {@link JComponent}
     */
    public Chunk(String dateiname) throws ClassNotFoundException {
        super(dateiname);
    }
}
