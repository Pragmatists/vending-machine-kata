package tdd.vendingmachine.domain;

import java.util.Optional;

interface ChangeCalculator {
    Optional<Change> calculate(Coins availableCoins, Money changeValue);
}
