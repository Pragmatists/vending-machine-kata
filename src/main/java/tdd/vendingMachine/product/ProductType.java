package tdd.vendingMachine.product;

import lombok.Getter;

public enum ProductType {
    COLA(2.0),
    CHOCOLATE_BAR(3.0),
    WATER(1.2),
    CHIPS(2.5),
    NUTS(3.5);

    @Getter
    private double price;

    ProductType(double price) {
        this.price = price;
    }
}
