package tdd.vendingMachine.view;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Agustin Cabra on 2/23/2017.
 * @since 1.0
 */
public class VendingMachineMessagesTest {

    @Test
    public void should_build_warning_message_with_currency_subject() {
        String problem = "tests";
        int subject = 1;
        String expectedMessage = "WARN: " + problem + " [0.01$]";
        Assert.assertEquals(expectedMessage, VendingMachineMessages.buildWarningMessageWithSubject(problem, subject));
    }

    @Test
    public void should_build_warning_message_no_currency_subject() {
        String problem = "tests";
        int subject = 1;
        String expectedMessage = "WARN: " + problem + " [" + subject + "]";
        Assert.assertEquals(expectedMessage, VendingMachineMessages.buildWarningMessageWithSubject(problem, subject, false));
    }

    @Test
    public void should_build_warning_message_without_subject() {
        String problem = "tests";
        String expectedMessage = "WARN: " + problem;
        Assert.assertEquals(expectedMessage, VendingMachineMessages.buildWarningMessageWithoutSubject(problem));
    }

    @Test
    public void should_convert_value_to_screen_decimal_to_present() {
        Assert.assertEquals("0.99$", VendingMachineMessages.provideCashToDisplay(99));
        Assert.assertEquals("0.05$", VendingMachineMessages.provideCashToDisplay(5));
        Assert.assertEquals("0.78$", VendingMachineMessages.provideCashToDisplay(78));
        Assert.assertEquals("5.00$", VendingMachineMessages.provideCashToDisplay(500));
    }
}
