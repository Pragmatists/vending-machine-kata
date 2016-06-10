package tdd.vendingMachine.core;

public class ProductPrice {

    private final CurrencyUnit currencyUnit;

    private ProductPrice(CurrencyUnit value) {
        this.currencyUnit = value;
    }

    public static ProductPrice valueOf(String value) {
        CurrencyUnit currencyUnit = CurrencyUnit.valueOf(value);

        if (currencyUnit.isNegative() || currencyUnit.isZero()) {
            throw new IllegalCurrencyValueException("Value should be greater than 0");
        }

        return new ProductPrice(currencyUnit);
    }

    public String value() {
        return currencyUnit.value();
    }
}
