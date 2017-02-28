package tdd.vendingMachine.state.seller;

import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.exception.UnableToProvideBalanceException;
import tdd.vendingMachine.state.State;
import tdd.vendingMachine.view.VendingMachineMessages;

/**
 * @author Agustin Cabra on 2/27/2017.
 * @since 1.0
 * Defines a State that allows to perform sales operations on the vendingMachine.
 * States not extending from this class are not meant to perform sell or rollback operations.
 */
public abstract class SellerState implements State {

    protected final VendingMachine vendingMachine;

    /**
     *
     * @param vendingMachine the vending machine to provide a state
     */
    public SellerState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    /**
     * Regular sell execution method described as follows:
     *  1. If there is a selected product and the credit covers the price of that product the sell procedure:
     *      a. Provision all the credit from the user to the dispenser
     *      b. dispense if possible the balance to the cash pickup bucket
     *      c. dispense the product to the pickup bucket
     *      d. remove the selection of the product
     *      e. pass the machine to readyState
     *      f. return true
     *   or return false.
     * @throws UnableToProvideBalanceException If unable to provide change
     */
    protected void attemptSell() throws UnableToProvideBalanceException {
        if (vendingMachine.getSelectedProduct() != null
            && Integer.compare(vendingMachine.getCredit(), vendingMachine.getSelectedProduct().getPrice()) >= 0) {
            vendingMachine.provisionCreditStackCashToDispenser();
            vendingMachine.dispenseCurrentBalance();
            vendingMachine.dispenseSelectedProductToBucketAndClearCreditStack();
            vendingMachine.undoProductSelection();
            if (vendingMachine.isSoldOut()) {
                vendingMachine.setStateToSoldOutState();
            } else {
                vendingMachine.setStateToReadyState();
            }
        }
    }

    /**
     * Compensation task meant to be run in case of failure to perform sell
     * if state unable to perform task machine will enter on technical error state
     * @param exceptionMessage the exception message received from sell transaction
     * @param pendingBalance the current pending balance
     */
    protected void returnCreditStackToCashPickupBucketAndSetToReadyState(String exceptionMessage, int pendingBalance) {
        this.vendingMachine.showMessageOnDisplay(
            VendingMachineMessages.buildWarningMessageWithSubject(
                String.format("%s [%s] %s", exceptionMessage,
                    VendingMachineMessages.provideCashToDisplay(pendingBalance),
                    VendingMachineMessages.RETURNING_TOTAL_CASH_TO_BUCKET.label),
                vendingMachine.getCredit()
            )
        );
        this.vendingMachine.undoProductSelection();
        this.vendingMachine.returnCreditStackToBucketUpdatingCashDispenser();
        this.vendingMachine.setStateToReadyState();
    }
}
