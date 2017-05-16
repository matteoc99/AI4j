package agent.cosi;

import agent.Agent;
import neuralNetworkLib.Network;

/**
 * Created by matte on 15.05.2017.
 */
public class CosiAgent extends Agent{

    public static void main(String[] args){
        Network n = Network.createDFF(3,4,2,3);
       double[] d= n.processData(new double[]{0.3,0.2,0.3});
        for (int i = 0; i < d.length; i++) {
            System.out.println(d[i]);
        }
    }
}
