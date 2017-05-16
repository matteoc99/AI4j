package game;

import com.sun.javafx.geom.Vec2d;

import javax.swing.*;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */

public class MobileObject extends ImmobileObject {


    /**
     * describes the speed at which the {@link MobileObject} is moving
     */
    private double speed;

    /**
     * Describes the current direction of this {@link MobileObject}
     */
    Vec2d direction;


    /**
     * Construktor for the {@link MobileObject}
     *
     * @param dateiname image to load in the {@link JComponent}
     */
    public MobileObject(String dateiname) throws ClassNotFoundException {
        super(dateiname);
    }

    //TODO get, set, move &direction
}
