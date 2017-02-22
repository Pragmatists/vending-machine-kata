package tdd.vendingMachine.util.TestUtils;

import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.dto.CashImport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 *
 * Helper class used to build stubs of objects for testing purposes either plain or
 * reading from test resources file
 */
public class TestUtils {

    /**
     * Builds a full list of cash imports for the given capacity for every coin available
     * @param amount the amount of items per import created
     * @return a list of imports for every coin with the given amount
     */
    public static Collection<CashImport> getStubCashImportsFull(final int amount) {
        Collection<CashImport> list = new ArrayList<>();
        for (Coin c: Coin.values()) {
            list.add(new CashImport(c.label, amount));
        }
        return list;
    }

    public static Map<Coin, Shelf<Coin>> buildCoinDispenserWithGivenItemsPerShelf(int capacity, int initialCoinsOnShelf) {
        return new CoinDispenserBuilder(capacity).withCashImport(getStubCashImportsFull(initialCoinsOnShelf)).buildShelf();
    }

    /**
     * Builds a product shelves with one product given the amount of each product
     * @param product the item to include
     * @param amount the amount of items of that product
     * @return the  shelves for products on the vending machine
     */
    public static Map<Integer, Shelf<Product>> buildShelvesWithItems(Product product, int amount) {
        Shelf<Product> shelf = ShelfProductFactory.buildShelf(0, product, 10, amount);
        return new HashMap<Integer, Shelf<Product>>(){{put(shelf.id, shelf);}};
    }

    /**
     * Builds a product shelves with list of products given the amount of each product
     * @param products collection of products to include
     * @param amount the amount of items of that product
     * @return the  shelves for products on the vending machine
     */
    public static Map<Integer, Shelf<Product>> buildShelvesWithItems(Collection<Product> products, int amount) {
        HashMap<Integer, Shelf<Product>> shelves = new HashMap<>();
        Shelf<Product> shelf;
        int counter = 0;
        for(Product product: products) {
            shelf = ShelfProductFactory.buildShelf(counter++, product, 10, amount);
            shelves.put(shelf.id, shelf);
        }
        return shelves;
    }
}
