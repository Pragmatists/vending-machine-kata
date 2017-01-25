package tdd.vendingMachine;

import java.util.EnumMap;

/**
 * Created by dzalunin on 2017-01-25.
 */
public class Result {

    private Product product;
    private EnumMap<Denomination, Integer> change;

    public Result(Product product, EnumMap<Denomination, Integer> change) {
        this.product = product;
        this.change = change.clone();
    }

    public Product getProduct() {
        return product;
    }

    public EnumMap<Denomination, Integer> getChange() {
        return change;
    }
}
