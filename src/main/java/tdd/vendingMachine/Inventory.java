package tdd.vendingMachine;

import tdd.vendingMachine.model.Product;

/**
 * @author Yevhen Sukhomud
 */
public interface Inventory {

    Product get(int index);

    Product getAndDelete(int index);

    void put(int index, Product product);

    void clean();

}
