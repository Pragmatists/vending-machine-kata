package tdd.vendingMachine.domain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class represents single shelf on vending machine. It contains products but only one type of product.
 *
 * @author kdkz
 */
public class Shelf {

    private final static Logger log = LoggerFactory.getLogger(Shelf.class);

    private Product product;

    public Shelf(Product product) {
        this.product = product;
    }

    /**
     * Takes off product from shelf.
     */
    void takeOffProduct() {
        log.debug("Product: {} spent", product.getName());
    }

    Product getProductOnShelf() {
        return product;
    }
}
