package tdd.vendingmachine.domain;

import lombok.ToString;
import tdd.vendingmachine.domain.dto.CoinDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ToString
class ChangeDispenser {

    private List<Coin> coins;

    private ChangeDispenser(List<Coin> coins) {
        Objects.requireNonNull(coins);
        this.coins = new ArrayList<>(coins);
    }

    static ChangeDispenser empty() {
        return new ChangeDispenser(Collections.emptyList());
    }

    void put(Coin coin) {
        coins.add(coin);
    }

    Collection<CoinDto> dispense() {
        List<CoinDto> dispensedCoins = coins.stream().map(Coin::toDto).collect(Collectors.toList());
        coins.clear();
        return dispensedCoins;
    }
}
