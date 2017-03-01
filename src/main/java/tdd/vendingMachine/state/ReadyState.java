package tdd.vendingMachine.state;

import org.apache.log4j.Logger;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.exception.CashDispenserFullException;
import tdd.vendingMachine.domain.exception.ShelfEmptyNotAvailableForSelectionException;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 * State representing a vending machine with no credit and no selected product
 */
public class ReadyState implements State {

    private static final Logger logger = Logger.getLogger(ReadyState.class);
    public static final String label = "READY";
    protected final VendingMachine vendingMachine;

    public ReadyState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        try {
            vendingMachine.addCoinToCredit(coin);
            vendingMachine.setStateToCreditNotSelectedProductState();
        } catch (CashDispenserFullException cashDispenserFullException) {
            logger.error(cashDispenserFullException);
            this.vendingMachine.showMessageOnDisplay(String.format("%s %s: %s", coin.label,
                VendingMachineMessages.CASH_NOT_ACCEPTED_DISPENSER_FULL.label,
                VendingMachineMessages.provideCashToDisplay(this.vendingMachine.getCredit())));
        } catch (Exception uoe) {
            logger.error(uoe);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.setStateToTechnicalErrorState();
        }
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.displayProductPrice(shelfNumber);
            vendingMachine.setStateToNoCreditSelectedProductState();
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(
                VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber, false));
        } catch (ShelfEmptyNotAvailableForSelectionException shelfEmptyException) {
            logger.error(shelfEmptyException);
            vendingMachine.showMessageOnDisplay(String.format("%s: %d",
                shelfEmptyException.getMessage(),
                shelfEmptyException.getShelfNumber()));
        } catch (Exception uoe) {
            logger.error(uoe);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.setStateToTechnicalErrorState();
        }
    }

    /**
     * On this state cancelling will not trigger any actions on the vending machine
     */
    @Override
    public void cancel() {
    }
}
