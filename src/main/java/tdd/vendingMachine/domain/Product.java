package tdd.vendingMachine.domain;

import java.math.BigDecimal;

/**
 * Vending machine products.
 *
 * @author kdkz
 */
public enum Product {

    COLA("Cola", BigDecimal.valueOf(7.4)),
    CHOCOLATE_BAR("Chocolate bar", BigDecimal.valueOf(1.2)),
    MINERAL_WATER("Mineral water", BigDecimal.valueOf(3.7));

    private String name;

    private BigDecimal price;

    Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
