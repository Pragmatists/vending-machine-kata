package tdd.vendingMachine.state;

import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class NoCreditSelectedProductState implements State {

    public static final String label = "NO CREDIT SELECTED PRODUCT";
    final VendingMachine vendingMachine;

    public NoCreditSelectedProductState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {

    }

    @Override
    public void selectShelfNumber(int shelfNumber) {

    }

    @Override
    public void cancel() {

    }
}
