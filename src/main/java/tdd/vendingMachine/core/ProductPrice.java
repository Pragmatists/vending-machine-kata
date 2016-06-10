package tdd.vendingMachine.core;

import java.math.BigDecimal;

public class ProductPrice {

    private final BigDecimal value;

    private ProductPrice(BigDecimal value) {
        this.value = value;
    }

    public static ProductPrice valueOf(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalProductPriceException("Product price should be a valid value");
        }

        BigDecimal bigDecimal = new BigDecimal(value).setScale(1, BigDecimal.ROUND_FLOOR);

        if (bigDecimal.signum() != 1) {
            throw new IllegalProductPriceException("Product price should be greater than 0");
        }

        return new ProductPrice(bigDecimal);
    }
}
