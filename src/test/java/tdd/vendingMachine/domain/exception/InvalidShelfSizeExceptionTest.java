
package tdd.vendingMachine.domain.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 */
public class InvalidShelfSizeExceptionTest {

    @Test
    public void should_validate_creation_of_exception_no_cause() {
        String message = "test_message";
        int givenSize = 0;
        int maxSize = 0;
        try {
            throw new InvalidShelfSizeException(message, maxSize, givenSize);
        } catch (InvalidShelfSizeException exception) {
            Assert.assertEquals(message, exception.getMessage());
            Assert.assertEquals(givenSize, exception.getGivenSize());
            Assert.assertEquals(maxSize, exception.getMaximumSize());
        }
    }

    @Test
    public void should_validate_creation_of_exception_with_cause() {
        String message = "test_message";
        int givenSize = 0;
        int maxSize = 0;
        try {
            throw new InvalidShelfSizeException(new NullPointerException(), message, maxSize, givenSize);
        } catch (InvalidShelfSizeException exception) {
            Assert.assertEquals(givenSize, exception.getGivenSize());
            Assert.assertEquals(maxSize, exception.getMaximumSize());
            Assert.assertNotNull(exception.getCause());
        }
    }
}
