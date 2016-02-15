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

    public Integer getPidAtShelf(int shelf) {
        return null;
    }

    public Integer getCountAtShelf(int shelf) {
        return null;
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
     * @throws RuntimeException
     */
    public void setProductAtShelf(int shelf, int productid, int count) throws RuntimeException {

    }


    //---------------
    private boolean checkShelfNumber(int shelf) {
        return shelf >= 0 && shelf < nShelves;
    }

}
