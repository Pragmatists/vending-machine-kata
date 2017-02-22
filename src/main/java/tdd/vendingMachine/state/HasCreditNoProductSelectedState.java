package tdd.vendingMachine.state;

import org.apache.log4j.Logger;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;

import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class HasCreditNoProductSelectedState implements State {

    private static final Logger logger = Logger.getLogger(HasCreditNoProductSelectedState.class);
    public static final String label = "HAS CREDIT NO PRODUCT SELECTED";
    final VendingMachine vendingMachine;

    public HasCreditNoProductSelectedState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        vendingMachine.addCoinToCredit(coin);
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        try {
            vendingMachine.displayProductPrice(shelfNumber);
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.setCurrentState(vendingMachine.getHasCreditProductSelectedState());
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(nse.getMessage());
        }
    }

    @Override
    public void cancel() {
        vendingMachine.returnAllCreditToBucket();
        vendingMachine.setCurrentState(vendingMachine.getNoCreditNoProductSelectedState());
    }
}
