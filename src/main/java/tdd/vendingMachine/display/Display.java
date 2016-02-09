package tdd.vendingMachine.display;

import java.math.BigDecimal;

/**
 * Created by okraskat on 06.02.16.
 */
public interface Display {

    void showWarning(String warning);

    void showProductPrice(BigDecimal selectedProductPrice);

    void showChangeWarning();

    void showCoverAmount(BigDecimal subtract);

    void showEmptyShelve();

    void showProductNotSelected();
}
