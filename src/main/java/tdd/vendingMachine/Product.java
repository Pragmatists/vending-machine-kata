package tdd.vendingMachine;

/**
 * Created by dzalunin on 2017-01-25.
 */
public enum Product {

    COCA_COLA("Coca Cola 0.5L", 300),
    KROPLA_BESKIDU("Kropla Beskidu 0.5L", 200);

    private String name;
    private long price;

    Product(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
