package network_gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author Maximilian Estfelller
 * @since 14.06.2017
 */
class MovableComponent extends JComponent{
    Point onClickP = new Point(0,0);

    MovableComponent() {
        addMouseListener(new MCMouseListener());
        addMouseMotionListener(new MCMouseMotionListener());
    }

    private class MCMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            onClickP = e.getPoint();
        }
    }

    private class MCMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            Container parent = getParent();
            if (parent == null || !(parent instanceof MovePanel))
                return;
            Point raw = e.getLocationOnScreen();
            Point relativeParent = parent.getLocationOnScreen();
            Point total = new Point((int)(raw.getX()-relativeParent.getX()-onClickP.getX()),
                    (int)(raw.getY()-relativeParent.getY()-onClickP.getY()));

            ((MovePanel) parent).requestMove(MovableComponent.this, total);
        }
    }
}
