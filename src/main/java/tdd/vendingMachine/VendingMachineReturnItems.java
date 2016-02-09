package tdd.vendingMachine;

import tdd.vendingMachine.product.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by okraskat on 06.02.16.
 */
public class VendingMachineReturnItems {

    private final List<BigDecimal> change;

    private final Product product;

    public VendingMachineReturnItems(List<BigDecimal> change, Product product) {
        this.change = change;
        this.product = product;
    }

    public List<BigDecimal> getChange() {
        return change;
    }

    public Product getProduct() {
        return product;
    }
}
