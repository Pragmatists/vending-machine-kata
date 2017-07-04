package tdd.vendingmachine.domain;

import com.google.common.collect.ImmutableList;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ToString
class Coins {

    private final List<Coin> coins;

    private Coins(List<Coin> coins) {
        this.coins = Collections.unmodifiableList(Objects.requireNonNull(coins));
    }

    static Coins empty() {
        return new Coins(Collections.emptyList());
    }

    Coins add(Coin coin) {
        return new Coins(ImmutableList.<Coin>builder()
            .addAll(coins)
            .add(coin)
            .build());
    }

    Money moneyValue() {
        return coins.stream()
                    .map(Coin::value)
                    .reduce(MoneyAmount.zero(), Money::add);
    }
}
