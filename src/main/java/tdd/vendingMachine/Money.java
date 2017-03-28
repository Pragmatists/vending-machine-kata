package tdd.vendingMachine;

import java.math.BigDecimal;

/**
 * @author kdkz
 */
public enum  Money {

    FIVE(BigDecimal.valueOf(5)),
    TWO(BigDecimal.valueOf(2)),
    ONE(BigDecimal.valueOf(1)),
    HALF(BigDecimal.valueOf(0.5)),
    ONE_FIFTH(BigDecimal.valueOf(0.2)),
    ONE_THENTH(BigDecimal.valueOf(0.1));

    private BigDecimal value;

    Money(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
