package tdd.vendingMachine.util.TestUtils;

import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.dto.CashImport;
import tdd.vendingMachine.dto.ProductImport;

import java.util.*;

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

    public static Map<Coin, Shelf<Coin>> buildStubCoinDispenserWithGivenItemsPerShelf(int shelfCapacity, int initialCoinsOnShelf) {
        Map<Coin, Shelf<Coin>> coinDispenser = new HashMap<>();
        int idCounter = 0;
        for(Coin coin: Coin.ascendingDenominationIterable()) {
            Shelf<Coin> shelf = ShelfFactory.buildShelf(idCounter++, coin, shelfCapacity, initialCoinsOnShelf);
            coinDispenser.put(coin, shelf);
        }
        return coinDispenser;
    }

    /**
     * Builds a product shelves with one product given the amount of each product
     * @param product the item to include
     * @param amount the amount of items of that product
     * @return the  shelves for products on the vending machine
     */
    public static Map<Integer, Shelf<Product>> buildShelvesWithItems(Product product, int amount) {
        Shelf<Product> shelf = ShelfFactory.buildShelf(0, product, 10, amount);
        return new HashMap<Integer, Shelf<Product>>(){{put(shelf.id, shelf);}};
    }

    /**
     * Builds a product shelves with list of products given the amount of each product
     * @param products collection of products to include
     * @param amount the amount of items of that product
     * @param shelfCapacity
     * @return the  shelves for products on the vending machine
     */
    public static Map<Integer, Shelf<Product>> buildShelvesWithItems(Collection<Product> products, int amount, int shelfCapacity) {
        HashMap<Integer, Shelf<Product>> shelves = new HashMap<>();
        Shelf<Product> shelf;
        int counter = 0;
        for(Product product: products) {
            shelf = ShelfFactory.buildShelf(counter++, product, shelfCapacity, amount);
            shelves.put(shelf.id, shelf);
        }
        return shelves;
    }

    /**
     * Provides a list of products of given size
     * @param amountProducts the size of the returned list
     * @return a list of products
     */
    public static Collection<Product> buildStubListOfProducts(int amountProducts) {
        Collection<Product> list = new ArrayList<>();
        for (int i = 0; i < amountProducts; i++) {
            list.add(new Product((i + 1)*10, "p"+i));
        }
        return list;
    }

    public static Map<Integer, Shelf<Product>> buildShelfStubFromProductImports(Collection<ProductImport> productImports, int capacity) {
        Map<Integer, Shelf<Product>> productShelves = new HashMap<>();
        int idCounter = 0;
        for (ProductImport productImport: productImports) {
            Shelf<Product> productShelf = ShelfFactory.buildShelf(idCounter++, new Product(productImport.getPrice(), productImport.getType()), capacity, productImport.getItemCount());
            productShelves.put(productShelf.id, productShelf);
        }
        return productShelves;
    }

    public static Map<Integer, Shelf<Product>> buildShelvesWithItem(Product product, int itemCount, int coinShelfCapacity) {
        return buildShelvesWithItems(Collections.singletonList(product), itemCount, coinShelfCapacity);
    }
}
