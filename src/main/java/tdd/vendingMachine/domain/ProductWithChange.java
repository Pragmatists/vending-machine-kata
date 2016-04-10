package tdd.vendingMachine.domain;

import com.google.common.base.Optional;

public class ProductWithChange {

    private final Optional<Product> product;
    private final Coins change;

    public ProductWithChange(Optional<Product> product, Coins change) {
        this.product = product;
        this.change = change;
    }

    public Optional<Product> getProduct() {
        return product;
    }

    public Coins getChange() {
        return change;
    }

    @Override
    public String toString() {
        return "{" + product +
            ", " + change +
            '}';
    }
}
