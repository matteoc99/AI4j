package field;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Maximilian Estfelller
 * @since 18.07.2017
 */
public class FieldSection {

    private int posX = -1;
    private int posY = -1;

    private List<WallFunction> walls = new ArrayList<>();

    FieldSection(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    void addWall(WallFunction wallFunction) {
        walls.add(wallFunction);
    }
}
