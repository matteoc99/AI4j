package field;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 */
public class FieldSection {

    private List<WallFunction> walls = new ArrayList<>();

    void addWall(WallFunction wallFunction) {
        walls.add(wallFunction);
    }
}
