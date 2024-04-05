package game;

import javax.swing.*;
import java.awt.geom.Arc2D;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */

public class MobileObject extends ImmobileObject {

    /**
     * Declares the maximum speed this object can have
     */
    public static final int MAX_SPEED = 15;

    /**
     * describes the speed at which the {@link MobileObject} is moving
     */
    private double speed;


    /**
     * stores the current direction of the Object
     */
    Vec2d direction;

    /**
     * stores current the position of the Object
     */
    Vec2d position;

    /**
     * Construktor for the {@link MobileObject}
     *
     * @param dateiname image to load in the {@link JComponent}
     */
    public MobileObject(String dateiname) throws ClassNotFoundException {
        super(dateiname);
        direction = new Vec2d(0, 0);
        direction.x = 0;
        while (direction.x == 0) {
            direction.x = (int) (Math.random() * 8) - 4;
        }
        direction.y = (int) (Math.random() * 8) - 4;
    }

    //TODO get, set, &direction

    public void move() {
        if (position == null) {
            position = new Vec2d(0, 0);
            position.x = getX();
            position.y = getY();
        }
    }

    /**
     * Changes the Location of the current Object
     */

    protected void changeLoc(MobileObject mobileObject) {
        if (mobileObject instanceof Ball)
            if (direction.y < 0.1 && direction.y > -0.1)
                direction.y = Math.random() * 2 - 1;
        this.setLocation((int) (position.x + direction.x), (int) (position.y + direction.y));
        position.x = (int) (position.x + direction.x);
        position.y = (int) (position.y + direction.y);

    }

    public class Vec2d {
        public double x;
        public double y;

        public Vec2d(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
