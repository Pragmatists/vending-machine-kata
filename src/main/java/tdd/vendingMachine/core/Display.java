package tdd.vendingMachine.core;

import java.util.List;

public interface Display {
    interface Observer {
        Transaction shelfHasBeenSelected(int index);
    }

    void displayShelves(List<Shelf> shelves);
    void displayProductPrice(CurrencyUnit productPrice);
}
