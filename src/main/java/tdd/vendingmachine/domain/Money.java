package tdd.vendingmachine.domain;

import java.math.BigDecimal;

interface Money extends Comparable<Money> {

    BigDecimal value();

    static Money zero() {
        return new MoneyAmount(BigDecimal.ZERO);
    }

    @Override
    default int compareTo(Money money) {
        return value().compareTo(money.value());
    }

    default boolean isNotMoreThan(Money money) {
        return value().compareTo(money.value()) <= 0;
    }

    default boolean isZero() {
        return value().compareTo(BigDecimal.ZERO) == 0;
    }

    default boolean isEqualOrBelowZero() {
        return value().compareTo(BigDecimal.ZERO) <= 0;
    }

    default Money add(Money money) {
        return new MoneyAmount(value().add(money.value()));
    }

    default Money subtract(Money money) {
        return new MoneyAmount(value().subtract(money.value()));
    }
}
