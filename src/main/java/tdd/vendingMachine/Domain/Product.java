package tdd.vendingMachine.Domain;

import lombok.Data;

/**
 * Products that can be served by the Machine.
 * List of products will be avilable to clients.
 * Storage uses `pid` as foreign key.
 */


@Data
public class Product {
    private Integer pid = 0;
    private String name;
    private Integer price;

    public Product() {
    }

    public Product(String name, Integer price) {
        this.name = name;
        this.price = price;
    }
}
