package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.CurrencyUnit;
import tdd.vendingMachine.impl.BasicCashHandler;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class CashHandlerTest {

    @Test
    public void should_be_able_to_deposit_given_amount_of_coins() {
        assertThat(new BasicCashHandler()
            .deposit(CurrencyUnit.valueOf("2"), 5)
            .amount()
        ).isEqualTo(CurrencyUnit.valueOf("10"));
    }

    @Test
    public void should_be_able_to_deposit_coins_collection() {
        assertThat(new BasicCashHandler()
            .deposit(Arrays.asList(
                CurrencyUnit.valueOf("2"),
                CurrencyUnit.valueOf("2"),
                CurrencyUnit.valueOf("2"),
                CurrencyUnit.valueOf("2"),
                CurrencyUnit.valueOf("2")
            ))
            .amount()
        ).isEqualTo(CurrencyUnit.valueOf("10"));
    }

    @Test
    public void should_be_able_to_withdraw_if_there_is_money() {
        assertThat(new BasicCashHandler()
            .deposit(Arrays.asList(
                CurrencyUnit.valueOf("1"),
                CurrencyUnit.valueOf("2")
            ))
            .withdraw(CurrencyUnit.valueOf("2"))
        ).containsExactly(CurrencyUnit.valueOf("2"));
    }

    @Test
    public void should_return_moeny_if_empty() {
        new BasicCashHandler().withdraw(CurrencyUnit.valueOf("2"));
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_be_able_to_withdraw_if_there_is_not_enought_moeny() {
        new BasicCashHandler()
            .deposit(CurrencyUnit.valueOf("1"), 1)
            .withdraw(CurrencyUnit.valueOf("2"));
    }
}
