package tdd.vendingMachine;

import tdd.vendingMachine.core.CurrencyUnit;
import tdd.vendingMachine.core.Shelf;
import tdd.vendingMachine.impl.BasicShelf;

import java.util.*;

public class VendingMachine {

    private final Set<CurrencyUnit> allowedDenominations = new HashSet<>();
    private final List<Shelf> shelves = new ArrayList<>();

    public VendingMachine addAllowedDenomination(CurrencyUnit currencyUnit) {
        if (currencyUnit != null) {
            allowedDenominations.add(currencyUnit);
            return this;
        }

        throw new IllegalArgumentException("Allowed denomination should be a valid number");
    }

    public Set<CurrencyUnit> getAllowedDenominations() {
        return Collections.unmodifiableSet(allowedDenominations);
    }

    public VendingMachine addShelf(BasicShelf shelf) {
        if (shelf != null) {
            shelves.add(shelf);
            return this;
        }

        throw new IllegalArgumentException("Shelf should be a valid object");
    }

    public List<Shelf> getShelves() {
        return Collections.unmodifiableList(shelves);
    }
}
