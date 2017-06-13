package botGUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * @author Matteo Cosi
 * @since 15.05.2017
 */
public class ImmobileObject extends JComponent {
    /**
     * Image displayed on the {@link JComponent}
     */
    protected Image bild = null;
    /**
     * remembers if the Object is already death
     */
    protected boolean gestorben = false;

    /**
     * Construktor for the {@link ImmobileObject}
     *
     * @param dateiname image to load in the {@link JComponent}
     */
    public ImmobileObject(String dateiname) throws ClassNotFoundException {
        newimg(dateiname);

    }

    /**
     * Loads and draws a new Image on {@link ImmobileObject}
     *
     * @param dateiname {@link java.nio.file.Path} to where the image is lacated
     */
    public void newimg(String dateiname) throws ClassNotFoundException {
        URL url = this.getClass().getResource(dateiname);

        if (url == null)
            throw new ClassNotFoundException(dateiname + " not found");
        else {
            this.bild = getToolkit().getImage(url);
            prepareImage(bild, this);
            Thread t = Thread.currentThread();
            while ((checkImage(bild, this) & PROPERTIES) != PROPERTIES) {
                try {
                    t.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("SO");

                }
            }
            this.setSize(this.bild.getWidth(this), this.bild.getHeight(this));
        }
        repaint();
    }


    /**
     * Method, which draws the image on the {@link JComponent}
     */
    public void paint(Graphics g) {
        if (bild != null)
            g.drawImage(this.bild, 0, 0, this);
    }

    /**
     * Object dies and removes itself from the Container where it was previously in.
     */
    public void stirb() {
        if (!this.gestorben && this.getParent() != null) {
            this.getParent().remove(this);
            this.gestorben = true;
        }


    }

    /**
     * returns if the Object is dead
     *
     * @return true is the Object is dead, otherwise false
     */
    public boolean getGestorben() {
        return this.gestorben;
    }


    /**
     * Controls if this Object collides with another Object at the given x and y coordinates
     *
     * @param x coordinate
     * @param y coordinate
     * @return null, if there is no Object, otherwise the Component
     */
    public Component getObjectAt(int x, int y) {
        Component ret = null;
        if (this.getParent() != null) {
            // Kontrolliere ob neue Position auï¿½erhalb des Frames liegt
            if (x < 0 || y < 0 || x + this.getWidth() > this.getParent().getWidth() ||
                    y + this.getHeight() > this.getParent().getHeight())
                // In diesem Fall wird der contentPane des Formulars ï¿½bergeben
                ret = this.getParent();
            else {
                // Kontrolliere ob sich die neue Position mit anderen Objekten ï¿½berdeckt
                Rectangle neuePosition =
                        new Rectangle(x, y, this.getWidth(), this.getHeight());
                // Gehe alle Objekte des Formulars durch und vergleiche ihre Position mit der
                // neuen Position
                Component[] komponenten;
                komponenten = this.getParent().getComponents();
                int i = 0;
                while (komponenten != null && i < komponenten.length && ret == null) {
                    // Wenn das Objekt nicht das zu kontrollierende Objekt ist und das Objekt
                    // mit dem zu Kontrollierendem zusammenfï¿½llt
                    if (komponenten[i] != this &&
                            neuePosition.intersects(komponenten[i].getBounds()))
                        ret = komponenten[i];
                    i++;
                }
            }
        }
        return ret;
    }
}