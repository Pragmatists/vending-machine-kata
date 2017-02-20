package tdd.vendingMachine.domain;

/**
 * Created by Agustin on 2/19/2017.
 * A class representing a product to be sold on the vending machine,
 * has a price and a type.
 */
public class Product implements ShelfItem{

    public final double price;
    public final String type;

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

    @Override
    public String provideType() {
        return getType();
    }

    @Override
    public double provideValue() {
        return getPrice();
    }
}
