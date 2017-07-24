package flappyBirdTrainer;


import agent.cosi.CosiAgent;
import flappyBirdGUI.PlayGround;
import network.Network;
import network_gui.NetworkGUI;

import java.util.ArrayList;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class PopulationOrganizer {
    public static double[] bestDescriptor = null;
    public static ArrayList<double[]> goodParents = new ArrayList<>();
    public static int Generations = 2000;
    public static int population = 800;
    public static double bestFitness = -1;

    public static void main(String[] args) {
        NetworkGUI n = new NetworkGUI();
        for (int i = 1; i < Generations; i++) {
            try {
                CosiAgent[] agent = new CosiAgent[population];
                for (int j = 0; j < population; j++) {
                    if (i == 0) {
                        //first generation
                        int hiddAmm = (int) (Math.random() * 3)+1;
                        int[] hidd = new int[hiddAmm];
                        for (int k = 0; k < hiddAmm; k++) {
                            hidd[k] = (int) (Math.random() * 7 + 1);
                        }
                        agent[j] = new CosiAgent(new Network(3, 1, hiddAmm, hidd));
                    } else {
                        int neu = (int) (Math.random() * 7);
                        if (neu == (int) (Math.random() * 6) || neu == (int) (Math.random() * 6) || bestDescriptor == null) {
                            int hiddAmm = (int) (Math.random() * 3)+1;
                            int[] hidd = new int[hiddAmm];
                            for (int k = 0; k < hiddAmm; k++) {
                                hidd[k] = (int) (Math.random() * 7 + 1);
                            }
                            agent[j] = new CosiAgent(new Network(3, 1, hiddAmm, hidd));
                        } else if ((neu == (int) (Math.random() * 6)) && goodParents.size() > 1) {
                            agent[j] = new CosiAgent(new Network(goodParents.get((int) (Math.random() * goodParents.size()))));
                            agent[j].getNet().mutateSoft(neu, 0.2);
                        } else {
                            agent[j] = new CosiAgent(new Network(bestDescriptor));
                            agent[j].getNet().mutateSoft(neu, 0.2);
                        }
                    }
                }
                goodParents.clear();
                PlayGround p = new PlayGround(agent, i);
                while (!PlayGround.trainingOver)
                    Thread.sleep(100);

                double durchschnitt = 0;
                for (int j = 0; j < population; j++)
                    durchschnitt += agent[j].getFitness();
                durchschnitt /= population;


                for (int j = 0; j < population; j++) {
                    durchschnitt += agent[j].getFitness();
                    if (agent[j].getFitness() > bestFitness) {
                        n.addNetwork(agent[j].getNet());
                        n.repaint();
                        bestDescriptor = agent[j].getNet().getDescriptor();
                        bestFitness = agent[j].getFitness();
                        System.out.print("{");
                        for (int k = 0; k < bestDescriptor.length; k++) {
                            System.out.print(bestDescriptor[k] + ",");
                        }
                        System.out.println("}");
                        System.out.println(bestFitness);
                    } else if (agent[j].getFitness() > durchschnitt + 30 && (int) (Math.random() * 3) == 1) {
                        goodParents.add(agent[j].getNet().getDescriptor());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
