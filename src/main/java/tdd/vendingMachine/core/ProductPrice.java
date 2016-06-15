package tdd.vendingMachine.core;

public class ProductPrice {

    private final CurrencyUnit currencyUnit;

    private ProductPrice(CurrencyUnit value) {
        this.currencyUnit = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ProductPrice that = (ProductPrice) obj;
        return currencyUnit != null ? currencyUnit.equals(that.currencyUnit) : that.currencyUnit == null;

    }

    @Override
    public int hashCode() {
        return currencyUnit != null ? currencyUnit.hashCode() : 0;
    }

    public static ProductPrice valueOf(String value) {
        CurrencyUnit currencyUnit = CurrencyUnit.valueOf(value);

        if (currencyUnit.isNegative() || currencyUnit.isZero()) {
            throw new IllegalArgumentException("Value should be greater than 0");
        }

        return new ProductPrice(currencyUnit);
    }

    public String value() {
        return currencyUnit.value();
    }

    public CurrencyUnit toCurrency() {
        return currencyUnit;
    }
}
