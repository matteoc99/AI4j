package flappyBirdTrainer;


import agent.cosi.CosiAgent;
import flappyBirdGUI.PlayGround;
import network.Network;
import network_gui.NetworkGUI;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class PopulationOrganizer {
    public static double[] bestDescriptor = null;
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
                        int hiddAmm = (int) (Math.random() * 3) + 1;
                        int[] hidd = new int[hiddAmm];
                        for (int k = 0; k < hiddAmm; k++) {
                            hidd[k] = (int) (Math.random() * 15 + 1);
                        }
                        agent[j] = new CosiAgent(new Network(5, 1, hiddAmm, hidd));
                    } else {
                        int neu = (int) (Math.random() * 6);
                        if (neu == (int) (Math.random() * 6) || neu == (int) (Math.random() * 6) || bestDescriptor == null) {
                            int hiddAmm = (int) (Math.random() * 3) + 1;
                            int[] hidd = new int[hiddAmm];
                            for (int k = 0; k < hiddAmm; k++) {
                                hidd[k] = (int) (Math.random() * 15 + 1);
                            }
                            agent[j] = new CosiAgent(new Network(5, 1, hiddAmm, hidd));
                        } else {
                            agent[j] = new CosiAgent(new Network(bestDescriptor));
                            agent[j].getNet().mutateSoft(neu);
                        }
                    }
                }

                PlayGround p = new PlayGround(agent, i);
                while (!PlayGround.trainingOver)
                    Thread.sleep(100);

                for (int j = 0; j < population; j++) {
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
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
