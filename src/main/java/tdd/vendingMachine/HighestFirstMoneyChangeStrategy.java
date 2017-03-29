package tdd.vendingMachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    final static Logger log = LoggerFactory.getLogger(HighestFirstMoneyChangeStrategy.class);

    /**
     * Uses highest first strategy for count the change
     *
     * @see MoneyChangeStrategy#countRestInCoinsQuantity(BigDecimal, Map)
     */
    public Map<Denomination, Integer> countRestInCoinsQuantity(BigDecimal toChange, Map<Denomination, Integer> coinsInCashier) throws MoneyChangeException {

        log.debug("Counting rest - started.");
        BigDecimal rest = toChange.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        //set for ensure the enum order
        Map<Denomination, Integer> coinsQuantity = new TreeMap<>(coinsInCashier);

        Map<Denomination, Integer> restDenominations = new TreeMap<>();

        for (Map.Entry<Denomination, Integer> entry : coinsQuantity.entrySet()) {
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
            log.error("Failed to count the rest. Not enough coins");
            throw new MoneyChangeException("Failed to count the rest. Not enough coins");
        }
        log.debug("Rest counted successfully. Rest coins quantity: {}", restDenominations);
        log.debug("Counting rest - finished.");
        return restDenominations;
    }
}
