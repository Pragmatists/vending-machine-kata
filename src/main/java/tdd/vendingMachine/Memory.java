package tdd.vendingMachine;

import tdd.vendingMachine.model.Product;

import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public interface Memory {

    int productIndex();

    List<Integer> insertedMoney();

    Integer price();

    void clear();

    void remember(Product product, int index);

    void remember(Integer money);

    boolean hasInsertedMoney();

    boolean hasSelectedProduct();

}
