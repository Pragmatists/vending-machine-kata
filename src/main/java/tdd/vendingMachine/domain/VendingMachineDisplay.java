package tdd.vendingMachine.domain;

import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 *
 * Class Representing Vending machine display.
 */
public class VendingMachineDisplay {

    private static final Logger logger = Logger.getLogger(VendingMachineDisplay.class);

    private String currentMessage = "";

    /**
     * Returns the last requested label to be displayed on the screen
     * @return
     */
    public String getCurrentMessage() {
        return this.currentMessage;
    }

    /**
     * Displays the label and updates the currentMessage variable
     * @param message the label to display
     */
    public void update(String message) { //TODO: TODO01 perhaps improve the storage to a more concise approach using an event stack.
        this.currentMessage = message;
        System.out.println("VENDING MACHINE: " + currentMessage);
    }
}
