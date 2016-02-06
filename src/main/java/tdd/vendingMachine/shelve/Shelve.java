package tdd.vendingMachine.shelve;

import tdd.vendingMachine.product.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by okraskat on 06.02.16.
 */
public interface Shelve<T extends Product> {
    List<T> getProducts();
    BigDecimal getProductPrice();
}
