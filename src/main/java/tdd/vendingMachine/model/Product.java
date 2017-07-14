package tdd.vendingMachine.model;

/**
 * @author Yevhen Sukhomud
 */
public class Product {

    private final String name;
    private final Integer price;

    public Product(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Integer price() {
        return price;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return name.equals(product.name) && price.equals(product.price);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }

}
