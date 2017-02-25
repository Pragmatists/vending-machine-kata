package tdd.vendingMachine.domain.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 */
public class CashDispenserFullExceptionTest {

    @Test
    public void should_validate_creation_of_exception_no_cause() {
        String message = "test_message";
        int pendingBalance = 0;
        try {
            throw new CashDispenserFullException(message, pendingBalance);
        } catch (CashDispenserFullException exception) {
            Assert.assertEquals(message, exception.getMessage());
            Assert.assertEquals(pendingBalance, exception.getAmountDeclined());
        }
    }

    @Test
    public void should_validate_creation_of_exception_with_cause() {
        String message = "test_message";
        int pendingBalance = 0;
        try {
            throw new CashDispenserFullException(new NullPointerException(), message, pendingBalance);
        } catch (CashDispenserFullException exception) {
            Assert.assertEquals(message, exception.getMessage());
            Assert.assertEquals(pendingBalance, exception.getAmountDeclined());
            Assert.assertNotNull(exception.getCause());
        }
    }
}
