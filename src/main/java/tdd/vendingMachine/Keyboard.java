package tdd.vendingMachine;

import java.util.Observable;

/**
 * @author macko
 * @since 2015-08-19
 */
public class Keyboard extends Observable {

    public static final int CANCEL_BUTTON = -1;

    public void select(int number) {
        setChanged();
        notifyObservers(number);
    }
}
