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
            .addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("1")).charge(1));

        Transaction transaction = vendingMachine.selectShelf(0);
        assertThat(transaction).isNotNull();
    }

    @Test
    public void should_consume_coins_and_detect_shortfall() {
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addAllowedDenomination(CurrencyUnit.valueOf("2"))
            .addAllowedDenomination(CurrencyUnit.valueOf("5"))
            .addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("10")).charge(10));

        Transaction transaction = vendingMachine.selectShelf(0).insertCoin(CurrencyUnit.valueOf("2"));
        assertThat(transaction.getShortFall()).isEqualTo(CurrencyUnit.valueOf("8"));
        assertThat(transaction.insertCoin(CurrencyUnit.valueOf("5")).getShortFall()).isEqualTo(CurrencyUnit.valueOf("3"));
        assertThat(transaction.insertCoin(CurrencyUnit.valueOf("2")).getShortFall()).isEqualTo(CurrencyUnit.valueOf("1"));
        assertThat(transaction.insertCoin(CurrencyUnit.valueOf("2")).getShortFall()).isEqualTo(CurrencyUnit.zero());
    }
}
