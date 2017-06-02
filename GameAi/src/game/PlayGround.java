package game;

import agent.Agent;
import agent.cosi.CosiAgent;
import agent.cosi.OpKi;
import agent.est.EstAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class PlayGround extends JFrame {

    public static boolean trainingOver = false;

    public enum Mode {
        GRAPHIC, CALC_ONLY
    }

    //FPS control
    private static final int FPS = 120;
    private long timeUntilSleep;

    public Player leftPlayer[], rightPlayer[];
    public Ball ball;

    public JLabel points = new JLabel();
    public static int pointsPlayerLeft = 0, pointsPlayerRight = 0;
    private Container c;

    static Mode mode = Mode.GRAPHIC;

    public static int HEIGHT = 720;
    public static int WIDTH = 1080;

    public PlayGround(Agent[] agentsLeft, Agent[] agentsRigth) {
        trainingOver = false;
        setTitle("Pong game");
        setBounds(0, 0, WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        c = getContentPane();
        c.setLayout(null);
        points.setText(pointsPlayerLeft + " : " + pointsPlayerRight);
        points.setBounds(500, 0, 100, 30);
        c.add(points);

        try {
            ball = new Ball("res/ball.png", this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ball.setLocation(500, 300);
        c.add(ball);

        if (agentsRigth == null) {
            agentsRigth = new Agent[1];
            agentsRigth[0] = new OpKi(null);
        }
        if (agentsLeft == null) {
            agentsLeft = new Agent[1];
            agentsLeft[0] = new OpKi(null);
        }


        leftPlayer = new Player[agentsLeft.length];
        for (int i = 0; i < agentsLeft.length; i++) {
            try {
                leftPlayer[i] = new Player("res/player.png", agentsLeft[i], ball);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            leftPlayer[i].setLocation(20, 250);
            c.add(leftPlayer[i]);
        }


        rightPlayer = new Player[agentsRigth.length];
        for (int i = 0; i < rightPlayer.length; i++) {
            try {
                rightPlayer[i] = new Player("res/player.png", agentsRigth[i], ball);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            rightPlayer[i].setLocation(1040, 250);
            c.add(rightPlayer[i]);
        }


        //TODO replace with agents
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        leftPlayer[0].direction.y = -6;
                        break;
                    case KeyEvent.VK_S:
                        leftPlayer[0].direction.y = +6;
                        break;
                    case KeyEvent.VK_UP:
                        rightPlayer[0].direction.y = -6;

                        break;
                    case KeyEvent.VK_DOWN:
                        rightPlayer[0].direction.y = +6;

                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        leftPlayer[0].direction.y = 0;
                        break;
                    case KeyEvent.VK_S:
                        leftPlayer[0].direction.y = 0;
                        break;
                    case KeyEvent.VK_UP:
                        rightPlayer[0].direction.y = 0;
                        break;
                    case KeyEvent.VK_DOWN:
                        rightPlayer[0].direction.y = 0;
                        break;
                }
            }
        });
        requestFocus();
        if (mode == Mode.GRAPHIC) {
            setVisible(true);
            repaint();
        } else
            calcOnly();
    }

    private void calcOnly() {
        timeUntilSleep = System.currentTimeMillis();

        Component[] components = this.getContentPane().getComponents();
        if (components != null && components.length > 0) {
            for (Component c : components) {
                if (c instanceof MobileObject) {
                    ((MobileObject) c).move();
                }
            }
        }
        // FPS control
        long passedTime = System.currentTimeMillis() - timeUntilSleep;
        if (passedTime < 1000.0 / FPS) {
            try {
                Thread.sleep((long) (1000.0 / FPS - passedTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        calcOnly();
    }

    @Override
    public void paint(Graphics g) {
        timeUntilSleep = System.currentTimeMillis();

        Component[] components = this.getContentPane().getComponents();
        if (components != null && components.length > 0) {
            for (Component c : components) {
                if (c instanceof MobileObject)
                    ((MobileObject) c).move();
            }
        }
        super.paint(g);

        // FPS control
        long passedTime = System.currentTimeMillis() - timeUntilSleep;
        if (passedTime < 1000.0 / FPS) {
            try {
                Thread.sleep((long) (1000.0 / FPS - passedTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        repaint();
    }

    public static void main(String args[]) {
        new PlayGround(null, null);
    }
}
