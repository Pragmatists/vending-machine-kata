package tdd.vendingMachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VendingMachine {
    private Map<Integer, Shelf> shelves;

    public VendingMachine() {
        shelves = new HashMap<Integer, Shelf>();
    }

    public void addProductToShelf(int shelfNo, Product product) {
        getShelf(shelfNo).addProduct(product);
    }

    public Product getProductFromShelf(int shelfNo) {
        return getShelf(shelfNo).getProduct();
    }

    protected Shelf getShelf(int shelfNumber) {
        if (!shelves.containsKey(shelfNumber)) {
            shelves.put(shelfNumber, new Shelf());
        }
        return shelves.get(shelfNumber);
    }

}
