package tdd.vendingMachine.display;

import org.junit.Test;
import tdd.vendingMachine.shelve.Shelve;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by okraskat on 06.02.16.
 */
public class DefaultDisplayFactoryTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenShelvesAreNull() throws Exception {
        //given
        DefaultDisplayFactory displayFactory = new DefaultDisplayFactory();
        //when
        displayFactory.createDisplay(null);
    }

    @Test
    public void shouldCreateDefaultDisplayInstance() throws Exception {
        //given
        DefaultDisplayFactory defaultDisplayFactory = new DefaultDisplayFactory();
        Map<Integer, Shelve> shelves = new HashMap<>();
        //when
        Display display = defaultDisplayFactory.createDisplay(shelves);
        //then
        assertNotNull(display);
        assertTrue(display instanceof DefaultDisplay);
    }

}
