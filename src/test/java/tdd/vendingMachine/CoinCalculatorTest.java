package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@RunWith(JUnitParamsRunner.class)
public class CoinCalculatorTest implements WithAssertions {

    @Test
    public void should_not_have_coins_for_value_greater_than_available() {
        // given
        Map<Coin, Integer> coinCountMap = new EnumMap<>(Coin.class);
        coinCountMap.put(Coin.COIN_2, 1);
        coinCountMap.put(Coin.COIN_1, 5);
        coinCountMap.put(Coin.COIN_0_5, 7);
        coinCountMap.put(Coin.COIN_0_1, 10);
        // when
        boolean hasCoins = new CoinCalculator(coinCountMap).hasCoinsForValue(new BigDecimal("12"));
        // then
        assertThat(hasCoins).isFalse();
    }

    @Test
    @Parameters({"1.20", "3.40", "0.80"})
    public void should_not_have_coins_if_there_is_not_enough_coins(String value) {
        Map<Coin, Integer> coinCountMap = new EnumMap<>(Coin.class);
        coinCountMap.put(Coin.COIN_2, 1);
        coinCountMap.put(Coin.COIN_1, 1);
        coinCountMap.put(Coin.COIN_0_5, 1);
        // when
        boolean hasCoins = new CoinCalculator(coinCountMap).hasCoinsForValue(new BigDecimal(value));
        // then
        assertThat(hasCoins).isFalse();
    }

    @Test
    @Parameters({"11.50", "11.10", "0.80", "1.70"})
    public void should_have_coins_for_value(String value) {
        Map<Coin, Integer> coinCountMap = new EnumMap<>(Coin.class);
        coinCountMap.put(Coin.COIN_2, 1);
        coinCountMap.put(Coin.COIN_1, 5);
        coinCountMap.put(Coin.COIN_0_5, 7);
        coinCountMap.put(Coin.COIN_0_1, 10);
        // when
        boolean hasCoins = new CoinCalculator(coinCountMap).hasCoinsForValue(new BigDecimal(value));
        // then
        assertThat(hasCoins).isTrue();
    }
}
