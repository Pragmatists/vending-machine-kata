package tdd.vendingMachine;

import org.junit.Assert;
import org.junit.Test;
import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.dto.CashImport;
import tdd.vendingMachine.state.NoCreditState;
import tdd.vendingMachine.state.SoldOutState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VendingMachineTest {

    @Test
    public void should_be_sold_out_state_for_empty_created_vending_machine() {
        VendingMachine emptyVendingMachine = new VendingMachine(Collections.emptyMap(), Collections.emptyMap());
        Assert.assertTrue(emptyVendingMachine.getCurrentState() instanceof SoldOutState);
    }

    @Test
    public void should_be_no_credit_state_for_non_empty_created_vending_machine() {
        VendingMachine nonEmptyVendingMachine = new VendingMachine(buildShelvesOneItem(), Collections.emptyMap());
        Assert.assertTrue(nonEmptyVendingMachine.getCurrentState() instanceof NoCreditState);
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_on_null_products_shelves_given() {
        new VendingMachine(null, Collections.emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_on_null_coins_shelves_given() {
        new VendingMachine(Collections.emptyMap(), null);
    }

    private Map<Integer, Shelf<Product>> buildShelvesOneItem() {
        Shelf<Product> shelf = ShelfProductFactory.buildShelf(0, new Product(1.99, "cola"), 10, 1);
        return new HashMap<Integer, Shelf<Product>>(){{put(shelf.id, shelf);}};
    }
}
