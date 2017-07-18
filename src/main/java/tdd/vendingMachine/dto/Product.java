package tdd.vendingMachine.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
public class Product {
    private final String name;
    private final BigDecimal price;

    private Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, price.setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }
}
