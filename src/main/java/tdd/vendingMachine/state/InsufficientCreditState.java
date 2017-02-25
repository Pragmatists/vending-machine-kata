package tdd.vendingMachine.state;

import org.apache.log4j.Logger;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.exception.CashDispenserFullException;
import tdd.vendingMachine.domain.exception.ShelfEmptyNotAvailableForSelectionException;
import tdd.vendingMachine.domain.exception.UnableToProvideBalanceException;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 * State representing a machine with a selected product but with credit lower than
 * the selected product's price
 */
public class InsufficientCreditState implements State {

    private static final Logger logger = Logger.getLogger(InsufficientCreditState.class);
    public static final String label = "HAS CREDIT PRODUCT SELECTED";
    final VendingMachine vendingMachine;

    public InsufficientCreditState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    private void attemptProductSell() throws UnableToProvideBalanceException {
        if (Integer.compare(vendingMachine.getCredit(), vendingMachine.getSelectedProduct().getPrice()) >= 0) {
            vendingMachine.provisionCreditStackCashToDispenser();
            vendingMachine.dispenseCurrentBalance();
            vendingMachine.dispenseSelectedProductToBucketAndClearCreditStack();
            vendingMachine.undoProductSelection();
            vendingMachine.setCurrentState(vendingMachine.getReadyState());
        }
    }

    @Override
    public void insertCoin(Coin coin) {
        try {
            vendingMachine.addCoinToCredit(coin);
            this.attemptProductSell();
        } catch (CashDispenserFullException cashDispenserFullException) {
            logger.error(cashDispenserFullException);
            String message = String.format("%s %s: %s", coin.label,
                cashDispenserFullException.getMessage(),
                VendingMachineMessages.provideCashToDisplay(this.vendingMachine.getCredit()));
            this.vendingMachine.showMessageOnDisplay(message);
        } catch (UnableToProvideBalanceException unableToProvideBalanceException) {
            logger.error(unableToProvideBalanceException);
            returnCreditStackToCashPickupBucketAndSetToReadyState(unableToProvideBalanceException.getMessage(),
                unableToProvideBalanceException.getPendingBalance());
        }
    }

    private void returnCreditStackToCashPickupBucketAndSetToReadyState(String exceptionMessage, int pendingBalance) {
        this.vendingMachine.showMessageOnDisplay(
            VendingMachineMessages.buildWarningMessageWithSubject(
                String.format("%s [%s] %s", exceptionMessage,
                    VendingMachineMessages.provideCashToDisplay(pendingBalance),
                    VendingMachineMessages.RETURNING_TOTAL_CASH_TO_BUCKET.label),
                vendingMachine.getCredit()
            )
        );
        this.vendingMachine.returnCreditStackToBucketUpdatingCashDispenser();
        this.vendingMachine.setCurrentState(this.vendingMachine.getReadyState());
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        Product prev = vendingMachine.getSelectedProduct();
        try {
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.displayProductPrice(shelfNumber);
            this.attemptProductSell();
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(String.format("%s, [%s] %s: %s",
                VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber),
                prev.provideType(),
                VendingMachineMessages.PENDING.label,
                VendingMachineMessages.provideCashToDisplay(vendingMachine.calculatePendingBalance()))
            );
        } catch (UnableToProvideBalanceException unableToProvideBalanceException) {
            logger.error(unableToProvideBalanceException);
            returnCreditStackToCashPickupBucketAndSetToReadyState(unableToProvideBalanceException.getMessage(),
                unableToProvideBalanceException.getPendingBalance());
        } catch (ShelfEmptyNotAvailableForSelectionException shelfEmptyException) {
            logger.error(shelfEmptyException);
        }
    }

    @Override
    public void cancel() {
        vendingMachine.showMessageOnDisplay(VendingMachineMessages.CANCEL.label);
        vendingMachine.returnAllCreditToBucket();
        vendingMachine.setCurrentState(vendingMachine.getReadyState());
    }
}
