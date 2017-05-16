package neuralNetworkLib;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by matte on 15.05.2017.
 */
public class Network {

    private LinkedList<Layer> layers;

    public Network(LinkedList<Layer> layers) {
        this.layers = layers;
    }

    public Network() {
        this(new LinkedList<>());
    }

    public void addLayer(@NotNull Layer layer) {
        if (layer != null) {
            //  if (!layers.isEmpty();
            //    layer.setInputCount(layers.g)
            layers.add(layer);
        }
    }
}
