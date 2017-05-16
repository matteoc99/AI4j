package neuralNetworkLib;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Network is a class that combines the Layers and offers some utilities
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class Network {
    ArrayList<Layer> layers;

    private Network(){
        layers=new ArrayList<>();
    }
    private Network(ArrayList<Layer> layers){
        this();
        if (layers!=null)
        this.layers=layers;
    }
}
