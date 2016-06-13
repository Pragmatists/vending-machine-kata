package tdd.vendingMachine.core;

import java.math.BigDecimal;

public class CurrencyUnit {

    private final BigDecimal amount;

    private CurrencyUnit(BigDecimal amount) {
        this.amount = amount;
    }

    public static CurrencyUnit valueOf(String value) {
        if (value == null || value.isEmpty() || !value.matches("-?\\d+(\\.\\d+)?")) {
            throw new IllegalCurrencyValueException("Value should not be null, empty or non number");
        }

        return new CurrencyUnit(new BigDecimal(value).setScale(1, BigDecimal.ROUND_FLOOR));
    }

    public String value() {
        return amount.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CurrencyUnit that = (CurrencyUnit) obj;
        return amount != null ? amount.equals(that.amount) : that.amount == null;
    }

    @Override
    public int hashCode() {
        return amount != null ? amount.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value();
    }

    public boolean isNegative() {
        return amount.signum() == -1;
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }
}
