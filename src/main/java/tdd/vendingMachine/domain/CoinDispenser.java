package tdd.vendingMachine.domain;

import java.util.HashMap;
import java.util.Map;

public class CoinDispenser {

    // Inverted coin values array to make the algorithm optimal.
    private static final Coin[] coinTypes = {Coin.COIN_5, Coin.COIN_2, Coin.COIN_1, Coin.COIN_0_5, Coin.COIN_0_2, Coin.COIN_0_1};

    private Map<Coin, Integer> coinsInside;

    public CoinDispenser(Map<Coin, Integer> coinsInside) {
        this.coinsInside = coinsInside;
    }

    public Map<Coin, Integer> calculateChange(Money needed) {
        Map<Coin, Integer> change = new HashMap<>();
        for (Coin coinType : coinTypes) {
            if (coinsInside.getOrDefault(coinType, 0) > 0)
                while (needed.isGreaterOrEqualTo(coinType.getDenomination())) {
                    needed = needed.subtract(coinType.getDenomination());
                    addOneMoreCoin(change, coinType);
                }
        }
        return change;
    }

    private void addOneMoreCoin(Map<Coin, Integer> change, Coin coinType) {
        Integer coinsCount = change.get(coinType);
        change.put(coinType, coinsCount == null ? 1 : coinsCount + 1);
    }

    public Integer getCoinsCount(Coin coinType) {
        return coinsInside.getOrDefault(coinType, 0);
    }

    public void decreaseCoinsCountersAccordingToChange(Map<Coin, Integer> change) {
        coinsInside.forEach((coin, integer) -> coinsInside.put(coin, (integer - change.getOrDefault(coin, 0))));
    }

}
