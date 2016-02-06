package tdd.vendingMachine.display;

import java.math.BigDecimal;

/**
 * Created by okraskat on 06.02.16.
 */
public interface Display {
    BigDecimal getProductPriceByShelveNumber(Integer shelveNumber);
}
