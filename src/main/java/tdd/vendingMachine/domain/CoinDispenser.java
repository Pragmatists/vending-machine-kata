package tdd.vendingMachine.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static tdd.vendingMachine.domain.Money.createMoney;

public class CoinDispenser {

    // Inverted coin values array to make the algorithm optimal.
    private static final Coin[] coinTypes = {Coin.COIN_5, Coin.COIN_2, Coin.COIN_1, Coin.COIN_0_5, Coin.COIN_0_2, Coin.COIN_0_1};

    private Map<Coin, Integer> coinsInside;

    public CoinDispenser(Map<Coin, Integer> coinsInside) {
        this.coinsInside = coinsInside;
    }

    public Optional<Map<Coin, Integer>> calculateChange(Money needed) {
        Map<Coin, Integer> coinsInsideRightNow = new HashMap<>(coinsInside);
        Map<Coin, Integer> change = new HashMap<>();
        for (Coin coinType : coinTypes) {
            if (coinsInsideRightNow.getOrDefault(coinType, 0) > 0)
                while (needed.isGreaterOrEqualTo(coinType.getDenomination()) &&
                    coinsInsideRightNow.getOrDefault(coinType, 0) > 0) {

                    needed = needed.subtract(coinType.getDenomination());
                    addOneMoreCoin(change, coinType);
                    coinsInsideRightNow.put(coinType, coinsInsideRightNow.get(coinType) - 1);
                }
        }

        if (needed.equals(createMoney("0"))) {
            return Optional.of(change);
        } else {
            return Optional.empty();
        }
    }

    private void addOneMoreCoin(Map<Coin, Integer> change, Coin coinType) {
        Integer coinsCount = change.get(coinType);
        change.put(coinType, coinsCount == null ? 1 : coinsCount + 1);
    }

    public Integer getCoinsCount(Coin coinType) {
        return coinsInside.getOrDefault(coinType, 0);
    }

    public void decreaseCoinCountersAccordingToChange(Map<Coin, Integer> change) {
        coinsInside.forEach((coin, integer) -> coinsInside.put(coin, (integer - change.getOrDefault(coin, 0))));
    }

}
