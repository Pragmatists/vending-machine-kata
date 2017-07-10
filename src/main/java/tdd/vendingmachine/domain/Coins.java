package tdd.vendingmachine.domain;

import com.google.common.collect.ImmutableList;
import lombok.ToString;
import tdd.vendingmachine.domain.dto.CoinDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ToString
class Coins {

    private final List<Coin> coins;

    private Coins(List<Coin> coins) {
        this.coins = Collections.unmodifiableList(Objects.requireNonNull(coins));
    }

    static Coins empty() {
        return new Coins(Collections.emptyList());
    }

    static Coins create(List<CoinDto> coins) {
        return new Coins(coins.stream().map(Coin::create).collect(Collectors.toList()));
    }

    Coins add(Coin coin) {
        return new Coins(ImmutableList.<Coin>builder()
            .addAll(coins)
            .add(coin)
            .build());
    }

    Coins add(Coins coins) {
        return new Coins(ImmutableList.<Coin>builder()
            .addAll(this.coins)
            .addAll(coins.coins)
            .build());
    }

    Coins remove(Coins coins) {
        List<Coin> coinsCopy = new ArrayList<>(this.coins);
        coins.coins.forEach(coinsCopy::remove);
        return new Coins(coinsCopy);
    }

    Coins remove(Coin coin) {
        List<Coin> coinsCopy = new ArrayList<>(this.coins);
        coinsCopy.remove(coin);
        return new Coins(coinsCopy);
    }

    Money moneyValue() {
        return coins.stream()
                    .map(Coin::value)
                    .reduce(Money.zero(), Money::add);
    }

    boolean existInDenominations(Denominations denominations) {
        return coins.stream()
                    .allMatch(coin -> denominations.contain(coin.denomination()));
    }

    List<CoinDto> toDto() {
        return coins.stream().map(Coin::toDto).collect(Collectors.toList());
    }

    Optional<Coin> maxValueCoinAndAtMost(Money moneyValue) {
        return coins.stream()
                    .filter(coin -> coin.value().isNotMoreThan(moneyValue))
                    .max(Comparator.comparing(Coin::value));
    }
}
