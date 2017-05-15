package agent;

import gui.PlayGround;

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
        return ballX;
    }

    //0-1
    public double getBallX() {
        return ballX;
    }

    //0-1
    public double getBallY() {
        return ballY;
    }

    //0-1
    public double getPalayer1Y() {
        return palayer1Y;
    }
    //0-1
    public double getPlayer2Y() {
        return player2Y;
    }
}
