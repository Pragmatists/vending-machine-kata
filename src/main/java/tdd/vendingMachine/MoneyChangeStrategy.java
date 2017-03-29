package tdd.vendingMachine;

import tdd.vendingMachine.exception.MoneyChangeException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * MoneyChangeStrategy count the change of amount from given coins set
 *
 * @author kdkz
 */
@FunctionalInterface
public interface MoneyChangeStrategy {

    /**
     * Counts change of amount from given coins set. When correct change count is not possible
     * MoneyChangeException is thrown
     *
     * @param toChange       amount to change
     * @param coinsInCashier coins and their count map
     * @return coins and their count change
     */
    Map<Denomination, Integer> countRestInCoinsQuantity(BigDecimal toChange, Map<Denomination, Integer> coinsInCashier) throws MoneyChangeException;

}
