package tdd.vendingMachine.product;

import java.math.BigDecimal;

/**
 * Created by okraskat on 06.02.16.
 */
public class Cola implements Product {

    private final BigDecimal price = new BigDecimal("2.0");;

    private final String name = "Cola";
    @Override
    public BigDecimal getPrice() {

        return price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Cola: price=" + price + ", name='" + name + '\'';
    }
}
