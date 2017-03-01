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
 * State representing that machine has credit received and no product has been selected.
 */
public class CreditNotSelectedProductState extends SellerState {

    private static final Logger logger = Logger.getLogger(CreditNotSelectedProductState.class);
    public static final String label = "HAS CREDIT NO PRODUCT SELECTED";

    public CreditNotSelectedProductState(VendingMachine vendingMachine) {
        super(vendingMachine);
    }

    @Override
    public void insertCoin(Coin coin) {
        try {
            vendingMachine.addCoinToCredit(coin);
        } catch (CashDispenserFullException cashDispenserFullException) {
            logger.error(cashDispenserFullException);
            this.vendingMachine.showMessageOnDisplay(String.format("[%s] %s",
                cashDispenserFullException.getMessage(),
                VendingMachineMessages.provideCashToDisplay(cashDispenserFullException.getAmountDeclined()))
            );
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
            if (vendingMachine.getCurrentState().equals(this)) {
                vendingMachine.setStateToInsufficientCreditState();
            }
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
    public void cancel() {
        try {
            vendingMachine.returnAllCreditToBucket();
            vendingMachine.setStateToReadyState();
        } catch (Exception e) {
            logger.error(e);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.setStateToTechnicalErrorState();

        }
    }
}
