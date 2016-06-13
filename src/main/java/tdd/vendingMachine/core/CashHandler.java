package tdd.vendingMachine.core;

import java.util.Collection;

public interface CashHandler {
    CashHandler deposit(Collection<CurrencyUnit> change);
    Collection<CurrencyUnit> withdraw();
    CurrencyUnit amount();
}
