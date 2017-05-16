package game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by matte on 15.05.2017.
 */
public class PlayGround extends JFrame {

    //FPS control
    public static final int FPS = 80;
    public long zeitvorsleep;

    public PlayGround() {
        setTitle("Pong game");
        setBounds(0, 0, 1080, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        zeitvorsleep = System.currentTimeMillis();

        Component[] komponenten = this.getContentPane().getComponents();
        if (komponenten != null && komponenten.length > 0) {
            for (int i = 0; i < komponenten.length; i = i + 1) {
                //TODO take care of the move method
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
        // System.out.println((long) (1000.0/FPS - zeitvergangen));

        repaint();
    }

    public static void main(String args[]) {
        new PlayGround();
    }
}
