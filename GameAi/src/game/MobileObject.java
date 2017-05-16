package game;

import javax.swing.*;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */

public class MobileObject extends ImmobileObject {
    /**
     * Construktor for the {@link MobileObject}
     *
     * @param dateiname image to load in the {@link JComponent}
     */
    public MobileObject(String dateiname) throws ClassNotFoundException {
        super(dateiname);
    }
}
