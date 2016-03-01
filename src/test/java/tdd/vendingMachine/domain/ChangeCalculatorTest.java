package tdd.vendingMachine.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static tdd.vendingMachine.domain.Money.createMoney;

public class ChangeCalculatorTest {

    private Map<Coin, Integer> availableCoins;

    private ChangeCalculator calculator;

    @Before
    public void setUp() throws Exception {
        calculator = new ChangeCalculator();
        availableCoins = new HashMap<>();
    }

    @Test
    public void should_calculate_change_when_the_coin_type_is_available() throws Exception {
        availableCoins.put(Coin.COIN_2, 1);

        Map<Coin, Integer> change = calculator.calculateChange(createMoney("2"), availableCoins).get();

        assertThat(change.get(Coin.COIN_2)).isEqualTo(1);
    }

    @Test
    public void should_calculate_change_considering_present_coins() throws Exception {
        availableCoins.put(Coin.COIN_1, 2);
        availableCoins.put(Coin.COIN_0_5, 1);

        Map<Coin, Integer> change = calculator.calculateChange(createMoney("2.5"), availableCoins).get();

        assertThat(change.get(Coin.COIN_1)).isEqualTo(2);
        assertThat(change.get(Coin.COIN_0_5)).isEqualTo(1);
    }

    @Test
    public void should_inform_if_cannot_calculate_change() throws Exception {
        availableCoins.put(Coin.COIN_1, 2);

        Optional change = calculator.calculateChange(createMoney("1.5"), availableCoins);

        assertThat(change.isPresent()).isFalse();
    }
}

