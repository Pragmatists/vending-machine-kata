package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class CoinCalculator {

    private final Map<Coin, Integer> availableCoinMap;

    public CoinCalculator(Map<Coin, Integer> availableCoinMap) {
        this.availableCoinMap = new EnumMap<>(availableCoinMap);
    }

    public boolean hasCoinsForValue(BigDecimal value) {
        return !getCoinsForValue(value).isEmpty();
    }

    public Map<Coin, Integer> getCoinsForValue(BigDecimal value) {
        Map<Coin, Integer> coinCountMap = new EnumMap<>(Coin.class);
        BigDecimal remainingAmount = value;

        List<Coin> denominationDescOrderList = prepareAvailableCoinsInDenominationDescOrder();
        for (Coin currentCoin : denominationDescOrderList) {
            BigDecimal currentDonimnationValue = currentCoin.getDenomination();
            if (remainingAmount.compareTo(currentDonimnationValue) < 0) {
                continue;
            }

            final int availableCoins = availableCoinMap.get(currentCoin);
            final int maxNeededCoins = remainingAmount.divideToIntegralValue(currentDonimnationValue).intValue();
            final int usedCoins = availableCoins > maxNeededCoins ? maxNeededCoins : availableCoins;
            coinCountMap.put(currentCoin, usedCoins);

            remainingAmount = remainingAmount.subtract(currentDonimnationValue.multiply(BigDecimal.valueOf(usedCoins)));
            if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
        }

        if (getCoinsValue(coinCountMap).compareTo(value) == -1) {
            return Collections.emptyMap();
        }

        return coinCountMap;
    }

    public BigDecimal getCoinsValue() {
        return getCoinsValue(availableCoinMap);
    }

    private BigDecimal getCoinsValue(Map<Coin, Integer> coinCountMap) {
        return coinCountMap.entrySet().stream()
            .map(this::getEntryValue)
            .reduce(BigDecimal::add)
            .get();
    }

    private List<Coin> prepareAvailableCoinsInDenominationDescOrder() {
        return availableCoinMap.keySet().stream()
            .sorted((c1, c2) -> c2.getDenomination().compareTo(c1.getDenomination()))
            .collect(Collectors.toList());
    }

    private BigDecimal getEntryValue(Map.Entry<Coin, Integer> entry) {
        return entry.getKey().getDenomination().multiply(BigDecimal.valueOf(entry.getValue()));
    }
}
