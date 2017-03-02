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
 * State representing that machine has credit received and no product has been selected.
 */
public class CreditNotSelectedProductState implements State {

    private static final Logger logger = Logger.getLogger(CreditNotSelectedProductState.class);

    protected final VendingMachineImpl vendingMachine;

    public static final StateEnum state = StateEnum.CREDIT_NOT_SELECTED_PRODUCT;

    public CreditNotSelectedProductState(VendingMachineImpl vendingMachine) {
        this.vendingMachine = vendingMachine;
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
            vendingMachine.sendStateTo(TechnicalErrorState.state);
        }
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.displayProductPrice(shelfNumber);
            vendingMachine.attemptSell();
            if (vendingMachine.provideCurrentState().equals(this)) {
                vendingMachine.sendStateTo(InsufficientCreditState.state);
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
            vendingMachine.rollBackSell(unableToProvideBalanceException.getMessage(),
                unableToProvideBalanceException.getPendingBalance());
        } catch (Exception uoe) {
            logger.error(uoe);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.sendStateTo(TechnicalErrorState.state);
        }
    }

    @Override
    public void cancel() {
        try {
            vendingMachine.returnAllCreditToBucket();
            vendingMachine.sendStateTo(ReadyState.state);
        } catch (Exception e) {
            logger.error(e);
            vendingMachine.showMessageOnDisplay(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.TECHNICAL_ERROR.label));
            vendingMachine.sendStateTo(TechnicalErrorState.state);

        }
    }
}
