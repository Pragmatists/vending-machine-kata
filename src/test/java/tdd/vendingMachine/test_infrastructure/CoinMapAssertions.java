package tdd.vendingMachine.test_infrastructure;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Money;

import java.util.Map;

public class CoinMapAssertions extends AbstractAssert<CoinMapAssertions, Map<Coin, Integer>> {

    public CoinMapAssertions(Map<Coin, Integer> actual) {
        super(actual, CoinMapAssertions.class);
    }

    public static CoinMapAssertions assertThat(Map<Coin, Integer> actual) {
        return new CoinMapAssertions(actual);
    }

    public CoinMapAssertions totalValueEquals(Money expected) {
        Assertions.assertThat(actual.entrySet()
            .stream()
            .map(entry -> entry.getKey().getDenomination().multiply(entry.getValue()))
            .reduce(Money::add).get()).isEqualTo(expected);
        return new CoinMapAssertions(actual);
    }
}
