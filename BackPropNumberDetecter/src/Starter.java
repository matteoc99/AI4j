import network.Network;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Matteo Cosi
 * @since 08.08.2017
 */
public class Starter extends JFrame {

    Led[] leds = new Led[7];
    JTextArea output = new JTextArea();

    Container c;

    JButton train = new JButton("TRAIN");
    JButton guess = new JButton("GUESS");

    public double[][] data = {{1, 1, 1, 0, 1, 1, 1}, {0, 0, 1, 0, 0, 1, 0},
            {1, 0, 1, 1, 1, 0, 1}, {1, 0, 1, 1, 0, 1, 1},
            {0, 1, 1, 1, 0, 1, 0}, {1, 1, 0, 1, 0, 1, 1},
            {1, 1, 0, 1, 1, 1, 1}, {1, 0, 1, 0, 0, 1, 0},
            {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 0, 1, 1}};
    public double[][] expected = {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    };

    Network net = Network.createDFF(7, 10, 1, 250);


    public Starter() {
        setTitle("Number Guesser");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0, 0, 680, 680);
        setLocationRelativeTo(null);

        c = getContentPane();
        c.setLayout(null);

        output.setBounds(440, 20, 200, 400);
        output.setEditable(false);
        output.setFont(new Font("Times New Roman", 0, 25));
        c.add(output);

        train.setBounds(440, 460, 80, 50);
        train.setBackground(Color.ORANGE);
        c.add(train);

        guess.setBounds(560, 460, 80, 50);
        guess.setBackground(Color.ORANGE);
        c.add(guess);


        for (int i = 0; i < leds.length; i++) {
            leds[i] = new Led();
            if (i == 0 || i == 3 || i == 6) {
                leds[i].setBounds(100, i / 3 * 200 + 40 * (int) Math.ceil(i / 3.0), 200, 80);
            } else {
                if (i == 1 || i == 4) {
                    leds[i].setBounds(0, (i - 1) / 3 * 260 + 40, 80, 200);
                } else {
                    leds[i].setBounds(320, (i - 2) / 3 * 260 + 40, 80, 200);
                }
            }
            leds[i].setEditable(false);
            leds[i].setBackground(Color.BLACK.brighter());
            c.add(leds[i]);
            final int fi = i;
            leds[fi].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    leds[fi].toggle();
                }
            });
        }
        train.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int iterations = 1;
                double rate = 0.02;
                while (iterations < 5000) {
                    for (int i = 0; i < data.length; i++) {
                        double[] y = net.processData(data[i]);
                        double[] error = new double[expected[i].length];
                        for (int j = 0; j < error.length; j++) {
                            error[j] = expected[i][j] - y[j];
                        }
                        net.propagateBack(rate, error);
                    }
                    iterations++;
                    if (iterations % 50 == 0) {
                        output.setText(iterations / 50 + "%");
                        output.update(output.getGraphics());
                    }
                }
                System.out.print("{");
                for (int i = 0; i < net.getDescriptor().length; i++) {
                    System.out.print(", " + net.getDescriptor()[i]);
                }
                System.out.print("}\n");
            }

        });

        guess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double[] data = new double[7];
                for (int i = 0; i < data.length; i++) {
                    data[i] = leds[i].clicked;
                }
                output.setText("");
                double[] ans = net.processData(data);

                for (int i = 0; i < 10; i++) {
                    int index = 0;
                    double best = 0;
                    for (int j = 0; j < ans.length; j++) {
                        if (best < ans[j]) {
                            best = ans[j];
                            index = j;
                        }
                    }
                    output.append((int) (ans[index] * 100) + "%  " + index + "\n");
                    ans[index] = -1;
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Starter();
    }
}
