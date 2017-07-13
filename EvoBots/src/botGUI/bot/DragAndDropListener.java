package botGUI.bot;

import botGUI.Chunk;
import botGUI.World;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Matteo Cosi
 * @since 10.07.2017
 */
public class DragAndDropListener extends MouseAdapter {


    int x, y;

    World w;

    public DragAndDropListener(World w) {
        this.w = w;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent() instanceof Bot) {
            Bot bf = (Bot) e.getComponent();
            if (w.selectedBot != null)
                w.selectedBot.selected = false;
            w.selectedBot = bf;
            bf.selected = true;
            w.controlPanel.removeAll();
            w.container.remove(w.controlPanel);
            w.controlPanel = new JPanel();
            w.controlPanel = w.addBotStats(bf);
            w.container.add(w.controlPanel,0);
        } else {
            if (w.selectedBot != null)
                w.selectedBot.selected = false;
            w.selectedBot = null;
            w.controlPanel.removeAll();
            w.container.remove(w.controlPanel);
            w.controlPanel = new JPanel();
            w.controlPanel = w.addControls();
            w.container.add(w.controlPanel,0);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getComponent() instanceof Bot||e.getComponent() instanceof Chunk) {
            e.translatePoint(w.containerPanel.getLocation().x - x, w.containerPanel.getLocation().y - y);
            w.containerPanel.setLocation(e.getX(), e.getY());
        } else {
            e.translatePoint(e.getComponent().getLocation().x - x, e.getComponent().getLocation().y - y);

            e.getComponent().setLocation(e.getX(), e.getY());
        }
    }

}
