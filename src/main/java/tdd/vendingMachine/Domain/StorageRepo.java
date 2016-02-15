package tdd.vendingMachine.Domain;

import tdd.vendingMachine.Transaction.MachineException;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for management of the physical storage of products.
 */
public class StorageRepo {
    Integer[] pidAtShelf;
    Integer[] countAtShelf;
    List<String> internalLog;   //registers physical actions of the storage
    final int nShelves;
    final int maxProducts;

    public StorageRepo(int nShelves, int maxProducts) {
        this.nShelves = nShelves;
        this.maxProducts = maxProducts;
        pidAtShelf = new Integer[nShelves];
        countAtShelf = new Integer[nShelves];
        for (int i = 0; i < nShelves; i++) {
            pidAtShelf[i] = null;
            countAtShelf[i] = 0;
        }
        internalLog = new ArrayList<>();
    }

    /**
     * Getters for state of the Storage; throw ArrayIndexOutOfBoundsException if
     * `shelf` is not an allowed shelf number.
     */

    public Integer getPidAtShelf(int shelf) {
        return pidAtShelf[shelf];
    }

    public Integer getCountAtShelf(int shelf) {
        return countAtShelf[shelf];
    }

    /**
     * @param shelf: number of shelf from which the product should be served
     * @throws MachineException if no products are present at this shelf
     *        (or other not yet specified condition prevents discharge of the product)
     */
    public void serveProduct(int shelf) throws MachineException {

    }

    /**
     * Adds products to selected shelf; implicitly these are the same type of products.
     *
     * @param shelf selected shelf
     * @param newProductCount (can be negative)
     * @throws RuntimeException when count at `shelf` becomes invalid
     */
    public void addProductsToShelf(int shelf, int newProductCount) throws RuntimeException {

    }

    /**
     * @param shelf selected shelf
     * @param productid (will not be checked for validity in ProductRepo)
     * @param count (must be valid; else RuntimeException thrown)
     * @throws ArrayIndexOutOfBoundsException for invalid shelf number, and
     *         RuntimeException if count of products is invalid.
     */
    public void setProductAtShelf(int shelf, int productid, int count) throws RuntimeException {
        if (!isCountValid(count)) throw new RuntimeException();
        pidAtShelf[shelf] = productid;
        countAtShelf[shelf] = count;
    }

    //helpers

    private boolean isCountValid(int count) {
        return count >= 0 && count <= maxProducts;
    }

}
