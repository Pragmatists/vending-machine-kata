package tdd.vendingMachine.listener;

import tdd.vendingMachine.domain.Denomination;
import tdd.vendingMachine.domain.VendingMachine;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Notifier which notifies Vending Machine about state changes on VEnding Machine components (Cashier Pad and Shelf Box)
 *
 * @author kdkz
 */
public class VendingMachineNotifier {

    private VendingMachine vendingMachine;

    public void setVendingMachine(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    /**
     * Notifies what is the whole inserted coins amount.
     *
     * @param amountFromCoins whole amount
     */
    public void notifyAmountInserted(BigDecimal amountFromCoins) {
        vendingMachine.isSufficient(amountFromCoins);
    }

    /**
     * Notifies how much coins will be returned after successful transaction.
     *
     * @param denominationIntegerMap rest coins
     * @param amountFromCoins        rest amount
     */
    public void notifyRestReturned(Map<Denomination, Integer> denominationIntegerMap, BigDecimal amountFromCoins) {
        vendingMachine.restReturned(denominationIntegerMap, amountFromCoins);
    }

    /**
     * Notifies what is the price of the product on the selected shelf.
     *
     * @param shelfNumber  shelf number
     * @param productPrice product price
     */
    public void notifyProductPrice(int shelfNumber, BigDecimal productPrice) {
        vendingMachine.returnProductPrice(shelfNumber, productPrice);
    }
}
