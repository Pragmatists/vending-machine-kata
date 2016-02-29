package tdd.vendingMachine.test_infrastructure;

import org.mockito.InOrder;
import org.mockito.Mockito;
import tdd.vendingMachine.external_interface.HardwareInterface;

import java.util.Arrays;

public class HardwareInteractionAssertions {

    public static void assertCorrectMessageOrder(HardwareInterface hardwareInterface, String... messagesInOrder) {
        InOrder ordered = Mockito.inOrder(hardwareInterface);
        Arrays.stream(messagesInOrder).forEachOrdered(message -> ordered.verify(hardwareInterface).displayMessage(message));
    }
}
