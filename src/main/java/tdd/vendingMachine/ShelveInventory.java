package tdd.vendingMachine;

import tdd.vendingMachine.exception.ShelveProductsShouldBeSimilarException;
import tdd.vendingMachine.model.Product;

import java.util.*;

/**
 * @author Yevhen Sukhomud
 */
public class ShelveInventory implements Inventory {

    private Map<Integer, List<Product>> shelve = new HashMap<>();

    public ShelveInventory() {
    }

    public ShelveInventory(Map<Integer, List<Product>> shelve) {
        this.shelve.putAll(shelve);
    }

    @Override
    public Product get(int index) {
        List<Product> products = shelve.get(index);
        if (products == null || products.isEmpty()) {
            return null;
        }
        return products.stream().findFirst().orElse(null);
    }

    @Override
    public Product getAndDelete(int index) {
        List<Product> products = shelve.get(index);
        if (products == null || products.isEmpty()) {
            return null;
        }
        Iterator<Product> iterator = products.iterator();
        Product product = iterator.next();
        iterator.remove();
        return product;
    }

    @Override
    public void put(int index, Product product) {
        List<Product> products = shelve.getOrDefault(index, new ArrayList<>());
        if (notTheSameTypeOnShelve(product, products)) {
            throw new ShelveProductsShouldBeSimilarException();
        }
        products.add(product);
        shelve.put(index, products);
    }

    private boolean notTheSameTypeOnShelve(Product product, List<Product> products) {
        return !products.isEmpty() && !products.contains(product);
    }

    @Override
    public void clean() {
        shelve.clear();
    }

}
