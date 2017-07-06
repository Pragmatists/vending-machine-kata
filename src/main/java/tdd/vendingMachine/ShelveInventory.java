package tdd.vendingMachine;

import tdd.vendingMachine.model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return null;
    }

    @Override
    public void put(int index, Product product) {
    }

    @Override
    public void clean() {

    }

}
