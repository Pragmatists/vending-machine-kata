package tdd.vendingMachine.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.strategy.MoneyChangeStrategy;
import tdd.vendingMachine.exception.MoneyChangeException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Cashier Pad which stores all inserted money and give the rest
 *
 * @author kdkz
 */
public class CashierPad {

    final static Logger log = LoggerFactory.getLogger(CashierPad.class);

    private Map<Denomination, Integer> coinsInCashier = new HashMap<>();
    private Map<Denomination, Integer> insertedCoins = new HashMap<>();

    /**
     * Inserts coins to cashier pad and returns rest coins quantity. If rest coins quantity can not be counted
     * MoneyChangeException is thrown
     *
     * @param amountToPay   amount to pay
     * @return rest
     * @throws MoneyChangeException money change exception
     */
    public Map<Denomination, Integer> payAndReturnChange(BigDecimal amountToPay, MoneyChangeStrategy changeStrategy) throws MoneyChangeException {
        log.debug("Coins inserted: {}, amount to pay: {}", insertedCoins, amountToPay);
        Map<Denomination, Integer> temporaryCoinsSum = sumInsertedCoinsWithCoinsInCashier();
        Map<Denomination, Integer> restInCoins = countRestInCoinsQuantity(amountToPay, temporaryCoinsSum, changeStrategy);
        changeCashierState(restInCoins);
        return restInCoins;
    }

    public BigDecimal insertCoins(Denomination denomination, Integer quantity) {
        if (insertedCoins.containsKey(denomination)) {
            insertedCoins.put(denomination, insertedCoins.get(denomination) + quantity);
        } else {
            insertedCoins.put(denomination, quantity);
        }
        return getAmountFromCoins(insertedCoins);
    }

    public void returnInsertedCoins() {
        insertedCoins.clear();
    }

    /**
     * Changes cashier state after successful rest counting. Adds inserted coins to cashier pad and
     * removes rest coins from cashier pad
     *
     * @param coinsToReturn coins to return
     */
    private void changeCashierState(Map<Denomination, Integer> coinsToReturn) {
        log.debug("Changing cashier state - started.");
        coinsInCashier = subtractCoinsInCashierAndRestCoins(coinsToReturn, sumInsertedCoinsWithCoinsInCashier());
        insertedCoins.clear();
        log.debug("State set to: {}. Changing cashier state - finished.", coinsInCashier);
    }

    /**
     * Counts rest coins quantity from coins quantity in cashier.
     * If rest can not be counted {@link MoneyChangeException} is thrown
     *
     * @param amountToPay    amount to pay
     * @param changeStrategy money change strategy
     * @return rest coins quantity
     * @throws MoneyChangeException money change exception
     */
    private Map<Denomination, Integer> countRestInCoinsQuantity(
        BigDecimal amountToPay,
        Map<Denomination, Integer> coinsInCashier,
        MoneyChangeStrategy changeStrategy) throws MoneyChangeException {

        BigDecimal amountInserted = getAmountFromCoins(insertedCoins);
        BigDecimal rest = amountInserted.subtract(amountToPay);
        return changeStrategy.countRestInCoinsQuantity(rest, coinsInCashier);
    }

    /**
     * Returns sum of inserted coins and currently existed coins in cashier pad
     */
    private Map<Denomination, Integer> sumInsertedCoinsWithCoinsInCashier() {
        return Stream.concat(insertedCoins.entrySet().stream(), coinsInCashier.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
    }


    /**
     * Returns subtract of inserted coins and currently existed coins in cashier pad
     *
     * @param restCoins      inserted coins
     * @param coinsInCashier coins in cashier
     */
    private Map<Denomination, Integer> subtractCoinsInCashierAndRestCoins(Map<Denomination, Integer> restCoins, Map<Denomination, Integer> coinsInCashier) {
        return Stream.concat(coinsInCashier.entrySet().stream(), restCoins.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (q1, q2) -> q1 - q2));
    }

    /**
     * Counts amount from given denomination and quantity map
     *
     * @param coinsQuantity coins quantity map
     * @return amount
     */
    public BigDecimal getAmountFromCoins(Map<Denomination, Integer> coinsQuantity) {
        return coinsQuantity.entrySet().stream()
            .map(moneyIntegerEntry -> moneyIntegerEntry.getKey().getDenomination().multiply(BigDecimal.valueOf(moneyIntegerEntry.getValue())))
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    /**
     * Counts whole amount of cash in cashier pad
     *
     * @return amount in cashier pad
     */
    public BigDecimal getMoneyInCashier() {
        return coinsInCashier.entrySet().stream()
            .map(entry -> {
                Integer quantity = entry.getValue();
                if (quantity == null) {
                    quantity = 0;
                }
                return entry.getKey().getDenomination().multiply(BigDecimal.valueOf(quantity));
            })
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    /**
     * Returns quantity of given denomination in cashier pad.
     *
     * @param denomination denomination
     * @return denomination quantity
     */
    public Integer getDenominationQuantity(Denomination denomination) {
        if (coinsInCashier.get(denomination) == null) {
            return 0;
        }
        return coinsInCashier.get(denomination);
    }

    public Map<Denomination, Integer> getCoinsInCashier() {
        return coinsInCashier;
    }

    public void setCoinsInCashier(Map<Denomination, Integer> coinsInCashier) {
        this.coinsInCashier = coinsInCashier;
    }
}
