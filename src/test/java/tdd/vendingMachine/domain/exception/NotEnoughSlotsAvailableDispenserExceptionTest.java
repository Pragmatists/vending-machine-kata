package tdd.vendingMachine.domain.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 */
public class NotEnoughSlotsAvailableDispenserExceptionTest {

    @Test
    public void should_validate_creation_of_exception() {
        String message = "test_message";
        int freeSlots = 4;
        int amountToProvision = 5;
        try {
            throw new NotEnoughSlotsAvailableDispenserException(message, freeSlots, amountToProvision);
        } catch (NotEnoughSlotsAvailableDispenserException exception) {
            Assert.assertEquals(message, exception.getMessage());
            Assert.assertEquals(freeSlots, exception.getAvailableSlots());
            Assert.assertEquals(amountToProvision, exception.getAmountToProvision());
        }
    }

    @Test
    public void should_validate_creation_of_exception_with_cause() {
        String message = "test_message";
        int freeSlots = 4;
        int amountToProvision = 5;
        try {
            throw new NotEnoughSlotsAvailableDispenserException(new NullPointerException(), message, freeSlots, amountToProvision);
        } catch (NotEnoughSlotsAvailableDispenserException exception) {
            Assert.assertEquals(message, exception.getMessage());
            Assert.assertEquals(freeSlots, exception.getAvailableSlots());
            Assert.assertEquals(amountToProvision, exception.getAmountToProvision());
            Assert.assertNotNull(exception.getCause());
        }
    }
}
