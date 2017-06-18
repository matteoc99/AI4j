package botGUI.bot;

import agent.Agent;
import botGUI.Chunk;
import botGUI.World;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matteo Cosi
 * @since 14/06/2017
 */
public class Bot extends JPanel {

    /**
     * Contains the age of the bot.
     * The older he is, the more difficult it is to eat
     */
    public int age = 1;
    public int ageingCounter = 0;

    /**
     * describes how fast the agent Ages
     * greater value = slower aging
     */
    private static final int AGEING_SPEED = 5000;


    /**
     * describes the length of the sensor
     */
    private int sensor_length = 60;

    /**
     * the Body of the Bot
     */
    Body body;


    /**
     * The sensor of the Bot
     */
    Sensor sensor;

    /**
     * Describes the rotation of the sensor
     * 0-360 degrees
     */
    public int sensorRotation = 0;


    /**
     * direction of the Bot
     */
    public int xDir = 1;
    public int yDir = 1;

    /**
     * Variable that stores th current hp
     */
    public int hp = 500;

    /**
     * {@link Agent} that controls the player behaviour
     */
    Agent agent = null;

    public Bot(Agent agent) {
        this.agent = agent;

        xDir = (int) (Math.random() * 1);
        yDir = (int) (Math.random() * 1);

        sensor = new Sensor();
        body = new Body();

        setSize(sensor.getWidth() + body.getWidth() + sensor_length, sensor.getHeight() + body.getHeight() + sensor_length);

        body.setLocation(getWidth() / 2 - body.getWidth() / 2, getHeight() / 2 - body.getHeight() / 2);

        //should be width/2
        sensor_length = getWidth() / 2;

        rotateAndResize(1);
        add(sensor);
        add(body);

        //transparent
        setOpaque(false);
        repaint();
    }


    @Override
    public void paint(Graphics g) {
        //draw a line tha connects Sensor and Body
        g.setColor(Color.BLACK);
        g.drawLine(body.getX() + body.getWidth() / 2, body.getY() + body.getHeight() / 2,
                sensor.getX() + sensor.getWidth() / 2, sensor.getY() + sensor.getHeight() / 2);
        super.paint(g);
        repaint();
    }

    /**
     * rotate with a given angle and resize and reposition the Sensor
     */
    public void rotateAndResize(int angle) {
        int radius = sensor_length - sensor.getWidth() / 2;
        sensorRotation += angle;
        sensorRotation = sensorRotation % 360;
        rotateAndResize();
    }

    /**
     * rotate and resize and reposition the Sensor
     */
    public void rotateAndResize() {
        int radius = sensor_length - sensor.getWidth() / 2;

        int x = (int) (radius * Math.cos(Math.toRadians(sensorRotation)));
        int y = (int) (radius * Math.sin(Math.toRadians(sensorRotation)));

        if (x < getWidth() / 2)
            x += getWidth() / 2;
        else
            x -= getWidth() / 2;
        if (y < getHeight() / 2)
            y += getHeight() / 2;
        else
            y -= getHeight() / 2;

        sensor.setLocation(getWidth() - x - sensor.getWidth() / 2, getHeight() - y - sensor.getHeight() / 2);
    }

    /**
     * Object dies and removes itself from the Container where it was previously in.
     */
    public void kill() {
        if (this.getParent() != null) {
            this.getParent().remove(this);
        }
        if (World.population.contains(this))
            World.population.remove(this);
    }

    /**
     * simple move method
     */
    public void move() {
        //so that the bot isn too fast at dieing
        if (ageingCounter % 4 == 0)
            hp--;
        if (hp < 0) {
            kill();
        } else {
            body.setBodyColor(new Color(255, (502 - hp) / 2, (502 - hp) / 2));
        }
        if (ageingCounter < AGEING_SPEED) {
            ageingCounter++;
        } else {
            age++;
            ageingCounter = -300;
        }

        /*Agent Tells what to do
         * Inputs:
         * [0]->isLandUnderBody [0 oder 1]
         * [1]->isandUnderSensor [0 oder 1]
         * Outputs:
         * [0]->xDir
         * [1]->yDir
         * [2]->rotation of sensor
         * [3]->eat
         */
        System.out.println(getIsLandUnderBody() + "     " + getisLandUnderSensor());

        if (agent != null) {
            agent.processData(new double[]{getIsLandUnderBody(), getisLandUnderSensor()});
        }
        setLocation(getX() + xDir, getY() + yDir);
    }


    /**
     * Eat the from the greatest Chunk
     */
    public void eat() {
        try {
            Chunk c = (Chunk) getChunkUnder(getX(), getY(), body);
            if (c != null && c.getType() == Chunk.Type.LAND) {
                hp += c.getFood() / age;
                c.setFood(0);
                c.toUpdate = true;
                if (hp > 500)
                    hp = 500;
            }
        } catch (Exception e) {

        }
    }


    /**
     * returns the Chunk with the greatest intersection area under a sensor or body
     *
     * @param x x Position
     * @param y y Position
     * @return the Chunk with the greatest intersection area
     */
    public Component getChunkUnder(int x, int y, JComponent component) {
        Component ret = null;
        if (this.getParent() != null) {

            if (x < -component.getX() || y < -component.getX()
                    || x + component.getX() + component.getWidth() > this.getParent().getWidth()
                    || y + component.getY() + component.getHeight() > this.getParent().getHeight())
                ret = this.getParent();
            else {
                Rectangle neuePosition = new Rectangle(x + component.getX(), y + component.getY(), component.getWidth(), component.getHeight());
                Component[] komponenten = null;
                komponenten = this.getParent().getComponents();
                int i = 0;
                int bestSize = -1;
                while (komponenten != null && i < komponenten.length) {
                    if (komponenten[i] != this &&
                            neuePosition.intersects(komponenten[i].getBounds()) && komponenten[i] instanceof Chunk) {
                        double newX = Math.max(neuePosition.x, komponenten[i].getX());
                        double newY = Math.max(neuePosition.y, komponenten[i].getY());

                        double newWidth = Math.min(neuePosition.x + neuePosition.width, komponenten[i].getX() + komponenten[i].getWidth()) - newX;
                        double newHeight = Math.min(neuePosition.y + neuePosition.height, komponenten[i].getY() + komponenten[i].getHeight()) - newY;

                        Rectangle intersection = new Rectangle((int) newX, (int) newY, (int) newWidth, (int) newHeight);
                        int size = intersection.width * intersection.height;
                        if (size > bestSize) {
                            ret = komponenten[i];
                            bestSize = size;

                        }
                    }
                    i++;
                }
            }
        }
        return ret;
    }

    private double getIsLandUnderBody() {
        try {
            Chunk c = (Chunk) (getChunkUnder(getX(), getY(), sensor));
            return c.getType() == Chunk.Type.LAND ? 1 : 0;
        } catch (Exception e) {
            return 0.5;
        }
    }

    public double getisLandUnderSensor() {
        try {
            Chunk c = (Chunk) (getChunkUnder(getX(), getY(), sensor));
            return c.getType() == Chunk.Type.LAND ? 1 : 0;
        } catch (Exception e) {
            return 0.5;
        }
    }
}
