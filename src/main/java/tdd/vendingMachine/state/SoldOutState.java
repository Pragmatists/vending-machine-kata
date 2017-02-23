package tdd.vendingMachine.state;

import org.apache.log4j.Logger;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 * State representing a Vending Machine with Empty shelves (sold out).
 */
public class SoldOutState implements State {

    private static final Logger logger = Logger.getLogger(SoldOutState.class);
    public final String label = "SOLD OUT";
    final VendingMachine vendingMachine;

    public SoldOutState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        vendingMachine.showMessageOnDisplay(String.format("WARN: [%s] %s", VendingMachineMessages.CASH_NOT_ACCEPTED_MACHINE_SOLD_OUT.label, coin.label));
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.displayProductPrice(shelfNumber);
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(String.format("WARN: %s: [%d]", VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber));
        }
    }

    @Override
    public void cancel() {
        if (vendingMachine.getCredit() > 0) {

        }
    }
}
