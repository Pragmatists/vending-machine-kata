package tdd.vendingMachine;

import tdd.vendingMachine.model.Product;

import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public interface Bucket {

    void putInto(List<Double> coins, Product product);

}
