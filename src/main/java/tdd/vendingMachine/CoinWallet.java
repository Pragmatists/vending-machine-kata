package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class CoinWallet {

    private static final int EMPTY = 0;

    private static final List<Coin> coinListInDenominationDescOrder = EnumSet.allOf(Coin.class).stream()
        .sorted((c1, c2) -> c2.getDenomination().compareTo(c1.getDenomination()))
        .collect(Collectors.toList());

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

    public boolean hasChange(BigDecimal change) {
        final int walletValueToChangeCompare = getValue().compareTo(change);
        return walletValueToChangeCompare == 0 || (walletValueToChangeCompare == 1 && hasCoinsForChange(change));
    }

    public BigDecimal getValue() {
        return coinCountMap.entrySet().stream()
            .map(this::getEntryValue)
            .reduce(BigDecimal::add).get();
    }

    private BigDecimal getEntryValue(Map.Entry<Coin, Integer> entry) {
        return entry.getKey().getDenomination().multiply(BigDecimal.valueOf(entry.getValue()));
    }

    private boolean hasCoinsForChange(BigDecimal change) {
        Map<Coin, Integer> coinCountTempMap = new EnumMap<>(coinCountMap);
        BigDecimal remainingChange = change.abs();

        int currentDenominationIndex = 0;
        while (remainingChange.compareTo(BigDecimal.ZERO) == 1) {
            if (currentDenominationIndex >= coinListInDenominationDescOrder.size()) {
                return false;
            }

            Coin currentCoin = coinListInDenominationDescOrder.get(currentDenominationIndex);
            if (coinCountTempMap.get(currentCoin) == EMPTY) {
                currentDenominationIndex++;
                continue;
            }

            BigDecimal currentDenominationValue = currentCoin.getDenomination();
            if (remainingChange.compareTo(currentDenominationValue) >= 0) {
                final int availableCoinCount = coinCountTempMap.get(currentCoin);
                final int maxCoinCount = remainingChange.divideToIntegralValue(currentDenominationValue).intValue();
                final int usedCoinCount = availableCoinCount > maxCoinCount ? maxCoinCount : availableCoinCount;
                remainingChange = remainingChange.subtract(currentDenominationValue.multiply(BigDecimal.valueOf(usedCoinCount)));
            }

            currentDenominationIndex++;
        }

        return true;
    }
}
