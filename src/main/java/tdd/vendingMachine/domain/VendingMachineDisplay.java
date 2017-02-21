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

    public String getCurrentMessage() {
        return this.currentMessage;
    }

    public void update(String message) {
        this.currentMessage = message.toString();
        logger.info(currentMessage);
    }
}
