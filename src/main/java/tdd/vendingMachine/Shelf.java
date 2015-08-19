package tdd.vendingMachine;

import java.util.LinkedList;

/**
 * @author macko
 * @since 2015-08-19
 */
public class Shelf {

    private LinkedList<Product> products;

    public Shelf() {
        this.products = new LinkedList<Product>();
    }

    public void addProduct(Product product) {
        if (!products.isEmpty() && !products.peek().hasSameTypeAs(product)) {
            throw new ManyProductsOnOneShelfException();
        }
        products.add(product);
    }

    public Product getProduct() {
        return products.poll();
    }
}
