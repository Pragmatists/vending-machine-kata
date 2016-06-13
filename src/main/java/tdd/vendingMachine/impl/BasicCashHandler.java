package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.CashHandler;
import tdd.vendingMachine.core.CurrencyUnit;

import java.util.ArrayList;
import java.util.Collection;

public class BasicCashHandler implements CashHandler {

    @Override
    public CashHandler deposit(Collection<CurrencyUnit> change) {
        return this;
    }

    @Override
    public Collection<CurrencyUnit> withdraw() {
        return new ArrayList<>();
    }

    @Override
    public CurrencyUnit amount() {
        return CurrencyUnit.zero();
    }
}
