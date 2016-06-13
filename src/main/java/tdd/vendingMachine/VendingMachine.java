package tdd.vendingMachine;

import tdd.vendingMachine.core.CurrencyUnit;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class VendingMachine {

    private final Set<CurrencyUnit> allowedDenominations = new HashSet<>();

    public VendingMachine addAllowedDenomination(CurrencyUnit currencyUnit) {
        allowedDenominations.add(currencyUnit);
        return this;
    }

    public Set<CurrencyUnit> getAllowedDenominations() {
        return Collections.unmodifiableSet(allowedDenominations);
    }
}
