package flappyBirdTrainer;


import agent.cosi.CosiAgent;
import flappyBirdGUI.PlayGround;
import network.Network;
import network_gui.NetworkGUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class PopulationOrganizer {
    public static double[] bestDescriptor = null;
    public static ArrayList<double[]> goodParents = new ArrayList<>();
    public static int Generations = 500;
    public static int population = 800;
    public static double bestFitness = -1;

    public static Random random = new Random();

    private static CosiAgent makeRandomAgent(int lmax, int nmax) {
        int hiddAmm = (int) (random.nextDouble() * lmax) + 1;
        int[] hidd = new int[hiddAmm];
        for (int k = 0; k < hiddAmm; k++) {
            hidd[k] = Math.max(3, (int) (random.nextDouble() * nmax + 1));
        }
        return new CosiAgent(new Network(4, 1, hiddAmm, hidd));
    }
    public static void main(String[] args) {
        NetworkGUI n = new NetworkGUI();
        CosiAgent[] agents = new CosiAgent[population];

        for (int j = 0; j < population; j++) {
            agents[j] = makeRandomAgent(3, 6);
        }

        for (int i = 0; i < Generations; i++) {
            try {
                goodParents.clear();
                PlayGround ignored = new PlayGround(agents, i);
                while (!PlayGround.trainingOver)
                    Thread.sleep(100);

                double[] fitsSorted = new double[population];
                for (int j = 0; j < population; j++)
                    fitsSorted[j] = agents[j].getFitness();
                Arrays.sort(fitsSorted);
                double average = Arrays.stream(fitsSorted).sum() / population;
                double median;
                if (fitsSorted.length % 2 == 0)
                    median = ((double)fitsSorted[fitsSorted.length/2] + (double)fitsSorted[fitsSorted.length/2 - 1])/2;
                else
                    median = (double) fitsSorted[fitsSorted.length/2];

                for (int j = 0; j < population; j++) {
                    if (agents[j].getFitness() > bestFitness) {
                        n.addNetwork(agents[j].getNet(), String.format("Record Bird %d.%d -> %f", i, j, agents[j].getFitness()));
                        n.repaint();
                        bestDescriptor = agents[j].getNet().getDescriptor();
                        bestFitness = agents[j].getFitness();
                        System.out.print("{");
                        for (int k = 0; k < bestDescriptor.length; k++) {
                            System.out.print(bestDescriptor[k] + ",");
                        }
                        System.out.println("}");
                        System.out.println(bestFitness);
                    }
                    if (agents[j].getFitness() > average + 500 || agents[j].getFitness() > average * 1.5) {
                        goodParents.add(agents[j].getNet().getDescriptor());
                    }
                }

                int fromNew = 0;
                int fromParent = 0;
                int fromBest = 0;
                int mutated = 0;
                for (int j = 0; j < population; j++) {
                    int rnd = (int) (random.nextDouble() * 6) + 1;
                    if ((agents[j].getFitness() <= median || agents[j].getFitness() <= average) || bestDescriptor == null) {
                        if (rnd > 4 && !goodParents.isEmpty()){
                            agents[j] = new CosiAgent(new Network(goodParents.get((int) (random.nextDouble() * goodParents.size()))));
                            agents[j].getNet().mutateSoft((int) (random.nextDouble() * 3) + 1, 0.2);
                            fromParent++;
                        } else if (rnd > 1 && bestDescriptor != null) {
                            agents[j] = new CosiAgent(new Network(bestDescriptor));
                            agents[j].getNet().mutateSoft((int) (random.nextDouble() * 6) + 1, 0.2);
                            fromBest++;
                        } else {
                            agents[j] = makeRandomAgent(2, 6);
                            fromNew++;
                        }
                    } else {
                        agents[j] = new CosiAgent(agents[j].getNet());
                        agents[j].getNet().mutateSoft((int) (random.nextDouble() * 3) + 1, 0.1);
                        mutated++;
                    }
                }

                System.out.printf("AVG(%f) MED(%f) | Modified networks: new-%d, parent-%d, best-%d, mutated-%d%n",
                        average, median, fromNew, fromParent, fromBest, mutated);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
