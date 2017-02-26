package tdd.vendingMachine.domain.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 */
public class ShelfEmptyNotAvailableForSelectionExceptionTest {

    @Test
    public void should_validate_creation_of_exception() {
        String message = "test_message";
        int shelfNumber = 4;
        try {
            throw new ShelfEmptyNotAvailableForSelectionException(message, shelfNumber);
        } catch (ShelfEmptyNotAvailableForSelectionException exception) {
            Assert.assertEquals(message, exception.getMessage());
            Assert.assertEquals(shelfNumber, exception.getShelfNumber());
        }
    }

    @Test
    public void should_validate_creation_of_exception_with_cause() {
        String message = "test_message";
        int shelfNumber = 4;
        try {
            throw new ShelfEmptyNotAvailableForSelectionException(new NullPointerException(), message, shelfNumber);
        } catch (ShelfEmptyNotAvailableForSelectionException exception) {
            Assert.assertEquals(message, exception.getMessage());
            Assert.assertEquals(shelfNumber, exception.getShelfNumber());
            Assert.assertNotNull(exception.getCause());
        }
    }
}
