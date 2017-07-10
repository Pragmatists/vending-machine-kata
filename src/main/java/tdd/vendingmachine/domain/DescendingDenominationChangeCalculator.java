package tdd.vendingmachine.domain;

import java.util.Optional;

class DescendingDenominationChangeCalculator implements ChangeCalculator {

    @Override
    public Optional<Change> calculate(Coins availableCoins, Money changeValue) {
        Coins initialChangeCoins = Coins.empty();
        return Optional.ofNullable(calculateChange(initialChangeCoins, changeValue, availableCoins));
    }

    private Change calculateChange(Coins changeCoins, Money remainingChangeValue, Coins availableCoins) {
        if (remainingChangeValue.isZero()) {
            return Change.of(changeCoins);
        }
        return availableCoins.maxValueCoinAndAtMost(remainingChangeValue)
                             .map(coin -> addCoinAndRecalculate(coin, availableCoins, remainingChangeValue, changeCoins))
                             .orElse(null);
    }

    private Change addCoinAndRecalculate(Coin coin, Coins availableCoins, Money remainingChangeValue,
                                         Coins changeCoins) {
        changeCoins = changeCoins.add(coin);
        remainingChangeValue = remainingChangeValue.subtract(coin.value());
        availableCoins = availableCoins.remove(coin);
        return calculateChange(changeCoins, remainingChangeValue, availableCoins);
    }
}
