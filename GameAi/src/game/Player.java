package game;

import agent.Agent;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;

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
     * the ball used to get his values to calculate where to go.
     */
    Ball ball;
    /**
     * Construktor for the {@link Player}
     *
     * @param dateiname image to load in the {@link JComponent}
     */
    public Player(String dateiname, Agent agent, Ball ball) throws ClassNotFoundException {
        super(dateiname);
        this.agent = agent;
        this.ball = ball;
        direction.x=0;
        direction.y=0;
        setSize(new Dimension(5, 100));
    }

    //IMPORTANT [0]= playerY [1]=ballY [2]=ballX [3]=ballYSpeed [4]=ballXSpeed

    @Override
    public void move() {
        super.move();
        if (agent != null) {
            double[] data = new double[]{calcPlayerY(position.y), calcBallY(ball)};

            double value = agent.processData(data)[0];
            value -= 0.5;
            direction.y = 10 * value;
        }
        if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) == null
                || getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) instanceof Player) {
            changeLoc(this);
        }
    }

    /**
     * calculates the xSpeed of the Ball and returns a value between 0-1
     *
     * @return a value between 0-1
     */
    private double calcBallXSpeed(Ball ball) {
        return ball.direction.y / MAX_SPEED;
    }

    /**
     * calculates the ySpeed of the Ball and returns a value between 0-1
     *
     * @return a value between 0-1
     */
    private double calcballYSpeed(Ball ball) {
        return ball.direction.y / MAX_SPEED;
    }

    /**
     * calculates the xPos of the Ball and returns a value between 0-1
     *
     * @return a value between 0-1
     */
    private double calcBallX(Ball ball) {
        return ball.position.x / (PlayGround.WIDTH - ball.getWidth());
    }

    /**
     * calculates the yPos of the Ball and returns a value between 0-1
     *
     * @return a value between 0-1
     */
    private double calcBallY(Ball ball) {
        return ball.position.y / (PlayGround.HEIGHT - ball.getHeight());
    }

    /**
     * calculates the player y and returns a value between 0-1
     *
     * @return a value between 0-1
     */
    private double calcPlayerY(double y) {
        return y / (PlayGround.HEIGHT - this.getHeight());
    }
}
