package tdd.vendingMachine.state;

import tdd.vendingMachine.domain.Coin;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 *
 * The States of the vending machine will be handled as extensions of this interface class,
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
public interface State {

    /**
     * Adds the current denomination as Credit to the vending machine and displays current credit.
     * @param coin the coin to be inserted in the machine
     */
    void insertCoin(Coin coin);

    /**
     * 1. Displays the product price related to the shelfNumber.
     * 2. Sets the product at selected.
     * @param shelfNumber the label visible to the customer on the vending machine
     */
    void selectShelfNumber(int shelfNumber);

    /**
     * The operations performs the changes to the VendingMachine state as follows:
     * 1. Returns total credit to pickup cash shelf.
     * 2. Vending machine credit to zero.
     * 3. Sets selectedProduct as null.
     */
    void cancel();

}
