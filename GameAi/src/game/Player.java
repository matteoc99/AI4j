package game;

import agent.Agent;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matteo Cosi
 * @since 15.04.2017
 */
public class Player extends MobileObject {

    /**
     * the {@link Agent} that controls the {@link Player}
     */
    Agent agent;

    /**
     * Construktor for the {@link Player}
     *
     * @param dateiname image to load in the {@link JComponent}
     */
    public Player(String dateiname, Agent agent) throws ClassNotFoundException {
        super(dateiname);
        this.agent = agent;
        direction.x=0;
        direction.y=0;
        setSize(new Dimension(5, 100));
    }

    @Override
    public void move() {
        super.move();
        if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) == null
                || getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) instanceof Player) {
            changeLoc();
        }
    }
}
