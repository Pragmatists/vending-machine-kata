package tdd.vendingmachine.domain;

import lombok.ToString;

import java.util.Objects;
import java.util.Optional;

@ToString
class MachineMoney {

    private final Denominations acceptableDenominations;
    private final Coins coins;
    private final ChangeCalculator changeCalculator;

    private MachineMoney(Denominations acceptableDenominations, Coins coins, ChangeCalculator changeCalculator) {
        this.acceptableDenominations = Objects.requireNonNull(acceptableDenominations);
        this.coins = Objects.requireNonNull(coins);
        if (!coins.existInDenominations(acceptableDenominations)) {
            throw new IllegalArgumentException("All coins must exist in acceptable denominations");
        }
        this.changeCalculator = changeCalculator;
    }

    boolean isCoinAcceptable(Coin coin) {
        return acceptableDenominations.contain(coin.denomination());
    }

    static MachineMoney create(Denominations acceptableDenominations, Coins coins) {
        return new MachineMoney(acceptableDenominations, coins, new DescendingDenominationChangeCalculator());
    }

    private MachineMoney changeCoins(Coins newCoins) {
        return new MachineMoney(acceptableDenominations, newCoins, changeCalculator);
    }

    MachineMoney add(Coin coin) {
        return changeCoins(coins.add(coin));
    }

    MachineMoney remove(Coins coins) {
        return changeCoins(this.coins.remove(coins));
    }

    Optional<Change> calculateChange(Money changeValue) {
        return changeCalculator.calculate(coins, changeValue);
    }
}
