package neural_network_lib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author Matteo Cosi
 * @since 21.05.2017
 */
public class ConnectionTest {


    @Test
    public void isActive() {
        Neuron from = new Neuron(1);
        Neuron to = new Neuron(2);
        Connection c = new Connection(from, to);
        assertEquals(c.isActive(), true);
    }

    @Test
    public void setActive() {
        Neuron from = new Neuron(1);
        Neuron to = new Neuron(2);
        Connection c = new Connection(from, to);
        assertEquals(c.isActive(), true);
        c.setActive(false);
        assertEquals(c.isActive(), false);
    }

    @Test
    public void setWeight() {
        Neuron from = new Neuron(1);
        Neuron to = new Neuron(2);
        Connection c = new Connection(from, to);
        assertEquals(c.isActive(), true);
        c.setWeight(3);
        assertEquals(3, c.getWeight());
        c.setWeight(-3);
        assertEquals(-3, c.getWeight());
        c.setWeight(0);
        assertEquals(0, c.getWeight());
    }

    @Test
    public void setFrom() {
        Neuron from = new Neuron(1);
        Neuron to = new Neuron(2);
        Connection c = new Connection(from, to);
        Neuron from2 = new Neuron(3);
        c.setFrom(from2);
        assertNotSame(from2,from);
        assertEquals(from2,c.getFrom());
        assertNotSame(from,c.getFrom());
    }

    @Test
    public void setTo() {
        Neuron from = new Neuron(1);
        Neuron to = new Neuron(2);
        Connection c = new Connection(from, to);
        Neuron to2 = new Neuron(3);
        assertNotSame(to,to2);
        c.setTo(to2);
        assertEquals(to2,c.getTo());
        assertNotSame(to,c.getTo());
    }

    @Test
    public void send() {
        Neuron from = new Neuron(1);
        Neuron to = new Neuron(2);
        Connection c = new Connection(from, to);
        assertEquals(to.getValue(),to.getFunction().calculate(0));
        c.send(3);
        c.send(3);
        c.send(3);
        assertEquals(to.getFunction().calculate(9.0*c.getWeight()),to.getValue());
    }
}
