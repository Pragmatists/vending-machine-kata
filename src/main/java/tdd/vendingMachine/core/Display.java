package tdd.vendingMachine.core;

import java.util.List;

public interface Display {

    @FunctionalInterface
    interface Observer {
        Transaction shelfHasBeenSelected(int index);
    }

    interface Input {
        String readString();
        int readInt();
    }

    void run(List<Shelf> shelves);
    void displayProductPrice(CurrencyUnit productPrice);
}
