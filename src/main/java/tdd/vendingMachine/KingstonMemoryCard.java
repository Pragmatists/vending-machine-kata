package tdd.vendingMachine;

import tdd.vendingMachine.model.Product;

import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public class KingstonMemoryCard implements Memory {

    @Override
    public int productIndex() {
        return 0;
    }

    @Override
    public List<Double> insertedMoney() {
        return null;
    }

    @Override
    public double price() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public void remember(Product product) {

    }

    @Override
    public void remember(double product) {

    }

    @Override
    public boolean hasInsertedMoney() {
        return false;
    }

    @Override
    public boolean hasSelectedProduct() {
        return false;
    }

}
