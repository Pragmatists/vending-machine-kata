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

    @Override
    public BigDecimal value() {
        return value;
    }
}
