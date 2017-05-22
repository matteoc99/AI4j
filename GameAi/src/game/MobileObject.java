package game;

import com.sun.javafx.geom.Vec2d;

import javax.swing.*;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */

public class MobileObject extends ImmobileObject {

    /**
     * Declares the maximum speed this object can have
     */
    public static final int MAX_SPEED=8;

    /**
     * describes the speed at which the {@link MobileObject} is moving
     */
    private double speed;

    /**
     * Describes the current direction of this {@link MobileObject}
     */
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
     *add comment
     * @param dateiname image to load in the {@link JComponent}
     */
    public MobileObject(String dateiname) throws ClassNotFoundException {
        super(dateiname);
        direction = new Vec2d();
        direction.x=(int)Math.random()*2-2;
        direction.y=(int)Math.random()*2-2;
    }

    //TODO get, set, &direction

    public void move(){
        if (position==null){
            position=new Vec2d();
            position.x=getX();
            position.y=getY();
        }
    }
    /**
     * Changes the Location of the current Object
     */

    protected void changeLoc() {
        this.setLocation((int) (position.x + direction.x), (int) (position.y + direction.y));
        position.x = (int) (position.x + direction.x);
        position.y = (int) (position.y + direction.y);
        if(Math.abs(direction.x)<MAX_SPEED) {
            direction.x *= 1.002;
        }
    }
}
