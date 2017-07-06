package tdd.vendingMachine;

import tdd.vendingMachine.model.Product;

import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public interface Memory {

    int productIndex();

    List<Double> insertedMoney();

    double price();

    void clear();

    void remember(Product product);

    void remember(double product);

    boolean hasInsertedMoney();

    boolean hasSelectedProduct();

}
