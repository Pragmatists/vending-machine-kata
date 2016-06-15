package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.*;
import tdd.vendingMachine.impl.*;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionTest {

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_where_there_is_no_shelf() {
        new BasicTransaction(null, new BasicCashHandler(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_where_shelf_is_empty() {
        new BasicTransaction(new BasicShelf(null, null), new BasicCashHandler(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_without_cash_handler() {
        new BasicTransaction(new BasicShelf(null, null).charge(5), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_inserting_coin_with_negative_value() {
        new BasicTransaction(new BasicShelf(null, null).charge(1), new BasicCashHandler(), null)
            .insertCoin(CurrencyUnit.valueOf("-1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_inserting_coin_with_zero_value() {
        new BasicTransaction(new BasicShelf(null, null).charge(1), new BasicCashHandler(), null)
            .insertCoin(CurrencyUnit.zero());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_inserted_coin_not_allowed() {
        AllowedDenominations allowedDenominations = mock(AllowedDenominations.class);
        when(allowedDenominations.isAllowed(any(CurrencyUnit.class))).thenReturn(false);

        new BasicTransaction(new BasicShelf(null, null).charge(1), new BasicCashHandler(), allowedDenominations)
            .insertCoin(CurrencyUnit.valueOf("2"));
    }

    @Test
    public void should_consume_coins_and_detect_shortfall() {
        Shelf shelf = mock(Shelf.class);
        when(shelf.hasProducts()).thenReturn(true);
        when(shelf.getProductPrice()).thenReturn(CurrencyUnit.valueOf("2"));

        Transaction transaction = new BasicTransaction(shelf, mock(CashHandler.class), null)
            .insertCoin(CurrencyUnit.valueOf("1"));

        assertThat(transaction.getShortFall()).isEqualTo(CurrencyUnit.valueOf("1"));
        assertThat(transaction.insertCoin(CurrencyUnit.valueOf("5")).getShortFall()).isEqualTo(CurrencyUnit.zero());
    }

    @Test
    public void should_be_able_to_rollback_transaction() {
        Shelf shelf = mock(Shelf.class);
        when(shelf.hasProducts()).thenReturn(true);

        assertThat(new BasicTransaction(shelf, mock(CashHandler.class), null)
            .insertCoin(CurrencyUnit.valueOf("1"))
            .insertCoin(CurrencyUnit.valueOf("2"))
            .rollback()
        ).containsExactly(CurrencyUnit.valueOf("1"), CurrencyUnit.valueOf("2"));
    }

    @Test
    public void should_be_able_to_commit_valid_transaction() {
        ProductName productName = ProductName.valueOf("Product 1");
        ProductPrice productPrice = ProductPrice.valueOf("10");

        Shelf shelf = mock(Shelf.class);
        when(shelf.hasProducts()).thenReturn(true);
        when(shelf.getProductPrice()).thenReturn(CurrencyUnit.valueOf("1"));
        when(shelf.withdraw()).thenReturn(new BasicProduct(productName, productPrice));

        CashHandler cashHandler = mock(CashHandler.class);
        when(cashHandler.withdraw(any(CurrencyUnit.class))).thenReturn(Collections.singletonList(CurrencyUnit.valueOf("9")));

        PurchaseResult result = new BasicTransaction(shelf, cashHandler, null)
            .insertCoin(CurrencyUnit.valueOf("1"))
            .insertCoin(CurrencyUnit.valueOf("2"))
            .insertCoin(CurrencyUnit.valueOf("3"))
            .commit();

        assertThat(result.getProduct().getName()).isEqualTo(productName);
        assertThat(result.getProduct().getPrice()).isEqualTo(productPrice);
        assertThat(result.getChange()).containsExactly(CurrencyUnit.valueOf("9"));
    }
}
