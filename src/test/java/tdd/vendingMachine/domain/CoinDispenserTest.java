package tdd.vendingMachine.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static tdd.vendingMachine.domain.Money.createMoney;

public class CoinDispenserTest {

    private Map<Coin, Integer> availableCoins;

    @Before
    public void setUp() throws Exception {
        availableCoins = new HashMap<>();
    }

    @Test
    public void should_calculate_change_when_the_coin_type_is_available() throws Exception {
        availableCoins.put(Coin.COIN_2, 1);
        CoinDispenser coinDispenser = new CoinDispenser(availableCoins);

        Map<Coin, Integer> change = coinDispenser.calculateChange(createMoney("2")).get();

        assertThat(change.get(Coin.COIN_2)).isEqualTo(1);
    }

    @Test
    public void should_calculate_change_considering_present_coins() throws Exception {
        availableCoins.put(Coin.COIN_1, 2);
        availableCoins.put(Coin.COIN_0_5, 1);
        CoinDispenser coinDispenser = new CoinDispenser(availableCoins);

        Map<Coin, Integer> change = coinDispenser.calculateChange(createMoney("2.5")).get();

        assertThat(change.get(Coin.COIN_1)).isEqualTo(2);
        assertThat(change.get(Coin.COIN_0_5)).isEqualTo(1);
    }

    @Test
    public void should_get_available_coins_count() throws Exception {
        int coinCount = 2;
        availableCoins.put(Coin.COIN_1, coinCount);
        CoinDispenser coinDispenser = new CoinDispenser(availableCoins);

        assertThat(coinDispenser.getCoinsCount(Coin.COIN_1)).isEqualTo(coinCount);
    }

    @Test
    public void should_decrease_coins_count_according_to_change() throws Exception {
        availableCoins.put(Coin.COIN_1, 2);
        availableCoins.put(Coin.COIN_0_5, 1);
        CoinDispenser dispenser = new CoinDispenser(availableCoins);

        Map<Coin, Integer> change = dispenser.calculateChange(createMoney("2.5")).get();
        dispenser.decreaseCoinCountersAccordingToChange(change);

        assertThat(dispenser.getCoinsCount(Coin.COIN_1)).isEqualTo(0);
        assertThat(dispenser.getCoinsCount(Coin.COIN_0_5)).isEqualTo(0);
    }

    @Test
    public void should_inform_if_cannot_calculate_change() throws Exception {
        availableCoins.put(Coin.COIN_1, 2);
        CoinDispenser dispenser = new CoinDispenser(availableCoins);

        Optional change = dispenser.calculateChange(createMoney("1.5"));

        assertThat(change.isPresent()).isFalse();
    }
}
