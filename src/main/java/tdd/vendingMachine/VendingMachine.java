package tdd.vendingMachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VendingMachine {
    private Map<Integer, Product> shelves;

    public VendingMachine() {
        shelves = new HashMap<Integer, Product>();
    }

    public void addProductToShelf(int shelfNo, Product product) {
        shelves.put(shelfNo, product);
    }

    public Product getProductFromShelf(int shelfNo) {
        return shelves.remove(shelfNo);
    }
}
