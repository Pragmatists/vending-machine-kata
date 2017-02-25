package tdd.vendingMachine.domain.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 */
public class UnableToProvideBalanceExceptionTest {

    @Test
    public void should_validate_creation_of_exception_no_cause() {
        String message = "test_message";
        int pendingBalance = 0;
        try {
            throw new UnableToProvideBalanceException(message, pendingBalance);
        } catch (UnableToProvideBalanceException exception) {
            Assert.assertEquals(message, exception.getMessage());
            Assert.assertEquals(pendingBalance, exception.getPendingBalance());
        }
    }
    @Test
    public void should_validate_creation_of_exception_with_cause() {
        String message = "test_message";
        int pendingBalance = 0;
        try {
            throw new UnableToProvideBalanceException(new NullPointerException(), message, pendingBalance);
        } catch (UnableToProvideBalanceException exception) {
            Assert.assertEquals(message, exception.getMessage());
            Assert.assertEquals(pendingBalance, exception.getPendingBalance());
            Assert.assertNotNull(exception.getCause());
        }
    }
}
