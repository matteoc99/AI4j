package agent;

import game.PlayGround;

/**
 * @author Matteo Cosi
 * @since 15.04.2017
 */
public class AgentInteface {


    private int playGroundHeight;
    private int playGroundWidth;

    PlayGround playGround;

    public AgentInteface(PlayGround playGround) {
        this.playGround = playGround;
        playGroundWidth= playGround.getWidth();
        playGroundHeight = playGround.getHeight();

    }


    //int 0-360
    public int getBallDir() {
        int ret=0;

        return ret;
    }

    //0-1
    public double getBallX() {
        return playGround.ball.getX()/(double)playGroundWidth;
    }

    //0-1
    public double getBallY() {
        return playGround.ball.getY()/(double)playGroundHeight;
    }

    //0-1
    public double getPalayer1Y() {
        return playGround.leftPlayer.getY();
    }
    //0-1
    public double getPlayer2Y() {
        return playGround.rightPlayer.getX();
    }
}
