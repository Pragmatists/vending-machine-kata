package tdd.vendingMachine.view;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Agustin Cabra on 2/23/2017.
 * @since 1.0
 */
public class VendingMachineMessagesTest {

    @Test
    public void should_build_warning_message_with_subject() {
        String problem = "tests";
        int subject = 1;
        String expectedMessage = "WARN: " + problem + " [" + subject + "]";
        Assert.assertEquals(expectedMessage, VendingMachineMessages.buildWarningMessageWithSubject(problem, subject));
    }

    @Test
    public void should_build_warning_message_without_subject() {
        String problem = "tests";
        String expectedMessage = "WARN: " + problem;
        Assert.assertEquals(expectedMessage, VendingMachineMessages.buildWarningMessageWithoutSubject(problem));
    }
}
