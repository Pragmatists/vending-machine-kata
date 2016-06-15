package tdd.vendingMachine.impl;

import tdd.vendingMachine.core.Shelf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ShelveContainer {

    private final List<Shelf> shelves = new ArrayList<>();

    public ShelveContainer add(Shelf shelf) {
        if (shelf == null) {
            throw new IllegalArgumentException("Shelf should be a valid object");
        }

        shelves.add(shelf);
        return this;
    }

    public Shelf get(int index) {
        if (index >= 0 && shelves.size() > index) {
            return shelves.get(index);
        }

        throw new IndexOutOfBoundsException("There is no shelf at index " + index);
    }

    public Collection<Shelf> getReadonlyShelves() {
        return Collections.unmodifiableList(shelves);
    }
}
