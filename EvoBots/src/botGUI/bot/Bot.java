package botGUI.bot;

import botGUI.Chunk;
import botGUI.ImmobileObject;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matteo Cosi
 * @since 14/06/2017
 */
public class Bot extends JPanel {

    //TODO >60 BUG
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


    public int xDir = 1;
    public int yDir = 1;


    //TODO hp implementation
    public int hp = 500;


    public Bot() {
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


    }

    /**
     * simple move method
     */
    public void move() {
        setLocation(getX() + xDir, getY() + yDir);
    }


    /**
     * Eat the from the greatest Chunk
     */
    public void eat() {
        try {
            Chunk c = (Chunk) getChunkAtEat(getX(), getY());
            if (c != null) {
                c.setFood(0);
                c.toUpdate = true;
            }
        } catch (Exception e) {
        }
    }

    /**
     * returns the Chunk with the greatest intersection area at a given Position
     * @param x x Position
     * @param y y Position
     * @return  the Chunk with the greatest intersection area
     */
    public Component getChunkAtEat(int x, int y) {
        Component ret = null;
        if (this.getParent() != null) {

            if (x < 0 || y < 0 || x + this.getWidth() > this.getParent().getWidth() ||
                    y + this.getHeight() > this.getParent().getHeight())

                ret = this.getParent();
            else {
                Rectangle neuePosition = new Rectangle(x + body.getX(), y + body.getY(), body.getWidth(), body.getHeight());
                Component[] komponenten = null;
                komponenten = this.getParent().getComponents();
                int i = 0;
                int bestSize = -1;
                while (komponenten != null && i < komponenten.length) {
                    if (komponenten[i] != this &&
                            neuePosition.intersects(komponenten[i].getBounds()) && komponenten[i] instanceof Chunk) {
                        Chunk c = (Chunk) komponenten[i];
                        if (c.getType() == Chunk.Type.LAND) {
                            double newX = Math.max(neuePosition.x, komponenten[i].getX());
                            double newY = Math.max(neuePosition.y, komponenten[i].getY());

                            double newWidth = Math.min(neuePosition.x + neuePosition.width, komponenten[i].getX() + komponenten[i].getWidth()) - newX;
                            double newHeight = Math.min(neuePosition.y + neuePosition.height, komponenten[i].getY() + komponenten[i].getHeight()) - newY;

                            Rectangle intersection = new Rectangle((int) newX, (int) newY, (int) newWidth, (int) newHeight);
                            int size = intersection.width * intersection.height;
                            System.out.println(size);
                            if (size > bestSize) {
                                ret = komponenten[i];
                                bestSize = size;
                            }
                        }
                    }
                    i++;
                }
            }
        }
        return ret;
    }

}
