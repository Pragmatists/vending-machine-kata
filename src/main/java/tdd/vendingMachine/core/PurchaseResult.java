package tdd.vendingMachine.core;

import java.util.Collection;

public interface PurchaseResult {
    Product getProduct();
    Collection<CurrencyUnit> getChange();
    boolean isSuccessful();
}
