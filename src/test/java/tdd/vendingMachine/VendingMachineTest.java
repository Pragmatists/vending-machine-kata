package tdd.vendingMachine;

import org.junit.Assert;
import org.junit.Test;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Shelf;
import tdd.vendingMachine.domain.ShelfProductFactory;
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
        VendingMachine nonEmptyVendingMachine = new VendingMachine(buildShelvesOneItem(), new HashMap<>());
        Assert.assertTrue(nonEmptyVendingMachine.getCurrentState() instanceof NoCreditState);
    }

    private Map<String, Shelf<Product>> buildShelvesOneItem() {
        Shelf<Product> shelf = ShelfProductFactory.buildShelf("shelf1", new Product(1.99, "cola"), 10, 1);
        return new HashMap<String, Shelf<Product>>(){{put(shelf.id, shelf);}};
    }
}
