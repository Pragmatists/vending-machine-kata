package tdd.vendingMachine;

import java.math.BigDecimal;
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
        return getCoinsValue(getCoinsForValue(value)).compareTo(value) == 0;
    }

    private Map<Coin, Integer> getCoinsForValue(BigDecimal value) {
        Map<Coin, Integer> coinCountMap = new EnumMap<>(Coin.class);
        BigDecimal remainingAmount = value;

        List<Coin> denominationDescOrderList = prepareAvailableCoinsInDenominationDescOrder();
        for (int i = 0; i < denominationDescOrderList.size(); i++) {
            Coin currentCoin = denominationDescOrderList.get(i);
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

        return coinCountMap;
    }

    private List<Coin> prepareAvailableCoinsInDenominationDescOrder() {
        return availableCoinMap.keySet().stream()
            .sorted((c1, c2) -> c2.getDenomination().compareTo(c1.getDenomination()))
            .collect(Collectors.toList());
    }

    private BigDecimal getCoinsValue(Map<Coin, Integer> coinCountMap) {
        return coinCountMap.entrySet().stream()
            .map(this::getEntryValue)
            .reduce(BigDecimal::add)
            .get();
    }

    private BigDecimal getEntryValue(Map.Entry<Coin, Integer> entry) {
        return entry.getKey().getDenomination().multiply(BigDecimal.valueOf(entry.getValue()));
    }
}
