package tdd.vendingmachine.domain;

import lombok.ToString;

import java.util.Objects;

@ToString
class Change {

    private final Coins coins;

    private Change(Coins coins) {
        this.coins = Objects.requireNonNull(coins);
    }

    static Change of(Coins coins) {
        return new Change(coins);
    }

    Coins coins() {
        return coins;
    }
}
