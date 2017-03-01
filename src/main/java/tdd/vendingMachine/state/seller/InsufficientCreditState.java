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
 * State representing a machine with a selected product but with credit lower than
 * the selected product's price
 */
public class InsufficientCreditState extends SellerState {

    private static final Logger logger = Logger.getLogger(InsufficientCreditState.class);
    public static final String label = "HAS CREDIT PRODUCT SELECTED";

    public InsufficientCreditState(VendingMachine vendingMachine) {
        super(vendingMachine);
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
            this.attemptSell();
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(String.format("%s, [%s] %s: %s",
                VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber, false),
                vendingMachine.getSelectedProduct()!=null ? vendingMachine.getSelectedProduct().getType() : "",
                VendingMachineMessages.PENDING.label,
                VendingMachineMessages.provideCashToDisplay(vendingMachine.calculatePendingBalance()))
            );
        } catch (UnableToProvideBalanceException unableToProvideBalanceException) {
            logger.error(unableToProvideBalanceException);
            this.returnCreditStackToCashPickupBucketAndSetToReadyState(unableToProvideBalanceException.getMessage(),
                unableToProvideBalanceException.getPendingBalance());
        } catch (ShelfEmptyNotAvailableForSelectionException shelfEmptyException) {
            logger.error(shelfEmptyException);
            vendingMachine.showMessageOnDisplay(
                VendingMachineMessages.buildWarningMessageWithSubject(shelfEmptyException.getMessage(),
                    shelfEmptyException.getShelfNumber(), false)
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
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.CANCEL.label);
            vendingMachine.returnAllCreditToBucket();
            vendingMachine.setStateToReadyState();
        } catch (Exception e) {
            logger.error(e);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.setStateToTechnicalErrorState();
        }
    }
}
