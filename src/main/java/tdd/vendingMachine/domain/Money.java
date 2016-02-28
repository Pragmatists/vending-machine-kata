package tdd.vendingMachine.domain;

import java.math.BigDecimal;

public class Money {

    private final BigDecimal value;

    private Money(String valueAsText) {
        this.value = new BigDecimal(valueAsText).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private Money(BigDecimal value) {
        this.value = value;
    }

    public static Money createMoney(String stringMoneyValue) {
        return new Money(stringMoneyValue);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public Money subtract(Money otherMoney) {
        return new Money(this.value.subtract(otherMoney.value));
    }

    public boolean isLessThan(Money money) {
        return this.value.compareTo(money.value) == -1;
    }
}
