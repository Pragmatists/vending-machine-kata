package tdd.vendingMachine.state;

import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;

import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 */
public class NoCreditNoProductSelectedState implements State {

    public static final String label = "NO CREDIT STATE NO PRODUCT SELECTED";
    final VendingMachine vendingMachine;

    public NoCreditNoProductSelectedState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        if (vendingMachine.addCoinToCredit(coin)) {
            vendingMachine.setCurrentState(vendingMachine.getHasCreditNoProductSelectedState());
        }
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.displayProductPrice(shelfNumber);
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.setCurrentState(vendingMachine.getNoCreditProductSelectedState());
        } catch (NoSuchElementException nse) {
            vendingMachine.showMessageOnDisplay(nse.getMessage());
        }
    }

    /**
     * On this state cancelling will not trigger any actions on the vending machine
     */
    @Override
    public void cancel() {
    }
}
