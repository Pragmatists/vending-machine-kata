package tdd.vendingMachine.domain.parts.money;

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

    public Money multiply(int times) {
        return new Money(this.value.multiply(new BigDecimal(times)));
    }

    public boolean isGreaterOrEqualTo(Money otherMoney) {
        return this.value.compareTo(otherMoney.value) != -1;
    }

    public Money add(Money otherMoney) {
        return new Money(this.value.add(otherMoney.value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        return value != null ? value.equals(money.value) : money.value == null;

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
