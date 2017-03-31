package tdd.vendingMachine.listener;

import tdd.vendingMachine.domain.Denomination;
import tdd.vendingMachine.domain.VendingMachine;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author kdkz
 */
public class VendingMachineNotifier {

    private VendingMachine vendingMachine;

    public void setVendingMachine(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public void notifyAmountInserted(BigDecimal amountFromCoins) {
        vendingMachine.isSufficient(amountFromCoins);
    }

    public void notifyRestReturned(Map<Denomination, Integer> denominationIntegerMap, BigDecimal amountFromCoins) {
        vendingMachine.returnRest(denominationIntegerMap, amountFromCoins);
    }

    public void notifyProductPrice(int shelfNumber, BigDecimal productPrice) {
        vendingMachine.returnProductPrice(shelfNumber, productPrice);
    }
}
