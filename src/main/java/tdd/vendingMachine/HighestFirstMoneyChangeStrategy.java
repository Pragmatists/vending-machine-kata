package tdd.vendingMachine;

import tdd.vendingMachine.exception.MoneyChangeException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * HighestFirstMoneyChangeStrategy implementation of {@link MoneyChangeStrategy} which count change using
 * highest denominations first.
 *
 * @author kdkz
 */
public class HighestFirstMoneyChangeStrategy implements MoneyChangeStrategy {

    /**
     * Uses highest first strategy for count the change
     *
     * @see MoneyChangeStrategy#countRestInCoinsQuantity(BigDecimal, Map)
     */
    public Map<Denomination, Integer> countRestInCoinsQuantity(BigDecimal toChange, Map<Denomination, Integer> coinsInCashier) throws MoneyChangeException {

        BigDecimal rest = toChange.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        Map<Denomination, Integer> restDenominations = new TreeMap<>();

        for (Map.Entry<Denomination, Integer> entry : coinsInCashier.entrySet()) {
            BigDecimal denomination = entry.getKey().getDenomination().setScale(2, BigDecimal.ROUND_HALF_EVEN);
            int quantity = entry.getValue();
            if (rest.compareTo(denomination) >= 0 && quantity > 0) {
                int coinsNeeded = rest.divide(denomination, BigDecimal.ROUND_FLOOR).intValue();
                if (coinsNeeded > quantity) {
                    coinsNeeded = quantity;
                }
                restDenominations.put(entry.getKey(), coinsNeeded);
                rest = rest.subtract(denomination.multiply(BigDecimal.valueOf(coinsNeeded)));
            }
        }
        if (!rest.equals(BigDecimal.ZERO.setScale(2))) {
            throw new MoneyChangeException("Failed to count the rest.");
        }
        return restDenominations;
    }
}
