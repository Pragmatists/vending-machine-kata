package tdd.vendingmachine.domain;

import lombok.ToString;
import tdd.vendingmachine.domain.dto.CoinDto;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ToString
class ChangeDispenser {

    private Coins coins;

    private ChangeDispenser(Coins coins) {
        this.coins = Objects.requireNonNull(coins);
    }

    static ChangeDispenser empty() {
        return new ChangeDispenser(Coins.empty());
    }

    static ChangeDispenser of(ChangeDispenser changeDispenser) {
        return new ChangeDispenser(changeDispenser.coins);
    }

    void put(Coin coin) {
        coins = coins.add(coin);
    }

    void put(Coins changeCoins) {
        coins = coins.add(changeCoins);
    }

    Collection<CoinDto> dispense() {
        List<CoinDto> dispensedCoins = coins.toDto();
        coins = Coins.empty();
        return dispensedCoins;
    }
}
