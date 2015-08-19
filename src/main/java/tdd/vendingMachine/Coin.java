package tdd.vendingMachine;

import java.math.BigDecimal;

/**
 * @author macko
 * @since 2015-08-19
 */
public enum Coin {
    FIVE("5"),
    TWO("2"),
    ONE("1"),
    HALF("0.5"),
    ONE_FIFTH("0.2"),
    ONE_TENTH("0.1");

    private BigDecimal value;

    Coin(String value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return this.value;
    }
}
