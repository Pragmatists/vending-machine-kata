package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class CoinWallet {

    private static final int EMPTY = 0;

    private final Map<Coin, Integer> coinCountMap = new EnumMap<>(Coin.class);

    public CoinWallet() {
        Stream.of(Coin.values()).forEach(c -> coinCountMap.put(c, EMPTY));
    }

    public boolean hasCoin(Coin coin) {
        return coinCountMap.get(coin) > EMPTY;
    }

    public CoinWallet putCoin(Coin coin) {
        return putCoin(coin, 1);
    }

    public CoinWallet putCoin(Coin coin, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Illegal quantity argument: " + quantity);
        }

        coinCountMap.compute(coin, (c, v) -> v + quantity);
        return this;
    }

    public Map<Coin, Integer> removeValueInCoins(BigDecimal value) {
        Map<Coin, Integer> coinsToRemove = calculator().getCoinsForValue(value);
        if (coinsToRemove.isEmpty()) {
            throw new IllegalStateException("Not enough coins in wallet for value " + value);
        }

        coinsToRemove.forEach(this::removeCoins);
        return coinsToRemove;
    }

    private void removeCoins(Coin coin, Integer count) {
        coinCountMap.compute(coin, (c, q) -> q - count);
    }

    public CoinCalculator calculator() {
        return new CoinCalculator(coinCountMap);
    }
}
