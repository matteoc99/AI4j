package game;

import javax.swing.*;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class Ball extends MobileObject {

    PlayGround playGround;

    /**
     * Construktor for the {@link Ball}
     *
     * @param dateiname image to load in the {@link JComponent}
     */
    public Ball(String dateiname, PlayGround playGround) throws ClassNotFoundException {
        super(dateiname);
        this.playGround = playGround;
    }

    @Override
    public void move() {
        super.move();
        if (getX() < 10) {
            stirb();
            playGround.dispose();
            playGround.pointsPlayerRight++;
            new PlayGround(null);
        }
        if (getX() > 1020) {
            stirb();
            playGround.dispose();
            playGround.pointsPlayerLeft++;
            new PlayGround(null);
        }
        if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) == null
                || getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) instanceof JLabel) {
            changeLoc();
        } else {
            if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) instanceof Player) {
                Player p = (Player) getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y));
                //calculate offset
                int mid = p.getY() + p.getHeight() / 2;
                int ballMid = (int) position.y + getHeight() / 2;
                int offset = mid - ballMid;

                direction.y = (-offset / 10);
                direction.x=-direction.x;

                this.move();
            } else {
                direction.x = -direction.x;
                if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) == null
                        || getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) instanceof JLabel) {
                    changeLoc();
                } else {
                    direction.x = -direction.x;
                    direction.y = -direction.y;
                    if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) == null
                            || getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) instanceof JLabel) {
                        changeLoc();
                    } else {
                        direction.x = -direction.x;
                        if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) == null
                                || getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) instanceof JLabel) {
                            changeLoc();
                        }
                    }
                }
            }
        }
    }
}