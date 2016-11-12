package tdd.vendingMachine.util;

import org.junit.Test;
import tdd.vendingMachine.domain.money.Coins;

import java.util.EnumMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ChangeCalculatorTest {

    @Test
    public void should_be_able_to_accurately_calculate_change() {
        EnumMap<Coins, Integer> change = ChangeCalculator.calculateChange(getMoneyBox(), 38);

        assertEquals(Integer.valueOf(0), change.get(Coins.COIN_5));
        assertEquals(Integer.valueOf(1), change.get(Coins.COIN_2));
        assertEquals(Integer.valueOf(1), change.get(Coins.COIN_1));
        assertEquals(Integer.valueOf(1), change.get(Coins.COIN_0_5));
        assertEquals(Integer.valueOf(1), change.get(Coins.COIN_0_2));
        assertEquals(Integer.valueOf(1), change.get(Coins.COIN_0_1));

        change = ChangeCalculator.calculateChange(getMoneyBox(), 43);

        assertEquals(Integer.valueOf(0), change.get(Coins.COIN_5));
        assertEquals(Integer.valueOf(1), change.get(Coins.COIN_2));
        assertEquals(Integer.valueOf(1), change.get(Coins.COIN_1));
        assertEquals(Integer.valueOf(1), change.get(Coins.COIN_0_5));
        assertEquals(Integer.valueOf(2), change.get(Coins.COIN_0_2));
        assertEquals(Integer.valueOf(4), change.get(Coins.COIN_0_1));
    }

    @Test
    public void should_return_null_when_impossible_to_give_back_change() {
        assertNull(ChangeCalculator.calculateChange(getMoneyBox(), 100));
    }

    private EnumMap getMoneyBox() {
        return new EnumMap<Coins, Integer>(Coins.class) {{
            put(Coins.COIN_0_1, 10);
            put(Coins.COIN_0_2, 2);
            put(Coins.COIN_0_5, 1);
            put(Coins.COIN_1, 1);
            put(Coins.COIN_2, 1);
            put(Coins.COIN_5, 0);
        }};
    }
}
