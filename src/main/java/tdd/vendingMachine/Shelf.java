package tdd.vendingMachine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.exception.VendingException;

import java.util.AbstractMap;
import java.util.Map;

import static tdd.vendingMachine.Product.EMPTY;

/**
 * @author kdkz
 */
public class Shelf {

    final static Logger log = LoggerFactory.getLogger(Shelf.class);

    public static final int DEFAULT_SHELF_SIZE = 8;

    private static int shelfCount = 0;

    private int shelfSize;

    private int shelfNumber;

    private Map.Entry<Product, Integer> productsOnShelf = new AbstractMap.SimpleEntry<>(EMPTY, 0);

    public Shelf() {
        shelfCount++;
        shelfNumber = shelfCount;
        if (System.getProperty("shelf.size") != null) {
            shelfSize = Integer.parseInt(System.getProperty("shelf.size"));
        }else {
            shelfSize = DEFAULT_SHELF_SIZE;
        }
    }

    public int getShelfNumber() {
        return shelfNumber;
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
            log.error("Failed to put products {} on shelf {}", product.getName(), shelfNumber);
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

    public void putProductsOnShelf(String productName, int quantity) throws VendingException {
        if (Product.getProductByName(productName) == EMPTY) {
            log.error("Product with given name {} does not exist. Product is not set.", productName);
            throw new VendingException("Product with given name does not exist.");
        }
        putProductsOnShelf(Product.getProductByName(productName), quantity);
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
