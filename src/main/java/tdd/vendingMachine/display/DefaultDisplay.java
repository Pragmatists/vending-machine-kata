package tdd.vendingMachine.display;

import java.math.BigDecimal;

/**
 * Created by okraskat on 06.02.16.
 */
public class DefaultDisplay implements Display {

    @Override
    public void showWarning(String warning) {
        System.out.println("WARNING!!!!");
        System.out.println(warning);
    }

    @Override
    public void showProductPrice(BigDecimal selectedProductPrice) {
        System.out.println("Product price: " + selectedProductPrice);
    }

    @Override
    public void showChangeWarning() {
        System.out.println("VendingMachine can not give change. Returning money");
    }

    @Override
    public void showCoverAmount(BigDecimal subtract) {
        System.out.println("Missing moneys to cover: " + subtract);
    }

    @Override
    public void showEmptyShelve() {
        System.out.println("Selected shelve is empty");
    }

    @Override
    public void showProductNotSelected() {
        System.out.println("Product not selected");
    }
}
