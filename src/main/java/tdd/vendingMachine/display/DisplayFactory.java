package tdd.vendingMachine.display;

import tdd.vendingMachine.shelve.Shelve;

import java.util.Map;

/**
 * Created by okraskat on 06.02.16.
 */
public interface DisplayFactory {
    Display createDisplay(Map<Integer, Shelve> shelves);
}
