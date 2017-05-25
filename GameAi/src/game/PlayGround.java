package game;

import agent.Agent;

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

    public enum Mode {
        GRAPHIC, CALC_ONLY
    }

    //FPS control
    private static final int FPS = 120;
    private long timeUntilSleep;

    public Player leftPlayer, rightPlayer;
    public Ball ball;

    public JLabel points = new JLabel();
    public static int pointsPlayerLeft = 0, pointsPlayerRight = 0;
    private Container c;

    static Mode mode = Mode.GRAPHIC;

    public PlayGround(Agent[] agents) {
        setTitle("Pong game");
        setBounds(0, 0, 1080, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        c = getContentPane();
        c.setLayout(null);
        points.setText(pointsPlayerLeft + " : " + pointsPlayerRight);

        points.setBounds(500, 0, 100, 30);
        c.add(points);
        //create the Objects
        try {
            leftPlayer = new Player("res/player.png", null);
            rightPlayer = new Player("res/player.png", null);
            ball = new Ball("res/ball.png", this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //locate the Objects
        leftPlayer.setLocation(20, 250);
        rightPlayer.setLocation(1040, 250);
        ball.setLocation(500, 300);

        //add the Objects to the Container
        c.add(ball);
        c.add(leftPlayer);
        c.add(rightPlayer);
        System.out.println(pointsPlayerLeft + " : " + pointsPlayerRight);

        //TODO replace with agents
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        leftPlayer.direction.y = -6;
                        break;
                    case KeyEvent.VK_S:
                        leftPlayer.direction.y = +6;
                        break;
                    case KeyEvent.VK_UP:
                        rightPlayer.direction.y = -6;

                        break;
                    case KeyEvent.VK_DOWN:
                        rightPlayer.direction.y = +6;

                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        leftPlayer.direction.y = 0;
                        break;
                    case KeyEvent.VK_S:
                        leftPlayer.direction.y = 0;
                        break;
                    case KeyEvent.VK_UP:
                        rightPlayer.direction.y = 0;
                        break;
                    case KeyEvent.VK_DOWN:
                        rightPlayer.direction.y = 0;
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
        new PlayGround(null);
    }
}
