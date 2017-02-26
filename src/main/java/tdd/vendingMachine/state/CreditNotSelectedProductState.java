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
public class CreditNotSelectedProductState extends State {

    private static final Logger logger = Logger.getLogger(CreditNotSelectedProductState.class);
    public static final String label = "HAS CREDIT NO PRODUCT SELECTED";

    public CreditNotSelectedProductState(VendingMachine vendingMachine) {
        super(vendingMachine, CreditNotSelectedProductState.class);
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
        }
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.displayProductPrice(shelfNumber);
            this.attemptSell();
            if (vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState) {
                vendingMachine.setCurrentState(vendingMachine.getInsufficientCreditState());
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
        } catch (UnsupportedOperationException uoe) {
            logger.error(uoe);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.setCurrentState(vendingMachine.getTechnicalErrorState());
        }

    }

    @Override
    public void cancel() {
        vendingMachine.returnAllCreditToBucket();
        vendingMachine.setCurrentState(vendingMachine.getReadyState());
    }
}
