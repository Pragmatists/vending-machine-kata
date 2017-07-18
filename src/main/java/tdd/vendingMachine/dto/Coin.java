package tdd.vendingMachine.dto;

import java.math.BigDecimal;

/**
 * By using an enum we have a type safe denominations. This is a trade off between configurability and readability safety.
 * It is assumed that since denominations rarely change, there will be little need to change this element.
 */
public enum Coin {
    FIVE(new BigDecimal("5")),
    TWO(new BigDecimal("2")),
    ONE(new BigDecimal("1")),
    DOT_FIVE(new BigDecimal("0.5")),
    DOT_TWO(new BigDecimal("0.2")),
    DOT_ONE(new BigDecimal("0.1")),;

    private final BigDecimal value;

    Coin(BigDecimal value) {

        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
