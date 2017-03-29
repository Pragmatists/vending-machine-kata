package tdd.vendingMachine;

import java.math.BigDecimal;

/**
 * Denomination enum
 *
 * @author kdkz
 */
enum Denomination {

    FIVE(5),
    TWO(2),
    ONE(1),
    HALF(0.5),
    ONE_FIFTH(0.2),
    ONE_TENTH(0.1);

    private BigDecimal denomination;

    Denomination(double denomination) {
        this.denomination = BigDecimal.valueOf(denomination);
    }

    public BigDecimal getDenomination() {
        return denomination;
    }
}
