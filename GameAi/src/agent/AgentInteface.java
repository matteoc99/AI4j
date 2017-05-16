package agent;

import game.PlayGround;

/**
 * Created by matte on 15.05.2017.
 */
public class AgentInteface {


    private int playGroundHeigth;
    private int playGroundWidth;

    PlayGround playGround;

    public AgentInteface(PlayGround playGround) {
        this.playGround = playGround;
        playGroundWidth= playGround.getWidth();
        playGroundHeigth= playGround.getHeight();

    }


    //int 0-360
    public int getBallDir() {
        int ret=0;

        return ret;
    }

    //0-1
    public double getBallX() {
        int ret=0;
        return ret;
    }

    //0-1
    public double getBallY() {
        int ret=0;
        return ret;
    }

    //0-1
    public double getPalayer1Y() {
        int ret=0;
        return ret;
    }
    //0-1
    public double getPlayer2Y() {
        int ret=0;
        return ret;
    }
}
