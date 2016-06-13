package tdd.vendingMachine.core;

import java.util.Collection;

public interface Transaction {
    Transaction insertCoin(CurrencyUnit currencyUnit);
    CurrencyUnit getShortFall();
    Collection<CurrencyUnit> rollback();
    PurchaseResult commit();
}
