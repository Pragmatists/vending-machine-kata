package tdd.vendingMachine.domain.parts;

import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.domain.parts.money.Coin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CoinDispenserTest {

    private Map<Coin, Integer> coins;

    @Before
    public void setUp() throws Exception {
        coins = new HashMap<>();
    }

    @Test
    public void should_get_available_coins_count() throws Exception {
        int coinCount = 2;
        coins.put(Coin.COIN_1, coinCount);
        CoinDispenser coinDispenser = new CoinDispenser(coins);

        assertThat(coinDispenser.getCoinsCount(Coin.COIN_1)).isEqualTo(coinCount);
    }

    @Test
    public void should_decrease_coins_count_according_to_change() throws Exception {
        coins.put(Coin.COIN_1, 2);
        coins.put(Coin.COIN_0_5, 1);
        CoinDispenser dispenser = new CoinDispenser(coins);

        dispenser.decreaseCoinCountersAccordingToChange(coins);

        assertThat(dispenser.getCoinsCount(Coin.COIN_1)).isEqualTo(0);
        assertThat(dispenser.getCoinsCount(Coin.COIN_0_5)).isEqualTo(0);
    }

    @Test
    public void should_take_coins() throws Exception {
        Coin[] coinsToTake = {Coin.COIN_0_5, Coin.COIN_0_2, Coin.COIN_0_5};
        CoinDispenser dispenser = new CoinDispenser(coins);
        dispenser.takeCoins(Arrays.asList(coinsToTake));

        assertThat(dispenser.getCoinsCount(Coin.COIN_0_2)).isEqualTo(1);
        assertThat(dispenser.getCoinsCount(Coin.COIN_0_5)).isEqualTo(2);
    }
}
