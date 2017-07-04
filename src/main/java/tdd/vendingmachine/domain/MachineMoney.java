package tdd.vendingmachine.domain;

import lombok.ToString;

import java.util.Objects;

@ToString
class MachineMoney {

    private final Denominations acceptableDenominations;

    private MachineMoney(Denominations acceptableDenominations) {
        this.acceptableDenominations = Objects.requireNonNull(acceptableDenominations);
    }

    static MachineMoney createEmptyAndAcceptingDenominations(Denominations denominations) {
        return new MachineMoney(denominations);
    }

    boolean isCoinAcceptable(Coin coin) {
        return acceptableDenominations.contains(coin.denomination());
    }
}
