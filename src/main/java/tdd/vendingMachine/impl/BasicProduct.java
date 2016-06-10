package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.Product;
import tdd.vendingMachine.core.ProductName;
import tdd.vendingMachine.core.ProductPrice;

public class BasicProduct implements Product {
    private final ProductName productName;
    private final ProductPrice productPrice;

    public BasicProduct(ProductName productName, ProductPrice productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    @Override
    public String getName() {
        return productName.value();
    }

    @Override
    public String getPrice() {
        return productPrice.value();
    }
}
