package tdd.vendingMachine.domain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.exception.VendingException;

import java.util.AbstractMap;
import java.util.Map;

import static tdd.vendingMachine.domain.Product.EMPTY;

/**
 * @author kdkz
 */
public class Shelf {

    final static Logger log = LoggerFactory.getLogger(Shelf.class);

    public static final int DEFAULT_SHELF_SIZE = 8;

    private int shelfSize;

    private Map.Entry<Product, Integer> productsOnShelf = new AbstractMap.SimpleEntry<>(EMPTY, 0);

    public Shelf() {
        shelfSize = DEFAULT_SHELF_SIZE;
    }

    public Shelf(Product product, Integer quantity) {
        super();
        this.productsOnShelf = new AbstractMap.SimpleEntry<>(product, quantity);
    }

    public int getShelfSize() {
        return shelfSize;
    }

    public Map.Entry<Product, Integer> getProductsOnShelf() {
        return productsOnShelf;
    }

    public int getCurrentProductCount() {
        return productsOnShelf.getValue();
    }

    public void putProductsOnShelf(Product product, int quantity) throws VendingException {
        if (quantity < 1) {
            log.error("Failed to put products {} on shelf", product.getName());
            throw new VendingException("Failed to put products on shelf.");
        } else if (quantity > shelfSize) {
            log.warn("Failed to put {} products on shelf. The shelf size is {}. Quantity set to {}",
                quantity, shelfSize, shelfSize);
            productsOnShelf = new AbstractMap.SimpleEntry<>(product, shelfSize);
            return;
        }
        productsOnShelf = new AbstractMap.SimpleEntry<>(product, quantity);
        log.info("Product {} placed on the shelf. Product quantity is {}", product.getName(), quantity);
    }

    /**
     * Takes off product from shelf. If There is already no product on shelf VendingException will throw
     *
     * @return product count left
     */
    public int takeOffProduct() throws VendingException {
        if (productsOnShelf.getValue() <= 0) {
            throw new VendingException("Warn!!!!!!!!!!!!!!");
        }
        int currentCount = productsOnShelf.getValue() - 1;
        productsOnShelf.setValue(currentCount);
        return currentCount;
    }
}
