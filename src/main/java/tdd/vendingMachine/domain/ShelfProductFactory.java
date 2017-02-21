package tdd.vendingMachine.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.InputMismatchException;

/**
 * @author Agustin on 2/19/2017.
 * @since 1.0
 * This is an utility factory class to build shelves of products
 */
public class ShelfProductFactory {

    private static void validateShelfInput(Product product, int capacity, int itemCount) {
        if (product == null || StringUtils.isEmpty(product.getType())) {
            throw new InputMismatchException("Invalid product must not be empty");
        }
        if (capacity <= 0) {
            throw new InputMismatchException("Invalid capacity must be greater than zero");
        }
        if (itemCount < 0 || itemCount > capacity) {
            throw new InputMismatchException("Invalid item count must equal or greater than zero and less or equal than capacity");
        }
    }

    /**
     * Build a shelf of given type and given capacity
     * @param id the shelf id
     * @param product The product type to be contained on the shelf
     * @param capacity the capacity of the shelf
     * @return a Shelf object
     */
    public static Shelf<Product> buildShelf(int id, Product product, int capacity) {
        return ShelfProductFactory.build(id, product, capacity, 0);
    }

    /**
     * Build a shelf of given type and given capacity and initial item count
     * @param id the shelf id
     * @param product The product type to be listed on the shelf
     * @param capacity the capacity of the shelf
     * @param itemCount the amount of items loaded on the shelf
     * @return a Shelf object
     */
    public static Shelf<Product> buildShelf(int id, Product product, int capacity, int itemCount) {
        return ShelfProductFactory.build(id, product, capacity, itemCount);
    }

    /**
     * This method is the only one instantiating objects
     * @param product The product be listed on the shelf
     * @param capacity the capacity of the shelf
     * @param itemCount the amount of items loaded on the shelf
     * @return a Shelf object
     */
    private static Shelf<Product> build(int id, Product product, int capacity, int itemCount) {
        validateShelfInput(product, capacity, itemCount);
        return new Shelf<>(id, product, capacity, itemCount);
    }
}
