package tdd.vendingMachine;

import org.junit.Before;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Coins;
import tdd.vendingMachine.domain.Product;

public abstract class AbstractVendingMachineTest {

    protected VendingMachine vendingMachine;

    @Before
    public void init() {
        // given
        this.vendingMachine = VendingMachineFactory.create(
            Coins.of(Coin.FIVE.nth(1), Coin.TWO.nth(1), Coin.HALF.nth(2)),
            Product.COLA, Product.CHIPS, Product.CROISSANT
        );
    }

}
