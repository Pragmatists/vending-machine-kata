package tdd.vendingMachine.util;

import org.junit.Test;
import tdd.vendingMachine.domain.money.Coins;
import tdd.vendingMachine.domain.money.MoneyBox;

import java.util.EnumMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ChangeCalculatorTest {

    @Test
    public void should_be_able_to_accurately_calculate_change() {
        MoneyBox change = ChangeCalculator.calculateChange(getMoneyBox(), 38);

        assertEquals(0, change.getCoinCount(Coins.COIN_5));
        assertEquals(1, change.getCoinCount(Coins.COIN_2));
        assertEquals(1, change.getCoinCount(Coins.COIN_1));
        assertEquals(1, change.getCoinCount(Coins.COIN_0_5));
        assertEquals(1, change.getCoinCount(Coins.COIN_0_2));
        assertEquals(1, change.getCoinCount(Coins.COIN_0_1));

        change = ChangeCalculator.calculateChange(getMoneyBox(), 43);

        assertEquals(0, change.getCoinCount(Coins.COIN_5));
        assertEquals(1, change.getCoinCount(Coins.COIN_2));
        assertEquals(1, change.getCoinCount(Coins.COIN_1));
        assertEquals(1, change.getCoinCount(Coins.COIN_0_5));
        assertEquals(2, change.getCoinCount(Coins.COIN_0_2));
        assertEquals(4, change.getCoinCount(Coins.COIN_0_1));

        change = ChangeCalculator.calculateChange(getMoneyBox(), 0);

        assertEquals(0, change.getCoinCount(Coins.COIN_5));
        assertEquals(0, change.getCoinCount(Coins.COIN_2));
        assertEquals(0, change.getCoinCount(Coins.COIN_1));
        assertEquals(0, change.getCoinCount(Coins.COIN_0_5));
        assertEquals(0, change.getCoinCount(Coins.COIN_0_2));
        assertEquals(0, change.getCoinCount(Coins.COIN_0_1));
    }

    @Test
    public void should_return_null_when_impossible_to_give_back_change() {
        assertNull(ChangeCalculator.calculateChange(getMoneyBox(), 100));
    }

    private MoneyBox getMoneyBox() {
        MoneyBox box = new MoneyBox();
        box
            .insert(Coins.COIN_0_1, 10)
            .insert(Coins.COIN_0_2, 2)
            .insert(Coins.COIN_0_5, 1)
            .insert(Coins.COIN_1, 1)
            .insert(Coins.COIN_2, 1)
            .insert(Coins.COIN_5, 0);

        return box;
    }
}
