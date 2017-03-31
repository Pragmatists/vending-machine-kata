package tdd.vendingMachine.domain;

import java.math.BigDecimal;

/**
 * Vending machine products
 *
 * @author kdkz
 */
public enum Product {

    EMPTY(BigDecimal.valueOf(0)),

    COLA(BigDecimal.valueOf(7.4)),
    CHOCOLATE_BAR(BigDecimal.valueOf(1.2)),
    MINERAL_WATER(BigDecimal.valueOf(3.7));

    private BigDecimal price;

    Product(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
