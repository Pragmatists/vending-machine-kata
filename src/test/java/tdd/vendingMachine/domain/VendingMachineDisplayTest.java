package tdd.vendingMachine.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 */
public class VendingMachineDisplayTest {

    private VendingMachineDisplay vmd;

    @Before
    public void setup() {
        vmd = new VendingMachineDisplay();
    }

    @After
    public void tearDown() {
        vmd = null;
    }

    @Test
    public void should_display_and_update_current_message() {
        String notification = "message sent";

        Assert.assertEquals("", vmd.getCurrentMessage());

        vmd.update(null, notification);

        Assert.assertEquals(notification, vmd.getCurrentMessage());
    }
}
