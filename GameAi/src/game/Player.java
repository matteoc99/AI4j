package game;

import javax.swing.*;

/**
 * Created by matte on 15.05.2017.
 */
public class Player extends MobileObject {
    /**
     * Construktor for the {@link Player}
     *
     * @param dateiname image to load in the {@link JComponent}
     */
    public Player(String dateiname) throws ClassNotFoundException {
        super(dateiname);
    }
}
