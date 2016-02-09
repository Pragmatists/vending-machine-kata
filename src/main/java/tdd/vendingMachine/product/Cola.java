package tdd.vendingMachine.product;

import java.math.BigDecimal;

/**
 * Created by okraskat on 06.02.16.
 */
public class Cola implements Product {

    @Override
    public BigDecimal getPrice() {
        return new BigDecimal("2.0");
    }

    @Override
    public String getName() {
        return "Cola";
    }

}
