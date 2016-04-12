package tdd.vendingMachine.domain.parts;

import tdd.vendingMachine.domain.parts.money.Coin;

import java.util.List;
import java.util.Map;

public class CoinDispenser {

    private Map<Coin, Integer> coinsInside;

    public CoinDispenser(Map<Coin, Integer> coinsInside) {
        this.coinsInside = coinsInside;
    }

    public Integer getCoinsCount(Coin coinType) {
        return coinsInside.getOrDefault(coinType, 0);
    }

    public void decreaseCoinCountersAccordingToChange(Map<Coin, Integer> change) {
        coinsInside.forEach((coin, integer) -> coinsInside.put(coin, (integer - change.getOrDefault(coin, 0))));
    }

    public Map<Coin, Integer> getCoinsInside() {
        return coinsInside;
    }

    public void takeCoins(List<Coin> coinsToTake) {
        coinsToTake.stream()
            .forEach(coin -> coinsInside.put(coin, coinsInside.getOrDefault(coin, 0) + 1));
    }
}
