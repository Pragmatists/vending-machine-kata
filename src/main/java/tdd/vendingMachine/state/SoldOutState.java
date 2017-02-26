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
public class SoldOutState extends State {

    private static final Logger logger = Logger.getLogger(SoldOutState.class);
    public final String label = "SOLD OUT";

    public SoldOutState(VendingMachine vendingMachine) {
        super(vendingMachine, false);
    }

    @Override
    public void insertCoin(Coin coin) {
        vendingMachine.showMessageOnDisplay(
            VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.CASH_NOT_ACCEPTED_MACHINE_SOLD_OUT.label, coin.denomination));
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.displayProductPrice(shelfNumber);
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(
                VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber,false));
        }
    }

    @Override
    public void cancel() {
        if (vendingMachine.getCredit() > 0) {

        }
    }
}
