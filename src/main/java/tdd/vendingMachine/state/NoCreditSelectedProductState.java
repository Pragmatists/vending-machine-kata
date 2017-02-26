package tdd.vendingMachine.state;

import org.apache.log4j.Logger;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.exception.CashDispenserFullException;
import tdd.vendingMachine.domain.exception.ShelfEmptyNotAvailableForSelectionException;
import tdd.vendingMachine.domain.exception.UnableToProvideBalanceException;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class NoCreditSelectedProductState extends State {

    private static final Logger logger = Logger.getLogger(NoCreditSelectedProductState.class);
    public static final String label = "NO CREDIT SELECTED PRODUCT";

    public NoCreditSelectedProductState(VendingMachine vendingMachine) {
        super(vendingMachine, true);
    }

    @Override
    public void insertCoin(Coin coin) {
        try {
            vendingMachine.addCoinToCredit(coin);
            this.attemptSell();
        } catch (CashDispenserFullException cashDispenserFullException) {
            logger.error(cashDispenserFullException);
            String message = String.format("%s %s: %s", coin.label,
                cashDispenserFullException.getMessage(),
                VendingMachineMessages.provideCashToDisplay(this.vendingMachine.getCredit()));
            this.vendingMachine.showMessageOnDisplay(message);
        } catch (UnableToProvideBalanceException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException uoe) {
            logger.error(uoe);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.setCurrentState(vendingMachine.getTechnicalErrorState());
        }

    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.displayProductPrice(shelfNumber);
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(
                VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label,
                    shelfNumber, false));
        } catch (ShelfEmptyNotAvailableForSelectionException e) {
            logger.error(e);
            this.vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithSubject(
                VendingMachineMessages.UNABLE_TO_SELECT_EMPTY_SHELF.label,
                e.getShelfNumber(), false)
            );
        }
    }

    @Override
    public void cancel() {

    }
}
