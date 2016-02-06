package tdd.vendingMachine;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class VendingMachine {

    private final List<Shelve> shelves;

    public VendingMachine(List<Shelve> shelves) {
        checkNotNull(shelves, "Shelves can not be null");
        this.shelves = shelves;
    }

    public List<Shelve> getShelves() {
        return shelves;
    }
}
