package tdd.vendingMachine.domain;

/**
 * Created by Agustin on 2/19/2017.
 * A class representing a product to be sold on the vending machine,
 * has a price and a type.
 */
public class Product implements ShelfItem{

    private final String type;
    private final int price;

    public Product(int price, String type) {
        this.price = price;
        this.type = type;
    }

    public int getPrice() {
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
    public int provideValue() {
        return getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (getPrice() != product.getPrice()) return false;
        return getType().equals(product.getType());

    }
}
