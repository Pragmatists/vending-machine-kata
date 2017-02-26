package tdd.vendingMachine.state;

import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.exception.UnableToProvideBalanceException;
import tdd.vendingMachine.view.VendingMachineMessages;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 *
 * The States of the vending machine will be handled as implementations of this interface,
 * the overall process when inserting coins is described as follows:
 * 1. If product was previously selected.
 *      a. If current credit equals selectedProduct price:
 *         The product should be dropped to pickup shelf.
 *      b. If credit > selected product price and machine can give total change:
 *         The product should be dropped to pickup shelf
 *         The change (credit-selectedProduct price) is dropped to cash pickup shelf.
 *      c. If credit > selected product price and machine can NOT give total change:
 *         Warning label is displayed 'No Change Available'
 *         The total credit is dropped to cash pickup shelf and transaction is canceled.
 *      d. If credit < selectedProduct price:
 *          a. Displays current credit.
 * 2. If no product was previously selected the coin amount will be displayed as credit.
 */
public abstract class State {

    final protected VendingMachine vendingMachine;
    private final boolean performSell;

    /**
     *
     * @param vendingMachine the vending machine to provide a state
     * @param performSell boolean describing if current state has the sell functionality
     */
    protected State(VendingMachine vendingMachine, boolean performSell) {
        this.vendingMachine = vendingMachine;
        this.performSell = performSell;
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
        if (!performSell) {
            throw new UnsupportedOperationException(VendingMachineMessages.SELL_NOT_AVAILABLE_STATE.label);
        }
        if (vendingMachine.getSelectedProduct() != null
            && Integer.compare(vendingMachine.getCredit(), vendingMachine.getSelectedProduct().getPrice()) >= 0) {
            vendingMachine.provisionCreditStackCashToDispenser();
            vendingMachine.dispenseCurrentBalance();
            vendingMachine.dispenseSelectedProductToBucketAndClearCreditStack();
            vendingMachine.undoProductSelection();
            vendingMachine.setCurrentState(vendingMachine.getReadyState());
        }
    }

    /**
     * Compensation task meant to be run in case of failure to perform sell
     * if state unable to perform task machine will enter on technica error state
     * @param exceptionMessage the exception message received for sell transaction
     * @param pendingBalance the current pending balance
     */
    protected void returnCreditStackToCashPickupBucketAndSetToReadyState(String exceptionMessage, int pendingBalance) {
        if (!performSell) {
            throw new UnsupportedOperationException(VendingMachineMessages.SELL_NOT_AVAILABLE_STATE.label);
        }
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

    /**
     * Adds the current denomination as Credit to the vending machine and displays current credit.
     * @param coin the coin to be inserted in the machine
     */
    public abstract void insertCoin(Coin coin);

    /**
     * 1. Displays the product price related to the shelfNumber.
     * 2. Sets the product at selected.
     * @param shelfNumber the label visible to the customer on the vending machine
     */
    public abstract void selectShelfNumber(int shelfNumber);

    /**
     * The operations performs the changes to the VendingMachine state as follows:
     * 1. Returns total credit to pickup cash shelf.
     * 2. Vending machine credit to zero.
     * 3. Sets selectedProduct as null.
     */
    public abstract void cancel();
}
