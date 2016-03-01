package tdd.vendingMachine.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static tdd.vendingMachine.domain.Money.createMoney;

public class ChangeCalculator {

    // Inverted coin values array to make the algorithm optimal.
    private static final Coin[] coinTypes = {Coin.COIN_5, Coin.COIN_2, Coin.COIN_1, Coin.COIN_0_5, Coin.COIN_0_2, Coin.COIN_0_1};

    public Optional<Map<Coin, Integer>> calculateChange(Money neededChangeValue, Map<Coin, Integer> consideredCoins) {
        Map<Coin, Integer> change = new HashMap<>();

        for (Coin coinType : coinTypes) {
            while (neededChangeValue.isGreaterOrEqualTo(coinType.getDenomination()) &&
                neededCoinsPresent(consideredCoins, coinType)) {

                excludeCoinFromConsideredCoins(consideredCoins, coinType);
                addCoinToChange(change, coinType);
                neededChangeValue = neededChangeValue.subtract(coinType.getDenomination());
            }
        }
        return neededChangeValue.equals(createMoney("0")) ? Optional.of(change) : Optional.empty();
    }

    private void excludeCoinFromConsideredCoins(Map<Coin, Integer> coinsToChooseFrom, Coin coinType) {
        coinsToChooseFrom.put(coinType, coinsToChooseFrom.get(coinType) - 1);
    }

    private void addCoinToChange(Map<Coin, Integer> change, Coin coinType) {
        Integer coinsCount = change.get(coinType);
        change.put(coinType, coinsCount == null ? 1 : coinsCount + 1);
    }

    private boolean neededCoinsPresent(Map<Coin, Integer> coinsToChooseFrom, Coin coinType) {
        return coinsToChooseFrom.getOrDefault(coinType, 0) > 0;
    }
}
