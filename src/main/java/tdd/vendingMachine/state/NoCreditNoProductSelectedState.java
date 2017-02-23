package tdd.vendingMachine.state;

import org.apache.log4j.Logger;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 */
public class NoCreditNoProductSelectedState implements State {

    private static final Logger logger = Logger.getLogger(NoCreditNoProductSelectedState.class);
    public static final String label = "NO CREDIT STATE NO PRODUCT SELECTED";
    final VendingMachine vendingMachine;

    public NoCreditNoProductSelectedState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        if (vendingMachine.addCoinToCredit(coin)) {
            vendingMachine.setCurrentState(vendingMachine.getHasCreditNoProductSelectedState());
        }
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.displayProductPrice(shelfNumber);
            vendingMachine.setCurrentState(vendingMachine.getNoCreditProductSelectedState());
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber));
        }
    }

    /**
     * On this state cancelling will not trigger any actions on the vending machine
     */
    @Override
    public void cancel() {
    }
}
