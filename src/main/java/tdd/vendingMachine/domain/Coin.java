package tdd.vendingMachine.domain;

import lombok.NonNull;

import java.util.*;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 * Enum representing the coins available/acceptable by the vending machine
 * Adding more enum coins should also include the coin on the following maps for proper
 * enum behaviour:
 *   1. coinLabelMap a map from labels to coins
 *   2. coinOrderMap a map from denomination order (from low to high) to coins
 */
public enum Coin implements ShelfItem {
    FIVE("5.0$", 500, 5),
    TWO("2.0$", 200, 4),
    ONE("1.0$", 100, 3),
    FIFTY_CENTS("0.5$", 50, 2),
    TWENTY_CENTS("0.2$", 20, 1),
    TEN_CENTS("0.1$", 10, 0);

    /**
     * Validation map containing labels of every available coin
     */
    private static Map<String, Coin> coinLabelMap = Collections.unmodifiableMap(new HashMap<String, Coin>() {{
        put(TEN_CENTS.label, TEN_CENTS);
        put(TWENTY_CENTS.label, TWENTY_CENTS);
        put(FIFTY_CENTS.label, FIFTY_CENTS);
        put(ONE.label, ONE);
        put(TWO.label, TWO);
        put(FIVE.label, FIVE);
    }});

    /**
     * Validation map containing order given by denomination, this object provides the proper functionality of the
     * class CoinOrderIterator which uses the keys on this map to retrieve the coins.
     */
    private static Map<Integer, Coin> coinOrderMap = Collections.unmodifiableMap(new HashMap<Integer, Coin>() {{
        put(0, TEN_CENTS);
        put(1, TWENTY_CENTS);
        put(2, FIFTY_CENTS);
        put(3, ONE);
        put(4, TWO);
        put(5, FIVE);
    }});

    /**
     * label since is final can be public
     */
    public final String label;

    /**
     * denomination of the coin since is final can be public
     */
    public final int denomination;

    /**
     * The order according to other values starting from 1 to lowest value to highest value
     */
    private final int order;

    Coin(String label, int denomination, int order) {
        this.label = label;
        this.denomination = denomination;
        this.order = order;
    }

    @Override
    public String provideType() {
        return label;
    }

    @Override
    public int provideValue() {
        return denomination;
    }

    public static boolean validCoin(@NonNull String label) {
        return coinLabelMap.containsKey(label);
    }

    public static Coin retrieveCoinByLabel(@NonNull String label) {
        if (validCoin(label)) {
            return coinLabelMap.get(label);
        }
        throw new NoSuchElementException(String.format("The current label %s is not a valid Coin", label));
    }

    /**
     * Given an order should return a coin corresponding to that order
     * @param order a number from 0 to coinLabelMap.size() -1
     * @return the coin corresponding to the given order
     * @throws NoSuchElementException if no such order exists
     */
    private static Coin retrieveCoinByOrder(int order) throws NoSuchElementException {
        if (order < 0 && order >= coinLabelMap.size()) {
            throw new NoSuchElementException("Unable to retrieve Coin by value: " + order);
        }
        return coinOrderMap.get(order);
    }

    /**
     * Static method that allows to obtain a new instance of the CoinOrderIterator
     * that provides Coins from highest to lowest denomination.
     * @return an Iterator from highest to lowest coin denomination
     */
    public static Iterator<Coin> retrieveOrderDescendingIterator() {
        return new CoinOrderIterator(true);
    }

    /**
     * Static method that allows to obtain a new instance of the CoinOrderIterator
     * that provides Coins from lowest to highest denomination.
     * @return an Iterator from highest to lowest coin denomination if given highToLow is true
     */
    public static Iterator<Coin> retrieveOrderAscendingIterator() {
        return new CoinOrderIterator(false);
    }

    /**
     * This Class represents an iterator for the Coin enumeration, can iterate from high to low denomination or
     * from low to high denomination of coins, based on the constructor boolean parameter provided.
     * This class is highly linked to the map coinOrderMap and changes to that map will reflect the iteration.
     */
    static class CoinOrderIterator implements Iterator<Coin> {

        private final boolean highToLow;
        private int currentOrder;

        CoinOrderIterator(boolean highToLow) {
            this.highToLow = highToLow;
            this.currentOrder = highToLow ? coinOrderMap.size() - 1 :  0;
        }

        @Override
        public boolean hasNext() {
            return highToLow ? currentOrder >= 0 : currentOrder < coinOrderMap.size();
        }

        @Override
        public Coin next() {
            return Coin.retrieveCoinByOrder(highToLow ? currentOrder--: currentOrder++);
        }
    }




}
