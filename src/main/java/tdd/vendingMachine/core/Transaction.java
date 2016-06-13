package tdd.vendingMachine.core;

public interface Transaction {
    Transaction insertCoin(CurrencyUnit currencyUnit);
    CurrencyUnit getShortFall();
}
