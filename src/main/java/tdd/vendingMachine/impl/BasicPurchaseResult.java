package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.CurrencyUnit;
import tdd.vendingMachine.core.Product;
import tdd.vendingMachine.core.PurchaseResult;

import java.util.Collection;

public class BasicPurchaseResult implements PurchaseResult {

    private final Product product;
    private final Collection<CurrencyUnit> change;

    public BasicPurchaseResult(Product product, Collection<CurrencyUnit> change) {
        this.product = product;
        this.change = change;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public Collection<CurrencyUnit> getChange() {
        return change;
    }

    @Override
    public boolean isSuccessful() {
        return product != null && change != null;
    }
}
