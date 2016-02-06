package tdd.vendingMachine;


import com.google.common.base.Function;
import com.google.common.collect.Maps;
import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.display.DisplayFactory;
import tdd.vendingMachine.shelve.Shelve;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class VendingMachine {

    private final List<Shelve> shelves;

    private final Display display;

    public VendingMachine(List<Shelve> shelves, DisplayFactory displayFactory, Function<? super Shelve, Integer> keyMapper) {
        checkNotNull(shelves, "Shelves can not be null");
        this.shelves = shelves;
        checkNotNull(displayFactory, "DisplayFactory can not be null");
        this.display = displayFactory.createDisplay(Maps.uniqueIndex(shelves, keyMapper));
    }

    public List<Shelve> getShelves() {
        return shelves;
    }

    public Display getDisplay() {
        return display;
    }
}
