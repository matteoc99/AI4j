package game;

import agent.Agent;

import javax.swing.*;

/**
 * Created by matte on 15.05.2017.
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
    public Player(String dateiname,Agent agent) throws ClassNotFoundException {
        super(dateiname);
        this.agent=agent;
    }
}
