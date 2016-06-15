package tdd.vendingMachine.core;

import java.util.Collection;

public interface Display {

    @FunctionalInterface
    interface Observer {
        Transaction shelfHasBeenSelected(int index);
    }

    interface Input {
        String readString();
        int readInt();
    }

    void run(Collection<Shelf> shelves);
    void displayProductPrice(CurrencyUnit productPrice);
}
