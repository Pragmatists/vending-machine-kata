package tdd.vendingMachine.display;

import tdd.vendingMachine.shelve.Shelve;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by okraskat on 06.02.16.
 */
public class DefaultDisplayFactory implements DisplayFactory {

    @Override
    public Display createDisplay(Map<Integer, Shelve> shelves) {
        checkNotNull(shelves, "Shelves can not be null");
        return new DefaultDisplay(shelves);
    }
}
