package game;

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

    //FPS control
    private static final int FPS = 120;
    private long zeitvorsleep;

    private Player leftPlayer, rightPlayer;
    private Ball ball;

    private Container c;

    public PlayGround() {
        setTitle("Pong game");
        setBounds(0, 0, 1080, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        c = getContentPane();
        c.setLayout(null);
        //create the Objects
        try {
            leftPlayer = new Player("res/player.png", null);
            rightPlayer = new Player("res/player.png", null);
            ball = new Ball("res/ball.png");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //locate the Objects
        leftPlayer.setLocation(20, 250);
        rightPlayer.setLocation(1000, 250);
        ball.setLocation(500, 300);

        //add the Objects to the Container
        c.add(ball);
        c.add(leftPlayer);
        c.add(rightPlayer);


        //TODO replace with agents
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        leftPlayer.direction.y = -leftPlayer.MAX_SPEED;
                        break;
                    case KeyEvent.VK_S:
                        leftPlayer.direction.y = +leftPlayer.MAX_SPEED;
                        break;
                    case KeyEvent.VK_UP:
                        rightPlayer.direction.y = -rightPlayer.MAX_SPEED;

                        break;
                    case KeyEvent.VK_DOWN:
                        rightPlayer.direction.y = +rightPlayer.MAX_SPEED;

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
        setVisible(true);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        zeitvorsleep = System.currentTimeMillis();

        Component[] komponenten = this.getContentPane().getComponents();
        if (komponenten != null && komponenten.length > 0) {
            for (int i = 0; i < komponenten.length; i = i + 1) {
                Component c = komponenten[i];
                if (c instanceof MobileObject)
                    ((MobileObject) c).move();
            }
        }
        super.paint(g);

        // FPS control
        long zeitvergangen = (long) (System.currentTimeMillis() - zeitvorsleep);
        if (zeitvergangen < 1000.0 / FPS) {
            try {
                Thread.sleep((long) (1000.0 / FPS - zeitvergangen));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        repaint();
    }

    public static void main(String args[]) {
        new PlayGround();
    }
}
