package tdd.vendingMachine.product;

import java.math.BigDecimal;

/**
 * Created by okraskat on 06.02.16.
 */
public interface Product {
    BigDecimal getPrice();
    String getName();
}
