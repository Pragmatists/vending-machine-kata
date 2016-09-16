package tdd.vendingMachine.product;

import lombok.Getter;

public class Product {
    @Getter
    private final ProductType type;

    public Product(ProductType type) {
        this.type = type;
    }

    public Double getPrice() {
        return type.getPrice();
    }

    @Override
    public String toString() {
        return "Product{" +
            "type=" + type +
            ", price=" + type.getPrice() +
            '}';
    }
}
