package tdd.vendingMachine.domain;

/**
 * Created by Agustin on 2/19/2017.
 * A class representing a product to be sold on the vending machine,
 * has a price and a type.
 */
public class Product {

    private final double price;
    private final String type;

    public Product(double price, String type) {
        this.price = price;
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }
}
