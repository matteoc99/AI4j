package game;

import javax.swing.*;

/**
 *
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class Ball extends MobileObject {
    /**
     * Construktor for the {@link Ball}
     *
     * @param dateiname image to load in the {@link JComponent}
     */
    public Ball(String dateiname) throws ClassNotFoundException {
        super(dateiname);
    }
}
