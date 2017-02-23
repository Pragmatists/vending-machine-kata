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
    public String toString() {
        return type + ": " + price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (Double.compare(product.price, price) != 0) return false;
        return type.equals(product.type);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = type.hashCode();
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
