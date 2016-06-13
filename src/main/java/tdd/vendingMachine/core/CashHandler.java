package tdd.vendingMachine.core;

import java.util.Collection;

public interface CashHandler {
    CashHandler deposit(Collection<CurrencyUnit> currencyUnits);
    CashHandler deposit(CurrencyUnit currencyUnit, int amount);
    Collection<CurrencyUnit> withdraw(CurrencyUnit currencyUnit);
    CurrencyUnit amount();
}
