import javax.swing.*;
import java.awt.*;

/**
 * @author Matteo Cosi
 * @since 08.08.2017
 */
public class Led extends JTextField {

    int clicked = 0;

    public void toggle() {
        if (clicked == 0) {
            clicked = 1;
        } else {
            clicked = 0;
        }
        refreshColor();
    }

    public void refreshColor() {
        if (clicked == 0) {
            setBackground(Color.BLACK);

        } else {
            setBackground(Color.YELLOW);
        }
    }
}
