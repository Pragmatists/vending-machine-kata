package tdd.vendingmachine.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import tdd.vendingmachine.domain.dto.PriceDto;

import java.math.BigDecimal;
import java.util.Objects;

@ToString
@EqualsAndHashCode
class Price implements Money {

    private final MoneyAmount amount;

    private Price(MoneyAmount amount) {
        this.amount = Objects.requireNonNull(amount);
    }

    static Price create(PriceDto priceDto) {
        MoneyAmount moneyAmount = new MoneyAmount(priceDto.getValue());
        return new Price(moneyAmount);
    }

    @Override
    public BigDecimal value() {
        return amount.value();
    }

    @Override
    public Money add(Money money) {
        return amount.add(money);
    }

    @Override
    public Money subtract(Money money) {
        return amount.subtract(money);
    }
}
