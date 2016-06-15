package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.CurrencyUnit;

import java.util.HashSet;
import java.util.Set;

public class AllowedDenominations {

    private final Set<CurrencyUnit> denominations = new HashSet<>();

    public AllowedDenominations add(CurrencyUnit currencyUnit) {
        if (currencyUnit == null) {
            throw new IllegalArgumentException("Allowed denominations accepts only valid currency");
        }

        denominations.add(currencyUnit);
        return this;
    }

    public boolean isAllowed(CurrencyUnit currencyUnit) {
        return denominations.contains(currencyUnit);
    }
}
