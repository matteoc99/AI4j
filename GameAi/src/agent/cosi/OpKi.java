package agent.cosi;

import agent.Agent;
import network.Network;

/**
 * @author Matteo Cosi
 * @since 25.05.2017
 */
public class OpKi extends Agent {
    public OpKi(Network net) {
        super(net);
    }

    public double[] processData(double[] in) {
        if (in[0] > in[1])
            return new double[]{-1};
        else
            return new double[]{1};
    }

}
