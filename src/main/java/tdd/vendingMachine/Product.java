package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Vending machine products
 *
 * @author kdkz
 */
public enum Product {

    EMPTY("EMPTY", BigDecimal.valueOf(0)),

    COLA("Cola", BigDecimal.valueOf(1.5)),
    CHOCOLATE_BAR("Chocolate bar", BigDecimal.valueOf(1)),
    MINERAL_WATER("Mineral water", BigDecimal.valueOf(1.25));

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

    public static Product getProductByName(String name) {
        return Arrays.stream(Product.values())
            .filter(product -> product.getName().equals(name))
            .findFirst()
            .orElse(EMPTY);
    }
}
