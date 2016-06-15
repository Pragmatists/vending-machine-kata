package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.CurrencyUnit;
import tdd.vendingMachine.impl.AllowedDenominations;
import tdd.vendingMachine.impl.BasicCashHandler;
import tdd.vendingMachine.impl.BasicShelf;
import tdd.vendingMachine.impl.BasicTransaction;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionTest {

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_where_there_is_no_shelf() {
        new BasicTransaction(null, new BasicCashHandler(), new AllowedDenominations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_where_shelf_is_empty() {
        new BasicTransaction(new BasicShelf(null, null), new BasicCashHandler(), new AllowedDenominations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_without_cash_handler() {
        new BasicTransaction(new BasicShelf(null, null).charge(5), null, new AllowedDenominations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_inserting_coin_with_negative_value() {
        AllowedDenominations allowedDenominations = mock(AllowedDenominations.class);
        when(allowedDenominations.add(any(CurrencyUnit.class))).thenReturn(allowedDenominations);
        when(allowedDenominations.isAllowed(any(CurrencyUnit.class))).thenReturn(true);

        new BasicTransaction(new BasicShelf(null, null).charge(1), new BasicCashHandler(), allowedDenominations)
            .insertCoin(CurrencyUnit.valueOf("-1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_inserting_coin_with_zero_value() {
        AllowedDenominations allowedDenominations = mock(AllowedDenominations.class);
        when(allowedDenominations.add(any(CurrencyUnit.class))).thenReturn(allowedDenominations);
        when(allowedDenominations.isAllowed(any(CurrencyUnit.class))).thenReturn(true);

        new BasicTransaction(new BasicShelf(null, null).charge(1), new BasicCashHandler(), allowedDenominations)
            .insertCoin(CurrencyUnit.zero());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_inserting_coin_with_invalid_value() {
        AllowedDenominations allowedDenominations = mock(AllowedDenominations.class);
        when(allowedDenominations.add(any(CurrencyUnit.class))).thenReturn(allowedDenominations);
        when(allowedDenominations.isAllowed(any(CurrencyUnit.class))).thenReturn(false);

        new BasicTransaction(new BasicShelf(null, null).charge(1), new BasicCashHandler(), allowedDenominations)
            .insertCoin(CurrencyUnit.valueOf("2"));
    }
}
