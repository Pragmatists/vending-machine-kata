package tdd.vendingMachine;

import tdd.vendingMachine.dto.Product;

import java.math.BigDecimal;

interface Shelf {

    static Shelf of(String productName, BigDecimal productPrice, int productAmount) {
        return new ShelfImpl(productName, productPrice, productAmount);
    }

    boolean isEmpty();

    Product dispense();

    String getProductName();

    BigDecimal getProductPrice();
}
