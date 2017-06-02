package agent.cosi;

import agent.Agent;
import game.PlayGround;
import game.Player;
import neural_network_lib.Network;
import neural_network_lib.Neuron;
import neural_network_lib.Utils;
import neural_network_lib.network_gui.NetworkGUI;

import java.util.ArrayList;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class PopulationOrganizer {
    public static double[] bestDescriptor;

    public static int Generations = 5000;
    public static int population = 1000;
    public static double bestFitness = -1;

    public static ArrayList<Agent> bests = new ArrayList<>();

    public static void main(String[] args) {
        //NetworkGUI n = new NetworkGUI();
        for (int i = 0; i < Generations; i++) {
            try {
                System.out.println(i);
                CosiAgent[] agent = new CosiAgent[population];
                for (int j = 0; j < population; j++) {
                    //two rand parents
                    int p1 = 0, p2 = 0;
                    if (bests != null) {
                        p1 = (int) (Math.random() * bests.size());
                        p2 = (int) (Math.random() * bests.size());
                    }
                    if (i == 0) {
                        //first generation
                        agent[j] = new CosiAgent(Network.createDFF(2, 1, 2, 3));
                    } else {
                        int ran = (int) (Math.random() * 8);
                        if (ran == 7) {
                            agent[j] = new CosiAgent(Network.createDFF(2, 1, 2, 3));
                        } else if (ran == 6 || ran == 5) {
                            agent[j] = new CosiAgent(new Network(bestDescriptor));
                            agent[j] = new CosiAgent(new Network(Utils.mutate(agent[j].getNet().getDescriptor())));
                        } else {
                            if (bests.size() > 0) {
                                agent[j] = new CosiAgent(new Network(Utils.mutate(Utils.crossOver(bests.get(p1).getNet().getDescriptor(), bests.get(p2).getNet().getDescriptor())[0])));
                                j++;
                                agent[j] = new CosiAgent(new Network(Utils.mutate(Utils.crossOver(bests.get(p1).getNet().getDescriptor(), bests.get(p2).getNet().getDescriptor())[1])));
                            } else {
                                agent[j] = new CosiAgent(Network.createDFF(2, 1, 2, 3));
                            }
                        }
                    }
                }

                PlayGround p = new PlayGround(agent, null);

                while (!PlayGround.trainingOver) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                bests = new ArrayList<>();
                for (int j = 0; j < population; j++) {
                    if (agent[j].getFitness() >= bestFitness) {
                        //n.addNetwork(agent[j].getNet());
                        //n.repaint();
                        bestDescriptor = agent[j].getNet().getDescriptor();
                        bestFitness = agent[j].getFitness();
                        bests.add(agent[j]);
                        System.out.print("{");
                        for (int k = 0; k < bestDescriptor.length; k++) {
                            System.out.print(bestDescriptor[k] + ",");
                        }
                        System.out.println("}");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
