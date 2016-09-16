package tdd.vendingMachine.cash.register;

import org.junit.Test;
import tdd.vendingMachine.cash.coin.Coin;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class CashBoxTest {

    CashBox cashBox = new CashBox();

    @Test
    public void shouldReturnTrueForValidCoinValue() throws Exception {
        //given
        //when
        //then
        assertThat(cashBox.isValidCoin(new Coin(5.0))).isTrue();
        assertThat(cashBox.isValidCoin(new Coin(2.0))).isTrue();
        assertThat(cashBox.isValidCoin(new Coin(1.0))).isTrue();
        assertThat(cashBox.isValidCoin(new Coin(0.5))).isTrue();
        assertThat(cashBox.isValidCoin(new Coin(0.2))).isTrue();
        assertThat(cashBox.isValidCoin(new Coin(0.1))).isTrue();
    }

    @Test
    public void shouldReturnFalseForInvalidCoinValue() throws Exception {
        //given
        //when
        //then
        assertThat(cashBox.isValidCoin(new Coin(10.0))).isFalse();
        assertThat(cashBox.isValidCoin(new Coin(-1.0))).isFalse();
        assertThat(cashBox.isValidCoin(new Coin(0.55))).isFalse();
        assertThat(cashBox.isValidCoin(new Coin(5.01))).isFalse();
    }

    @Test
    public void shouldAddCoinToCurrentRequestPocket() throws Exception {
        //given
        Coin coin = new Coin(5.0);
        //when
        cashBox.addToCurrentRequestPocket(coin);
        //then
        assertThat(cashBox.currentRequestPocket).contains(coin);
    }

    @Test
    public void shouldAddCoinToCashBoxPocket() throws Exception {
        //given
        Coin coin = new Coin(5.0);
        //when
        cashBox.addToCashBoxPocket(coin);
        //then
        assertThat(cashBox.get(coin.getValue())).contains(coin);
    }

    @Test
    public void shouldReturnCurrentPocketSum() throws Exception {
        //given
        cashBox.addToCurrentRequestPocket(new Coin(5.0));
        cashBox.addToCurrentRequestPocket(new Coin(2.0));
        cashBox.addToCurrentRequestPocket(new Coin(0.5));
        //when
        Double insertedCoinsValueForCurrentRequest = cashBox.getInsertedCoinsValueForCurrentRequest();
        //then
        assertThat(insertedCoinsValueForCurrentRequest).isEqualTo(7.5);
    }

    @Test
    public void shouldReturnTrueIfCashBoxHasEnoughMoneyToReturnChange() throws Exception {
        //given
        cashBox.addToCurrentRequestPocket(new Coin(5.0));
        cashBox.addToCashBoxPocket(new Coin(0.5));
        double price = 4.5;
        //when
        boolean couldReturnChangeForRequest = cashBox.isAbleToReturnChangeFor(price);
        //then
        assertThat(couldReturnChangeForRequest).isTrue();
    }

    @Test
    public void shouldReturnFalseIfCashBoxHasNotEnoughMoneyToReturnChange() throws Exception {
        //given
        cashBox.addToCurrentRequestPocket(new Coin(5.0));
        cashBox.addToCashBoxPocket(new Coin(0.1));
        cashBox.addToCashBoxPocket(new Coin(0.2));
        cashBox.addToCashBoxPocket(new Coin(0.1));
        double price = 4.5;
        //when
        boolean ableToReturnChangeFor = cashBox.isAbleToReturnChangeFor(price);
        //then
        assertThat(ableToReturnChangeFor).isFalse();
    }

    @Test
    public void shouldReturnTrueIfCashBoxHasEnoughMoneyToReturnChange_2() throws Exception {
        //given
        cashBox.addToCurrentRequestPocket(new Coin(2.0));
        cashBox.addToCurrentRequestPocket(new Coin(2.0));
        cashBox.addToCashBoxPocket(new Coin(0.1));
        cashBox.addToCashBoxPocket(new Coin(0.1));
        cashBox.addToCashBoxPocket(new Coin(0.2));
        cashBox.addToCashBoxPocket(new Coin(0.2));
        double price = 3.5;
        //when
        boolean ableToReturnChangeFor = cashBox.isAbleToReturnChangeFor(price);
        //then
        assertThat(ableToReturnChangeFor).isTrue();
    }

    @Test
    public void shouldReturnTrueIfCashBoxHasEnoughMoneyToReturnChange_3() throws Exception {
        //given
        cashBox.addToCurrentRequestPocket(new Coin(1.0));
        cashBox.addToCashBoxPocket(new Coin(5.0));
        cashBox.addToCashBoxPocket(new Coin(2.0));
        cashBox.addToCashBoxPocket(new Coin(1.0));
        cashBox.addToCashBoxPocket(new Coin(0.5));
        cashBox.addToCashBoxPocket(new Coin(0.2));
        cashBox.addToCashBoxPocket(new Coin(0.1));
        double price = 0.2;
        //when
        boolean ableToReturnChangeFor = cashBox.isAbleToReturnChangeFor(price);
        //then
        assertThat(ableToReturnChangeFor).isTrue();
    }

    @Test
    public void shouldReturnTrueWhenReturningCoinsAlsoFromCurrentRequestPocket() throws Exception {
        //given
        cashBox.addToCurrentRequestPocket(new Coin(0.5));
        cashBox.addToCurrentRequestPocket(new Coin(2.0));
        double price = 2.0;
        //when
        boolean ableToReturnChangeFor = cashBox.isAbleToReturnChangeFor(price);
        //then
        assertThat(ableToReturnChangeFor).isTrue();
    }

    @Test
    public void shouldReturnCoinsFromCashBoxEqualsToRequestedValue() throws Exception {
        //given
        Coin first = new Coin(0.5);
        cashBox.addToCashBoxPocket(first);
        Coin sec = new Coin(2.0);
        cashBox.addToCashBoxPocket(sec);
        //when
        List<Coin> coins = cashBox.withdrawCoinsFor(2.5);
        //then
        assertThat(coins).contains(first);
        assertThat(coins).contains(sec);
    }

}
