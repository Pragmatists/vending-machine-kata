package tdd.vendingMachine;

import java.util.List;

/**
 * Created by dzalunin on 2017-01-25.
 */
public class Result {

    private Product product;
    private List<Denomination> change;

    public Result(Product product, List<Denomination> change) {
        this.product = product;
        this.change = change;
    }

    public Product getProduct() {
        return product;
    }

    public List<Denomination> getChange() {
        return change;
    }
}
