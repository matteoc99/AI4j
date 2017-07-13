package botGUI.bot;

import agent.Agent;
import agent.cosi.CosiAgent;
import botGUI.Chunk;
import botGUI.World;
import network.Network;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static botGUI.World.*;

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
    private static final int AGEING_SPEED = 530;

    /**
     * describes the length of the sensor
     */
    private int sensor_length = World.CHUNK_SIZE * 2;


    /**
     * the Body of the Bot
     */
    public Body body;

    /**
     * The sensor of the Bot
     */
    public Sensor sensor;


    /**
     * Describes the rotation of the sensor
     * 0-360 degrees
     */
    public int sensorRotation = (int) (Math.random() * 360);

    /**
     * direction of the Bot
     */
    public int xDir = 1;
    public int yDir = 1;

    /**
     * Variable that stores th current hp
     */
    public int hp = 500;
    private static final int MAX_HP = 1000;

    /**
     * {@link Agent} that controls the player behaviour
     */
    public Agent agent = null;

    /**
     * tells if the bot should be killed
     */
    public boolean kill = false;

    /**
     * Color of the bot
     */
    public int red = (int) (Math.random() * 180) + 30;
    public int blue = (int) (Math.random() * 180) +30;
    public int green = (int) (Math.random() * 180) + 30;


    /**
     * if this value equals the age, a child is born
     */
    int makeChildren = 2;

    /**
     * stores the previous World chunk size
     */
    int prevChunkSize = World.CHUNK_SIZE;

    public boolean selected = false;

    public World world;


    public int generation;


    public Bot(Agent agent, World world, int generation) {
        this.world = world;
        this.agent = agent;
        this.generation = generation;


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
        if (selected) {
            g.setColor(new Color(200, 250, 200));
            g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
        }
        super.paint(g);
        repaint();
    }

    /**
     * rotate with a given angle and resize and reposition the Sensor
     */
    public void rotateAndResize(int angle) {
        sensorRotation += angle + (Math.random() * 15) - 7.5;
        sensorRotation = sensorRotation % 360;
        rotateAndResize();
    }

    /**
     * rotate and resize and reposition the Sensor
     */
    public void rotateAndResize() {
        body.setLocation(getWidth() / 2 - body.getWidth() / 2, getHeight() / 2 - body.getHeight() / 2);

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
        if (world.population.contains(this)) {
            world.population.remove(this);
        }
    }

    /**
     * simple move method
     */
    public void move() {
        if (kill)
            kill();
        //pusish for water
        if (getChunkUnder(getX(), getY(), body).getType() == Chunk.Type.WATER)
            hp -= 5;
        hp--;
        if (hp < 0) {
            kill();
        } else {
            //color
         /*   int temp = hp > MAX_HP / 2 ? MAX_HP - hp : hp;
            temp= Math.abs(temp-500);
            int red = this.red+(temp/4);
            int blue =this.blue+(temp/4);
            int green =this.green+(temp/4);

            if (red < 0)
                red = 0;
            if (green < 0)
                green = 0;
            if (blue < 0)
                blue = 0;
            if (red > 255)
                red = 255;
            if (green > 255)
                green = 255;
            if (blue > 255)
                blue = 255;

           */ body.setBodyColor(new Color(red, green, blue));
        }
        if (ageingCounter < AGEING_SPEED) {
            ageingCounter++;
        } else {
            ageingCounter = -100 * age;
            age++;
        }

        if (age == makeChildren) {
            if (age >= 3) {
                System.out.print("AGE " + age + " : {");
                for (double d : agent.getNet().getDescriptor())
                    System.out.print(", " + d);
                System.out.println("}");
            }
            makeChildren++;
            makeChildren(2);
        }

        /*Agent Tells what to do
         * Inputs:
         * [0]->isLandUnderBody [0 oder 1]
         * [1]->isandUnderSensor [0 oder 1]
         * [2]->hp [0-1] 1 wenn wenig hp
         * [4]->food under body [0-1]
         * [5]->food under sensor [0-1]
         * Outputs:
         * [0]->speed
         * [1]->rotation of sensor
         * [2]->eat
         */
        if (agent != null) {
            agent.increaseFitness();
            //   System.out.println("IN: "+getIsLandUnderBody()+" "+getisLandUnderSensor()+" "+getHp()+" "+getFoodUnderBody()+" "+getFoodUnderSensor());
            double out[] = agent.processData(new double[]{getIsLandUnderBody(), getisLandUnderSensor(), getHp(), getFoodUnderBody(), getFoodUnderSensor()});
            if (out[0] > 0.6 || out[0] < 0.4)
                transformSpeed(out[0]);
            else {
                xDir = 0;
                yDir = 0;
            }
            rotateAndResize((int) ((out[1] - 0.5) * 15));
            if (out[2] > 0.7)
                eat();
            //  System.out.println("OUT: "+xDir+"   "+yDir+" "+out[1]+"   "+out[2]);
        }
        if (getX() > 0 && getY() > 0 && getX() < World.CHUNK_SIZE * World.WORLD_WIDTH - getWidth() && getY() < World.CHUNK_SIZE * World.WORLD_HEIGHT - getHeight())
            setLocation(getX() + xDir, getY() + yDir);


    }

    /**
     * method that gives Birth to a child
     */
    private void makeChildren(int howmany) {
        for (int i = 0; i < howmany; i++) {
            Agent a = new CosiAgent(new Network(agent.getNet().getDescriptor()));
            a.getNet().mutateSoft((int) (Math.random() * 8));
            Bot child = new Bot(a, world, generation + 1);
            //create colors for child
            int newRed = (int) (red + Math.random() * 4 - 2);
            int newBlue = (int) (blue + Math.random() * 4 - 2);
            int newGreen = (int) (green + Math.random() * 4 - 2);
            if (newBlue > 255) {
                newBlue = 255;
            }
            if (newBlue < 0) {
                newBlue = 0;
            }
            if (newGreen > 255) {
                newGreen = 255;
            }
            if (newGreen < 0) {
                newGreen = 0;
            }
            if (newRed > 255) {
                newRed = 255;
            }
            if (newRed < 0) {
                newRed = 0;
            }
            child.red = newRed;
            child.blue = newBlue;
            child.green = newGreen;

            world.containerPanel.add(child, 0);
            world.population.add(child);
            child.setLocation(this.getX() + (int) (Math.random() * 60) - 30, this.getY() + (int) (Math.random() * 60) - 30);
            final Bot bf = child;
            bf.addMouseListener(World.listener);
        }
    }

    /**
     * Transforms the speed and sensor rotation into xDir and yDir
     *
     * @param speed of the direction
     */
    private void transformSpeed(double speed) {
        speed -= 0.5;
        speed *= prevChunkSize / 4;
        xDir = (int) (Math.round(speed * -Math.cos(Math.toRadians(sensorRotation))));
        yDir = (int) (Math.round(speed * -Math.sin(Math.toRadians(sensorRotation))));
    }


    /**
     * Eat the from the greatest Chunk
     */
    public void eat() {
        hp -= 2 * age +2;
        try {
            Chunk c = (Chunk) getChunkUnder(getX(), getY(), body);
            if (c != null && c.getType() == Chunk.Type.LAND) {
                hp += c.getFood();
                c.setFood(0);
                c.toUpdate = true;
                if (hp > MAX_HP)
                    this.kill();
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
    public Chunk getChunkUnder(int x, int y, JComponent component) {
        Chunk ret = null;
        if (this.getParent() != null) {
            if (!(getX() > 0 && getY() > 0 && getX() < World.CHUNK_SIZE * World.WORLD_WIDTH - getWidth() && getY() < World.CHUNK_SIZE * World.WORLD_HEIGHT - getHeight())) {
                ret = null;
            }
            if (x < -component.getX() || y < -component.getX()
                    || x + component.getX() + component.getWidth() > this.getParent().getWidth()
                    || y + component.getY() + component.getHeight() > this.getParent().getHeight()) {
                ret = null;
            } else {
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
                            ret = (Chunk) komponenten[i];
                            bestSize = size;

                        }
                    }
                    i++;
                }
            }
        }
        if (ret == null)
            try {
                return new Chunk(Chunk.Type.NONE, 0, 0);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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
        sensor_length = getWidth() / 2;
        try {
            Chunk c = (Chunk) (getChunkUnder(getX(), getY(), sensor));
            return c.getType() == Chunk.Type.LAND ? 1 : 0;
        } catch (Exception e) {
            return 0.5;
        }
    }

    /**
     * returns the sensor direction from -1 to 1
     */
    public double getSensorDir() {
        double temp = sensorRotation % 360;

        return (temp / 360) * 2 - 1;
    }

    /**
     * returns the health of the bot
     * 1 is the max health
     *
     * @return 0-1 the health of the bot
     */
    public double getHp() {
        if (hp >= MAX_HP / 2) {
            return 1;
        } else {
            return (hp) / (MAX_HP / 2);
        }
    }

    /**
     * returns a value from 0-1 which holds the information about how much food is under the sensor
     *
     * @return a value from 0-1
     */
    public double getFoodUnderSensor() {
        try {
            Chunk c = (Chunk) (getChunkUnder(getX(), getY(), sensor));
            return c.getType() == Chunk.Type.LAND ? c.getFood() / 100.0 : 0;
        } catch (Exception e) {
            return 0.5;
        }
    }

    /**
     * returns a value from 0-1 which holds the information about how much food is under the body
     *
     * @return a value from 0-1
     */
    public double getFoodUnderBody() {
        try {
            Chunk c = (Chunk) (getChunkUnder(getX(), getY(), body));
            return c.getType() == Chunk.Type.LAND ? c.getFood() / 100.0 : 0;
        } catch (Exception e) {
            return 0.5;
        }
    }

    /**
     * is a method which resize´s the bot relative to the World´s Chunk size
     */
    public void resizeAndRelocate() {
        double difference = (0.0 + World.CHUNK_SIZE) / prevChunkSize;
        sensor.width = World.CHUNK_SIZE / 4;
        sensor.height = sensor.width;
        body.width = World.CHUNK_SIZE / 2;
        body.height = body.width;
        sensor_length = World.CHUNK_SIZE * 2;
        setSize(sensor.getWidth() + body.getWidth() + sensor_length, sensor.getHeight() + body.getHeight() + sensor_length);
        body.setLocation(getWidth() / 2 - body.getWidth() / 2, getHeight() / 2 - body.getHeight() / 2);
        sensor_length = getWidth() / 2;

        setLocation((int) (getX() * difference), (int) (getY() * difference));

        prevChunkSize = World.CHUNK_SIZE;
    }
}
