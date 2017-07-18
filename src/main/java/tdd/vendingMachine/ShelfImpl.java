package tdd.vendingMachine;

import tdd.vendingMachine.dto.Product;

import java.math.BigDecimal;

class ShelfImpl implements Shelf {
    private final String productName;
    private final BigDecimal productPrice;
    private int productAmount;

    ShelfImpl(String productName, BigDecimal productPrice, int productAmount) {
        this.productName = productName;
        this.productPrice = productPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        this.productAmount = productAmount;
    }

    @Override
    public boolean isEmpty() {
        return this.productAmount == 0;
    }

    @Override
    public Product dispense() {
        if (this.productAmount > 0) {
            this.productAmount--;
            return Product.of(this.productName, this.productPrice);
        }
        return null;
    }

    @Override
    public String getProductName() {
        return this.productName;
    }

    @Override
    public BigDecimal getProductPrice() {
        return this.productPrice;
    }
}
