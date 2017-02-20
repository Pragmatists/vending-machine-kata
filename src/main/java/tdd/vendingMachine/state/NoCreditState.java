package tdd.vendingMachine.state;

import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 */
public class NoCreditState implements State {

    public final String label = "NO_CREDIT_STATE";
    private final VendingMachine vendingMachine;

    public NoCreditState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {

    }

    @Override
    public void selectProduct(String shelfLabel) {

    }

    @Override
    public void cancel() {
    }
}
