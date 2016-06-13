package tdd.vendingMachine;

import tdd.vendingMachine.core.CurrencyUnit;
import tdd.vendingMachine.core.Shelf;
import tdd.vendingMachine.core.Transaction;
import tdd.vendingMachine.impl.BasicTransaction;

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

    public VendingMachine addShelf(Shelf shelf) {
        if (shelf != null) {
            shelves.add(shelf);
            return this;
        }

        throw new IllegalArgumentException("Shelf should be a valid object");
    }

    public List<Shelf> getShelves() {
        return Collections.unmodifiableList(shelves);
    }

    public Transaction selectShelf(int index) {
        if (shelves.size() > index) {
            return new BasicTransaction(shelves.get(index));
        }

        throw new IndexOutOfBoundsException("There is no shelf at index " + index);
    }
}
