package tdd.vendingMachine.domain;

import java.math.BigDecimal;

public class Money implements Comparable<Money> {

    public static final Money ZERO = Money.of("0");

    private final BigDecimal denomination;

    public static Money of(String aDenomination) {
        return new Money(new BigDecimal(aDenomination));
    }

    private Money(BigDecimal aDenomination) {
        this.denomination = aDenomination;
    }

    public Money plus(Money aMoney) {
        return Money.of(this.denomination.add(aMoney.denomination).toString());
    }

    public Money minus(Money aMoney) {
        return Money.of(this.denomination.subtract(aMoney.denomination).toString());
    }

    @Override
    public String toString() {
        return denomination.toString();
    }

    @Override
    public int compareTo(Money o) {
        return denomination.compareTo(o.denomination);
    }
}
