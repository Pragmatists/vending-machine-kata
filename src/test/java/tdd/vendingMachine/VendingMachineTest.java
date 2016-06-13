package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.*;
import tdd.vendingMachine.impl.BasicShelf;

import java.util.Collection;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

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

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_inserted_not_allowed_coin() {
        VendingMachine vendingMachine = new VendingMachine()
            .addAllowedDenomination(CurrencyUnit.valueOf("2.5"))
            .addAllowedDenomination(CurrencyUnit.valueOf("5"))
            .addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("10")).charge(1));

        vendingMachine.selectShelf(0).insertCoin(CurrencyUnit.valueOf("2"));
    }

    @Test
    public void should_start_transaction_on_shelf_selection() {
        VendingMachine vendingMachine = new VendingMachine()
            .addAllowedDenomination(CurrencyUnit.valueOf("2"))
            .addAllowedDenomination(CurrencyUnit.valueOf("5"))
            .addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("1")).charge(1));

        Transaction transaction = vendingMachine.selectShelf(0);
        assertThat(transaction).isNotNull();
    }

    @Test
    public void should_consume_coins_and_detect_shortfall() {
        VendingMachine vendingMachine = new VendingMachine()
            .addAllowedDenomination(CurrencyUnit.valueOf("2"))
            .addAllowedDenomination(CurrencyUnit.valueOf("5"))
            .addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("10")).charge(10));

        Transaction transaction = vendingMachine.selectShelf(0).insertCoin(CurrencyUnit.valueOf("2"));
        assertThat(transaction.getShortFall()).isEqualTo(CurrencyUnit.valueOf("8"));
        assertThat(transaction.insertCoin(CurrencyUnit.valueOf("5")).getShortFall()).isEqualTo(CurrencyUnit.valueOf("3"));
        assertThat(transaction.insertCoin(CurrencyUnit.valueOf("2")).getShortFall()).isEqualTo(CurrencyUnit.valueOf("1"));
        assertThat(transaction.insertCoin(CurrencyUnit.valueOf("2")).getShortFall()).isEqualTo(CurrencyUnit.zero());
    }

    @Test
    public void should_be_able_to_rollback_transaction() {
        VendingMachine vendingMachine = new VendingMachine()
            .addAllowedDenomination(CurrencyUnit.valueOf("2"))
            .addAllowedDenomination(CurrencyUnit.valueOf("5"))
            .addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("10")).charge(10));

        Collection<CurrencyUnit> rollbackResult = vendingMachine.selectShelf(0)
            .insertCoin(CurrencyUnit.valueOf("5"))
            .insertCoin(CurrencyUnit.valueOf("2"))
            .insertCoin(CurrencyUnit.valueOf("5"))
            .rollback();

        assertThat(rollbackResult.size()).isEqualTo(3);

        Iterator<CurrencyUnit> iterator = rollbackResult.iterator();
        assertThat(iterator.next()).isEqualTo(CurrencyUnit.valueOf("5"));
        assertThat(iterator.next()).isEqualTo(CurrencyUnit.valueOf("2"));
        assertThat(iterator.next()).isEqualTo(CurrencyUnit.valueOf("5"));
    }

    @Test
    public void should_be_able_to_commit_valid_transaction() {
        VendingMachine vendingMachine = new VendingMachine()
            .addAllowedDenomination(CurrencyUnit.valueOf("2.5"))
            .addAllowedDenomination(CurrencyUnit.valueOf("5"))
            .addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("10")).charge(10));

        PurchaseResult purchaseResult = vendingMachine.selectShelf(0)
            .insertCoin(CurrencyUnit.valueOf("5"))
            .insertCoin(CurrencyUnit.valueOf("2.5"))
            .insertCoin(CurrencyUnit.valueOf("2.5"))
            .commit();

        assertThat(purchaseResult).isNotNull();
        assertTrue(purchaseResult.isSuccessful());

        Collection<CurrencyUnit> change = purchaseResult.getChange();

        assertThat(change).isNotNull();
        assertThat(change.size()).isZero();

        Product product = purchaseResult.getProduct();

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo(ProductName.valueOf("Product 1"));
        assertThat(product.getPrice()).isEqualTo(ProductPrice.valueOf("10"));

        Shelf shelf = vendingMachine.getShelves().iterator().next();

        assertThat(shelf.amount()).isEqualTo(9);
    }

    @Test
    public void should_be_able_to_give_right_change() {
        VendingMachine vendingMachine = new VendingMachine()
            .addAllowedDenomination(CurrencyUnit.valueOf("2.5"))
            .addAllowedDenomination(CurrencyUnit.valueOf("5"))
            .addShelf(new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("10")).charge(10));

        assertThat(vendingMachine.moneyAmount()).isEqualTo(CurrencyUnit.zero());

        PurchaseResult purchaseResult = vendingMachine.selectShelf(0)
            .insertCoin(CurrencyUnit.valueOf("5"))
            .insertCoin(CurrencyUnit.valueOf("2.5"))
            .insertCoin(CurrencyUnit.valueOf("5"))
            .commit();

        assertThat(purchaseResult.getProduct()).isNotNull();

        Collection<CurrencyUnit> change = purchaseResult.getChange();
        assertThat(change.size()).isEqualTo(1);

        Iterator<CurrencyUnit> iterator = change.iterator();
        assertThat(iterator.next()).isEqualTo(CurrencyUnit.valueOf("2.5"));
        assertThat(vendingMachine.moneyAmount()).isEqualTo(CurrencyUnit.valueOf("10"));
    }
}
