package tdd.vendingmachine.domain;

import lombok.ToString;
import tdd.vendingmachine.domain.dto.CoinDto;

import java.util.Collection;
import java.util.Objects;

@ToString
class ChangeDispenser {

    private final Coins coins;

    private ChangeDispenser(Coins coins) {
        this.coins = Objects.requireNonNull(coins);
    }

    static ChangeDispenser empty() {
        return new ChangeDispenser(Coins.empty());
    }

    static ChangeDispenser of(ChangeDispenser changeDispenser) {
        return new ChangeDispenser(changeDispenser.coins);
    }

    ChangeDispenser put(Coin coin) {
        return new ChangeDispenser(coins.add(coin));
    }

    ChangeDispenser put(Coins changeCoins) {
        return new ChangeDispenser(coins.add(changeCoins));
    }

    Collection<CoinDto> dispense() {
        return coins.toDto();
    }
}
