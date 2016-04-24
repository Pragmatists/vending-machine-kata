package tdd.vendingMachine.Domain;


import org.slf4j.Logger;


/**
 * Repository responsible for management of the physical storage of products.
 */
public class StorageRepo {
    private Integer[] pidAtShelf;
    private Integer[] countAtShelf;
    //Registers physical actions of the storageRepo
    private static Logger log = org.slf4j.LoggerFactory.getLogger(StorageRepo.class);

    private final int nShelves;
    private final int maxItemsOnShelf;

    public StorageRepo(int nShelves, int maxItemsOnShelf) {
        this.nShelves = nShelves;
        this.maxItemsOnShelf = maxItemsOnShelf;
        pidAtShelf = new Integer[nShelves];
        countAtShelf = new Integer[nShelves];
        for (int i = 0; i < nShelves; i++) {
            pidAtShelf[i] = 0;
            countAtShelf[i] = 0;
        }
    }

    /**
     * Getters for state of the Storage; throw ArrayIndexOutOfBoundsException if
     * `shelf` is not an allowed shelf number.
     */

    //Returns 0 if shelf contains no product
    public Integer getPidAtShelf(int shelf) {
        if (!isShelfNumberValid(shelf))
            throw new RuntimeException(Error.INVALID_SHELF_NUMBER.toString());
        return pidAtShelf[shelf];
    }

    public Integer getCountAtShelf(int shelf) {
        if (!isShelfNumberValid(shelf))
            throw new RuntimeException(Error.INVALID_SHELF_NUMBER.toString());
        return countAtShelf[shelf];
    }

    public int getnShelves() {
        return nShelves;
    }

    public int getMaxItemsOnShelf() {
        return maxItemsOnShelf;
    }

    /**
     * @param shelf: number of shelf from which the product should be served
     * @throws RuntimeException if no products are present at this shelf
     *        (or other not yet specified condition prevents discharge of the product)
     */
    public void serveProduct(int shelf) throws RuntimeException {
        if (countAtShelf[shelf]==0)
            throw new RuntimeException(Error.ERROR_SERVING_PRODUCT__EMPTY_SHELF.toString());
        countAtShelf[shelf]--;
        log.info("Served product from shelf " + shelf + " of pid=" + pidAtShelf[shelf]);
    }

    /**
     * Adds products to selected shelf; implicitly these are the same type of products.
     *
     * @param shelf selected shelf
     * @param addedCount (can be negative)
     * @throws RuntimeException when count at `shelf` becomes invalid
     */
    public void addProductsToShelf(int shelf, int addedCount) throws RuntimeException {
        if (!isShelfNumberValid(shelf)) throw new RuntimeException(Error.INVALID_SHELF_NUMBER.toString());
        int countNow = countAtShelf[shelf];
        if (countNow==0) throw new RuntimeException(Error.CANNOT_ADD_TO_EMPTY_SHELF.toString());
        if (!isCountValid(countNow + addedCount))
            throw new RuntimeException(Error.INVALID_NUMBER_OF_ITEMS_AT_SHELF.toString());
        countAtShelf[shelf] += addedCount;
        log.info("Added " + addedCount + " items (pid=" + pidAtShelf[shelf] + ") to shelf " + shelf);
    }

    /**
     * @param shelf selected shelf
     * @param productid (will not be checked for validity in ProductRepo)
     * @param count (must be valid; else RuntimeException thrown)
     * @throws RuntimeException for invalid shelf number, and
     *         RuntimeException if count of products is invalid.
     */
    public void setProductAtShelf(int shelf, int productid, int count) throws RuntimeException {
        if (!isCountValid(count)) throw new RuntimeException(Error.INVALID_NUMBER_OF_ITEMS_AT_SHELF.toString());
        if (!isShelfNumberValid(shelf)) throw new RuntimeException(Error.INVALID_SHELF_NUMBER.toString());
        pidAtShelf[shelf] = productid;
        countAtShelf[shelf] = count;
        log.info("Set pid=" + productid + " count=" + count + " at shelf=" + shelf);
    }

    //helpers

    private boolean isCountValid(int count) {
        return count >= 0 && count <= maxItemsOnShelf;
    }

    private boolean isShelfNumberValid(int shelf) {
        return shelf >= 0 && shelf < nShelves;
    }

}
