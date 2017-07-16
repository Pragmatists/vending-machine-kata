package tdd.vendingMachine;

import tdd.vendingMachine.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public class KingstonMemoryCard implements Memory {

    private Product product;
    private int index = 0;
    private List<Integer> money = new ArrayList<>();

    @Override
    public int productIndex() {
        return index;
    }

    @Override
    public List<Integer> insertedMoney() {
        return new ArrayList<>(money);
    }

    @Override
    public Integer price() {
        if (product == null) {
            return 0;
        }
        return product.price();
    }

    @Override
    public void clear() {
        product = null;
        index = 0;
        money.clear();
    }

    @Override
    public void remember(Product product, int index) {
        this.product = product;
        this.index = index;
    }

    @Override
    public void remember(Integer money) {
        this.money.add(money);
    }

    @Override
    public boolean hasInsertedMoney() {
        return money.size() > 0;
    }

    @Override
    public boolean hasSelectedProduct() {
        return product != null;
    }

}
