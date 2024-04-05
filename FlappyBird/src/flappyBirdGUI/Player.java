package flappyBirdGUI;

import agent.Agent;

/**
 * Created by mcosi on 09/06/2017.
 */
public class Player extends MovingObject {

    Vec2d fallspeed = new Vec2d(0, 0);
    long time = System.currentTimeMillis();

    public Agent agent = null;

    /**
     * Construktor for the {@link StaticObject}
     */
    public Player(Agent o) throws ClassNotFoundException {
        super("player.png");
        fallspeed.x = 0;
        agent = o;
    }


    @Override
    public void move() {
        super.move();
        if (getObjectAt(getX(), getY()) instanceof Wall || getY() < 6 || getY() > PlayGround.HEIGHT - getHeight()) {
            this.stirb();
        } else {
            if (agent != null) {
                try {
                    double data = agent.processData(new double[]{
                            getPlayerY(),  //Zum schnellen training
                            //getMyY(),          //langsamer
                            //PlayGround.spaceY / PlayGround.HEIGHT,//langsamer
                            //    PlayGround.SPACE/350,
                            getUnten(),
                            getAbstand(),
                            fallspeed.y / (1 + Math.abs(fallspeed.y)), // momentum (sigmoid)
                    })[0];

                    if (data > 0.7) {
                        jump();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("space y: " + PlayGround.spaceY + " unten " + getUnten() + " abstand " + getAbstand() + "  myY " + getMyY() + "  playerY " + getPlayerY() + "  Space " + PlayGround.SPACE / 350);
                }
            }
            fallspeed.y += 0.9 * ((System.currentTimeMillis() - time) / 1000.0);
        }
        if (getObjectAtWallZuerst(getX(), getY()) instanceof Wall) {
            this.stirb();
        }
        setLocation((int) (getX()), (int) (getY() + fallspeed.y));

    }


    public void jump() {
        fallspeed.y = -8;
        time = System.currentTimeMillis();
    }


    public double getPlayerY() {
        return (getY() - PlayGround.spaceY) / PlayGround.HEIGHT + 0.30;
    }

    public double getMyY() {
        return (getY()) / (PlayGround.HEIGHT + 0.0);
    }

    public double getAbstand() {
        return PlayGround.abstand;
    }

    public double getUnten() {
        return ((getY() + 2) / (PlayGround.HEIGHT + 0.0));
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
