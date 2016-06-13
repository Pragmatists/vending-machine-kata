package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.*;
import tdd.vendingMachine.impl.BasicShelf;

import static org.assertj.core.api.Assertions.assertThat;

public class VendingMachineTest {

    @Test
    public void should_configure_allowed_denominations() {
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addAllowedDenomination(CurrencyUnit.valueOf("2"));
        vendingMachine.addAllowedDenomination(CurrencyUnit.valueOf("5"));

        assertThat(vendingMachine.getAllowedDenominations().size()).isEqualTo(2);
    }

    @Test
    public void should_configure_shelves() {
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("1")));
        vendingMachine.addShelf(new BasicShelf(ProductName.valueOf("Product 2"), ProductPrice.valueOf("2")));

        assertThat(vendingMachine.getShelves().size()).isEqualTo(2);
    }

    @Test
    public void should_start_transaction_on_shelf_selection() {
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addAllowedDenomination(CurrencyUnit.valueOf("2"))
            .addAllowedDenomination(CurrencyUnit.valueOf("5"))
            .addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("1")));

        Transaction transaction = vendingMachine.selectShelf(0);
        assertThat(transaction).isNotNull();
    }
}
