package tdd.vendingmachine.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import tdd.vendingmachine.config.BigDecimalConfiguration;

import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
class MoneyAmount implements Money {

    private final BigDecimal value;

    MoneyAmount(BigDecimal value) {
        this.value = BigDecimalConfiguration.trimToDefaultScale(value);
    }

    static Money zero() {
        return new MoneyAmount(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal value() {
        return value;
    }

    public Money add(Money money) {
        return new MoneyAmount(value.add(money.value()));
    }

    @Override
    public Money subtract(Money money) {
        return new MoneyAmount(value.subtract(money.value()));
    }
}
