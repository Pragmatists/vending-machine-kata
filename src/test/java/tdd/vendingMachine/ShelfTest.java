package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.ProductName;
import tdd.vendingMachine.core.ProductPrice;
import tdd.vendingMachine.impl.BasicShelf;

public class ShelfTest {

    @Test(expected = IndexOutOfBoundsException.class)
    public void should_throw_exception_when_there_is_no_product_to_withdraw() {
        new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("1")).withdraw();
    }
}
