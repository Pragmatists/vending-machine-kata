package tdd.vendingMachine.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import tdd.vendingMachine.domain.currency.Coins;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MoneyBoxTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void should_be_initialized_with_empty_coin_counts() {
        MoneyBox box = new MoneyBox();

        assertEquals(0, box.getCoinCount(Coins.COIN_0_1));
        assertEquals(0, box.getCoinCount(Coins.COIN_0_2));
        assertEquals(0, box.getCoinCount(Coins.COIN_0_5));
        assertEquals(0, box.getCoinCount(Coins.COIN_1));
        assertEquals(0, box.getCoinCount(Coins.COIN_2));
        assertEquals(0, box.getCoinCount(Coins.COIN_5));
    }

    @Test
    public void should_be_possible_to_insert_coins() {
        MoneyBox box = new MoneyBox();

        box.insert(Coins.COIN_0_1, 1);

        assertEquals(1, box.getCoinCount(Coins.COIN_0_1));
        assertFalse(box.isEmpty(Coins.COIN_0_1));
    }

    @Test
    public void should_be_possible_to_remove_coins() {
        MoneyBox box = new MoneyBox();

        box.insert(Coins.COIN_0_1, 1);
        assertEquals(1, box.getCoinCount(Coins.COIN_0_1));

        box.remove(Coins.COIN_0_1, 1);
        assertEquals(0, box.getCoinCount(Coins.COIN_0_1));
        assertTrue(box.isEmpty(Coins.COIN_0_1));
    }

    @Test
    public void should_throw_exception_when_more_than_available_amount_is_to_be_removed() {
        MoneyBox box = new MoneyBox();

        box.insert(Coins.COIN_0_1, 1);
        assertEquals(1, box.getCoinCount(Coins.COIN_0_1));

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Not enough coins");

        box.remove(Coins.COIN_0_1, 2);
    }
}
