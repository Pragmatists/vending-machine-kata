package tdd.vendingMachine.state.seller;

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
 * State representing a machine with no credit, but with a selected product.
 */
public class NoCreditSelectedProductState extends SellerState {

    private static final Logger logger = Logger.getLogger(NoCreditSelectedProductState.class);
    public static final String label = "NO CREDIT SELECTED PRODUCT";

    public NoCreditSelectedProductState(VendingMachine vendingMachine) {
        super(vendingMachine);
    }

    @Override
    public void insertCoin(Coin coin) {
        try {
            vendingMachine.addCoinToCredit(coin);
            this.attemptSell();
            if (vendingMachine.getCurrentState().equals(this)) {
                vendingMachine.setStateToInsufficientCreditState();
            }
        } catch (CashDispenserFullException cashDispenserFullException) {
            logger.error(cashDispenserFullException);
            String message = String.format("%s %s: %s", coin.label,
                cashDispenserFullException.getMessage(),
                VendingMachineMessages.provideCashToDisplay(this.vendingMachine.getCredit()));
            this.vendingMachine.showMessageOnDisplay(message);
        } catch (UnableToProvideBalanceException unableToProvideBalanceException) {
            logger.error(unableToProvideBalanceException);
            this.returnCreditStackToCashPickupBucketAndSetToReadyState(unableToProvideBalanceException.getMessage(),
                unableToProvideBalanceException.getPendingBalance());
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
        } catch (Exception uoe) {
            logger.error(uoe);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.setStateToTechnicalErrorState();
        }
    }

    @Override
    public void cancel() {
        try {
            vendingMachine.undoProductSelection();
            vendingMachine.setStateToReadyState();
        } catch (Exception e) {
            logger.error(e);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.setStateToTechnicalErrorState();
        }
    }
}
