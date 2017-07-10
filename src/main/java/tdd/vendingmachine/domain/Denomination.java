package tdd.vendingmachine.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@ToString
@EqualsAndHashCode
class Denomination {

    private final MoneyAmount value;

    private Denomination(MoneyAmount value) {
        this.value = Objects.requireNonNull(value);
    }

    static Denomination create(BigDecimal value) {
        MoneyAmount moneyAmount = new MoneyAmount(value);
        return new Denomination(moneyAmount);
    }

    MoneyAmount value() {
        return value;
    }
}
