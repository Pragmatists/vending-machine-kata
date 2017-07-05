package tdd.vendingMachine;

import tdd.vendingMachine.model.Product;

/**
 * @author Yevhen Sukhomud
 */
public interface Inventory {

    Product get(int index);

    void put(Product product);

    void clean();
}
