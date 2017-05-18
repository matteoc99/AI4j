package game;

import javax.swing.*;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class Ball extends MobileObject {
    /**
     * Construktor for the {@link Ball}
     *
     * @param dateiname image to load in the {@link JComponent}
     */
    public Ball(String dateiname) throws ClassNotFoundException {
        super(dateiname);
    }

    @Override
    public void move() {
        super.move();
        //TODO kill ball when border-hit
        if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) == null) {
            changeLoc();
        } else {
            if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) instanceof Player) {
                Player p = (Player) getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y));
                //calculate offset
                int mid = p.getY() + p.getHeight() / 2;
                int ballMid = (int) position.y + getHeight() / 2;
                int offset = mid - ballMid;

                direction.y=(-offset/15);
                direction.x=-direction.x;

                this.move();
            } else {
                direction.x = -direction.x;
                if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) == null) {
                    changeLoc();
                } else {
                    direction.x = -direction.x;
                    direction.y = -direction.y;
                    if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) == null) {
                        changeLoc();
                    } else {
                        direction.x = -direction.x;
                        if (getObjectAt((int) (position.x + direction.x), (int) (position.y + direction.y)) == null) {
                            changeLoc();
                        }
                    }
                }
            }
        }
    }


}
