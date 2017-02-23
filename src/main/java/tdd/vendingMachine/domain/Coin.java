package tdd.vendingMachine.domain;

import lombok.NonNull;

import java.util.*;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 * Enum representing the coins available/acceptable by the vending machine
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
     * Validation map containing labels of every available coin
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
     * @param order a number from 0 to Coin.values().length -1
     * @return the coin corresponding to the given order
     * @throws NoSuchElementException if no such order exists
     */
    public static Coin retrieveCoinByOrder(int order) throws NoSuchElementException {
        if (order < 0 && order >= coinLabelMap.size()) {
            throw new NoSuchElementException("Unable to retrieve Coin by value: " + order);
        }
        return coinOrderMap.get(order);
    }
}
