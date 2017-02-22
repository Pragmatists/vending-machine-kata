package tdd.vendingMachine.state;

import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;

import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 * State representing a Vending Machine with Empty shelves (sold out).
 */
public class SoldOutState implements State {

    public final String label = "SOLD OUT";
    final VendingMachine vendingMachine;

    public SoldOutState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        vendingMachine.showMessageOnDisplay(String.format("WARNING: %s returned back to cash bucket, machine is sold out", coin.label));
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.displayProductPrice(shelfNumber);
        } catch (NoSuchElementException nse) {
            vendingMachine.showMessageOnDisplay(nse.getMessage());
        }
    }

    @Override
    public void cancel() {
        if (vendingMachine.getCredit() > 0) {

        }
    }
}
