package estfeller;

import network.Network;
import network_gui.NetworkGUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author Maximilian Estfelller
 * @since 09.06.2017
 */
public class Trainer {

    private static final int topCount = 1;
    private static final int trainCount = 1000;

    private static final Environment environment = new Environment();

    private static final ArrayList<Learner> agents = new ArrayList<>();

    private static Network bestNetwork;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        NetworkGUI gui = new NetworkGUI();
        environment.createData();
        createFirstWave();

        new Thread(() -> {
            while (true) {
                gui.addNetwork(bestNetwork);
                System.out.println(Arrays.toString(environment.getData()));
                System.out.println(Arrays.toString(bestNetwork.processData(environment.getData())));
                System.out.println(environment.evaluateActions(environment.getData(),
                        bestNetwork.processData(environment.getData())));
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            processAgents();
            filterAgents();
            copyAgents();
        }
    }

    private static void createFirstWave() {
        for (int i = 0; i < topCount; i++) {
            agents.add(new Learner(new Network(environment.environmentLength,
                    environment.environmentLength,
                    1,
                    new int[]{environment.environmentLength})));
        }
    }

    private static void processAgents() {
        double[] in = environment.getData();
        for (Learner agent : agents) {
            double[] out = agent.processData(in);
            agent.setFitness(environment.evaluateActions(in, out));
        }
    }

    private static void filterAgents() {
        agents.sort((o1, o2) -> (int)(o2.getFitness()-o1.getFitness()));
        while (agents.size() > topCount) {
            agents.remove(topCount);
        }
        bestNetwork = new Network(agents.get(0).getNet().getDescriptor());
        //System.out.println(agents.get(0).getFitness());
    }

    private static void copyAgents() {
        for (int i = 0; i < topCount; i++) {
            Agent agent = agents.get(i);
            agent.getNet().generateDescriptor();
            for (int j = 0; j < trainCount/topCount; j++) {
                double[] desc = agent.getNet().getDescriptor().clone();
                Utils.mutateDescriptor(desc);
                agents.add(new Learner(new Network(desc)));
            }
        }
    }
}
