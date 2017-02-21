package tdd.vendingMachine.domain;

import lombok.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 * Enum representing the coins available/acceptable by the vending machine
 */
public enum Coin implements ShelfItem {
    ONE("1.0", 1.0),
    TWO("2.0", 2.0),
    FIVE("5.0", 5.0),
    TEN_CENTS("0.1", 0.1),
    TWENTY_CENTS("0.2", 0.2),
    FIFTY_CENTS("0.5", 0.5);

    /**
     * Validation map containing labels of every available coin
     */
    private static Map<String, Coin> coinMap = Collections.unmodifiableMap(new HashMap<String, Coin>() {{
        put(ONE.label, ONE);
        put(TWO.label, TWO);
        put(FIVE.label, FIVE);
        put(TEN_CENTS.label, TEN_CENTS);
        put(TWENTY_CENTS.label, TWENTY_CENTS);
        put(FIFTY_CENTS.label, FIFTY_CENTS);
    }});

    /**
     * label since is final can be public
     */
    public final String label;

    /**
     * denomination of the coin since is final can be public
     */
    public final double denomination;

    Coin(String label, double denomination) {
        this.label = label;
        this.denomination = denomination;
    }


    @Override
    public String provideType() {
        return label;
    }

    @Override
    public double provideValue() {
        return denomination;
    }

    public static boolean validCoin(@NonNull String label) {
        return coinMap.containsKey(label);
    }

    public static Coin retrieveCoinByLabel(@NonNull String label) {
        if (validCoin(label)) {
            return coinMap.get(label);
        }
        throw new NoSuchElementException(String.format("The current label %s is not a valid Coin", label));
    }
}
