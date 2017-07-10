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

    boolean isMetWith(Money money) {
        return priceAmountMinusPayment(money).isEqualOrBelowZero();
    }

    Money amountLeftToPayAfterPaying(Money money) {
        return isMetWith(money) ? Money.zero() : priceAmountMinusPayment(money);
    }

    Money amountOfChangeAfterPaying(Money money) {
        return isMetWith(money) ? paymentMinusPriceAmount(money) : Money.zero();
    }

    private Money priceAmountMinusPayment(Money money) {
        return amount.subtract(money);
    }

    private Money paymentMinusPriceAmount(Money money) {
        return money.subtract(amount);
    }
}
