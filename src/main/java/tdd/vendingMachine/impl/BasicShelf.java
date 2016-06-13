package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.*;

public class BasicShelf implements Shelf {

    private final ProductName productName;
    private final ProductPrice productPrice;
    private int amountOfProducts = 0;

    public BasicShelf(ProductName productName, ProductPrice productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    @Override
    public Shelf charge(int amountOfProducts) {
        this.amountOfProducts += amountOfProducts;
        return this;
    }

    @Override
    public boolean hasProducts() {
        return amountOfProducts > 0;
    }

    @Override
    public Product withdraw() {
        if (amountOfProducts > 0) {
            --amountOfProducts;
            return new BasicProduct(productName, productPrice);
        } else {
            throw new IndexOutOfBoundsException("Shelf does not have any products");
        }
    }

    @Override
    public CurrencyUnit getProductPrice() {
        return productPrice.toCurrency();
    }
}
