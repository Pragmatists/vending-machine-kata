package tdd.vendingMachine.display;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by okraskat on 06.02.16.
 */
public class DefaultDisplayFactoryTest {

    @Test
    public void shouldCreateDefaultDisplayInstance() throws Exception {
        //given
        DefaultDisplayFactory defaultDisplayFactory = new DefaultDisplayFactory();
        //when
        Display display = defaultDisplayFactory.createDisplay();
        //then
        assertNotNull(display);
        assertTrue(display instanceof DefaultDisplay);
    }

}
