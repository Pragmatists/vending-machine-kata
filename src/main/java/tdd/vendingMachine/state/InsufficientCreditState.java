package tdd.vendingMachine.state;

import org.apache.log4j.Logger;
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
public class InsufficientCreditState implements State {

    private static final Logger logger = Logger.getLogger(InsufficientCreditState.class);

    protected final VendingMachineImpl vendingMachine;

    public final static StateEnum state = StateEnum.INSUFFICIENT_CREDIT;

    public InsufficientCreditState(VendingMachineImpl vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        try {
            vendingMachine.addCoinToCredit(coin);
            vendingMachine.attemptSell();
        } catch (CashDispenserFullException cashDispenserFullException) {
            logger.error(cashDispenserFullException);
            String message = String.format("%s %s: %s", coin.label,
                cashDispenserFullException.getMessage(),
                VendingMachineMessages.provideCashToDisplay(this.vendingMachine.provideCredit()));
            this.vendingMachine.showMessageOnDisplay(message);
        } catch (UnableToProvideBalanceException unableToProvideBalanceException) {
            logger.error(unableToProvideBalanceException);
            vendingMachine.rollBackSell(unableToProvideBalanceException.getMessage(),
                unableToProvideBalanceException.getPendingBalance());
        } catch (Exception uoe) {
            logger.error(uoe);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.sendStateTo(TechnicalErrorState.state);
        }

    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.displayProductPrice(shelfNumber);
            vendingMachine.attemptSell();
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(String.format("%s, [%s] %s: %s",
                VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber, false),
                vendingMachine.provideSelectedProduct()!=null ? vendingMachine.provideSelectedProduct().getType() : "",
                VendingMachineMessages.PENDING.label,
                VendingMachineMessages.provideCashToDisplay(vendingMachine.calculatePendingBalance()))
            );
        } catch (UnableToProvideBalanceException unableToProvideBalanceException) {
            logger.error(unableToProvideBalanceException);
            vendingMachine.rollBackSell(unableToProvideBalanceException.getMessage(),
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
            vendingMachine.sendStateTo(TechnicalErrorState.state);
        }

    }

    @Override
    public void cancel() {
        try {
            vendingMachine.undoProductSelection();
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.CANCEL.label);
            vendingMachine.returnAllCreditToBucket();
            vendingMachine.sendStateTo(ReadyState.state);
        } catch (Exception e) {
            logger.error(e);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.sendStateTo(TechnicalErrorState.state);
        }
    }
}
