package tdd.vendingMachine.state;

import org.apache.log4j.Logger;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.exception.CashDispenserFullException;
import tdd.vendingMachine.domain.exception.ShelfEmptyNotAvailableForSelectionException;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class CreditNotSelectedProductState implements State {

    private static final Logger logger = Logger.getLogger(CreditNotSelectedProductState.class);
    public static final String label = "HAS CREDIT NO PRODUCT SELECTED";
    final VendingMachine vendingMachine;

    public CreditNotSelectedProductState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        try {
            vendingMachine.addCoinToCredit(coin);
        } catch (UnsupportedOperationException uoe) {
            logger.error(uoe);
            this.vendingMachine.showMessageOnDisplay(String.format("%s %s: %s", coin.label,
                VendingMachineMessages.CASH_NOT_ACCEPTED_DISPENSER_FULL.label,
                VendingMachineMessages.provideCashToDisplay(this.vendingMachine.getCredit())));
        } catch (CashDispenserFullException e) {
            logger.error(e);
//            String.format("%s [%d] %s: %d",
//                e.getMessage(),
//                e.ge,
//                VendingMachineMessages.AVAILABLE.label,
//                maxShelfCountFound);

        }
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.displayProductPrice(shelfNumber);
            vendingMachine.setCurrentState(vendingMachine.getInsufficientCreditState());
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber));
        } catch (ShelfEmptyNotAvailableForSelectionException e) {
            logger.error(e);
            String.format("%s [%d] %s",
                e.getMessage(),
                e.getShelfNumber(),
                VendingMachineMessages.AVAILABLE.label);
        }
    }

    @Override
    public void cancel() {
        vendingMachine.returnAllCreditToBucket();
        vendingMachine.setCurrentState(vendingMachine.getReadyState());
    }
}
