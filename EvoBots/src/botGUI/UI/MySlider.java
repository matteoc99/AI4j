package botGUI.UI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matteo Cosi
 * @since 06.08.2017
 */
public class MySlider extends JSlider {

    public MySlider(int orientation, int min, int max, int value) {
        super(orientation, min, max, value);
        setPaintLabels(true);
        setBackground(Color.white);
        setPaintTicks(true);
        JSliderUI ui = new JSliderUI(this);
        setUI(ui);
    }
}
