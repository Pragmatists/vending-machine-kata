package tdd.vendingMachine;

import java.util.List;

/**
 * Created by okraskat on 06.02.16.
 */
public class Shelve<T extends Product> {

    private final List<T> products;

    public Shelve(List<T> products) {
        this.products = products;
    }

    public List<T> getProducts() {
        return products;
    }
}
