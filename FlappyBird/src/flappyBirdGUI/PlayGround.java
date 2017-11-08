package flappyBirdGUI;

import agent.Agent;
import agent.cosi.CosiAgent;
import network.Network;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by mcosi on 09/06/2017.
 */
public class PlayGround extends JFrame {
    public static boolean trainingOver = false;

    //FPS control
    private static int FPS = 60;
    private long timeUntilSleep;

    private Container c;

    public static double spaceY = 0.5;

    public static int HEIGHT = 820;
    public static int WIDTH = 1080;
    public static int SPACE = 350;

    public static double abstand = 0.2;
    //in ms
    public static int WALLTIMEOUT = 180;
    public static long lastWall = System.currentTimeMillis();

    public JLabel punkte = new JLabel("Punkte:");
    public JLabel highscore = new JLabel("Punkte:");
    public int punkteInt = 0;

    Agent[] agents;
    Player p[] = null;

    private int WallCounter = 300;

    public PlayGround(Agent[] agents, int gen) {
        SPACE = 350 - gen;
        if (SPACE < 100)
            SPACE = 100;
        this.agents = agents;
        trainingOver = false;
        setTitle("Flappy Bird Generation: " + gen);
        setBounds(0, 0, WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        c = getContentPane();
        c.setLayout(null);

        c.setBackground(Color.WHITE);
        if (agents == null) {
            p = new Player[1];
            try {
                p[0] = new Player(null);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            p[0].setLocation(100, 400);
            c.add(p[0]);
        } else {
            p = new Player[agents.length];
            for (int i = 0; i < agents.length; i++) {
                try {
                    p[i] = new Player(agents[i]);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                p[i].setLocation(100, 400);
                c.add(p[i]);
            }
        }
        punkte.setBounds(500, 50, 150, 100);
        highscore.setBounds(500, 100, 150, 100);

        Player[] finalP = p;
        for (int i = 0; i < finalP.length; i++) {
            int finali = i;
            finalP[finali].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    finalP[finali].stirb();
                }
            });
        }
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {

                    case KeyEvent.VK_UP:
                        finalP[0].jump();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        requestFocus();
        c.add(punkte);
        c.add(highscore);
        setVisible(true);
        repaint();
    }

    public PlayGround(double[] doubles, int gen, int t) {
        this(new CosiAgent[]{new CosiAgent(new Network(doubles))}, gen);
    }


    @Override
    public void paint(Graphics g) {
        timeUntilSleep = System.currentTimeMillis();

        if (WallCounter > WALLTIMEOUT) {
            WallCounter = 0;
            int ran = (int) (Math.random() * 450 + 50);
            if (SPACE > 150) {
                SPACE -= 5;
            }
            int space = SPACE;

            Wall w = null;
            try {
                w = new Wall(100, ran);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            w.setLocation(WIDTH - 5, 0);
            Wall.allowedWall = w;
            spaceY = (w.getHeight() + space / 2);

            Wall w2 = null;
            try {
                w2 = new Wall(100, HEIGHT - ran - space);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            w2.setLocation(WIDTH, w.getHeight() + space);
            c.add(w2);
            c.add(w);
            lastWall = System.currentTimeMillis();
            highscore.setText("Punkte: " + ++punkteInt);
        } else
            WallCounter++;

        int count = 0;
        for (int i = 0; i < p.length; i++) {
            if (!p[i].getGestorben()) {
                if (agents != null && p[i].agent != null)
                    p[i].agent.increaseFitness();
                count++;
            }
        }
        punkte.setText("Count: " + count);
        if (count == 0) {
            trainingOver = true;
            dispose();
        }

        Component[] components = this.getContentPane().getComponents();
        if (components != null && components.length > 0) {
            for (Component c : components) {
                if (c instanceof MovingObject)
                    ((MovingObject) c).move();
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
        new PlayGround(null, 0);
    }
}
