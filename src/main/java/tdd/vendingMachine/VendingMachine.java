package tdd.vendingMachine;

import com.sun.istack.internal.NotNull;

import java.util.*;

public class VendingMachine {
    private Map<Integer, Shelf> shelves;
    private Display display;

    public VendingMachine(Display display, Keyboard keyboard) {
        this.shelves = new HashMap<Integer, Shelf>();
        this.display = display;

        keyboard.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                selectShelf((Integer) arg);
            }
        });
    }

    public void selectShelf(int shelfNo) {
        display.setContent(String.valueOf(getShelf(shelfNo).getPrice()));
    }

    public void addProductToShelf(int shelfNo, Product product) {
        getShelf(shelfNo).addProduct(product);
    }

    public Product getProductFromShelf(int shelfNo) {
        return getShelf(shelfNo).getProduct();
    }

    @NotNull
    protected Shelf getShelf(int shelfNumber) {
        if (!shelves.containsKey(shelfNumber)) {
            shelves.put(shelfNumber, new Shelf());
        }
        return shelves.get(shelfNumber);
    }

}
