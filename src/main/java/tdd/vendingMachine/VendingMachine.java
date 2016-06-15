package tdd.vendingMachine;

import tdd.vendingMachine.core.*;
import tdd.vendingMachine.impl.AllowedDenominations;
import tdd.vendingMachine.impl.BasicDisplay;
import tdd.vendingMachine.impl.BasicTransaction;

import java.util.*;

public class VendingMachine implements Display.Observer {

    private final Display display = new BasicDisplay(this, new Display.Input() {
        private final Scanner scanner = new Scanner(System.in);

        @Override
        public String readString() {
            return scanner.next();
        }

        @Override
        public int readInt() {
            return scanner.nextInt();
        }
    });

    private final CashHandler cashHandler;
    private final AllowedDenominations allowedDenominations;
    private final List<Shelf> shelves = new ArrayList<>();

    public VendingMachine(CashHandler cashHandler, AllowedDenominations allowedDenominations) {
        this.cashHandler = cashHandler;
        this.allowedDenominations = allowedDenominations;
    }

    public VendingMachine addShelf(Shelf shelf) {
        if (shelf != null) {
            shelves.add(shelf);
            return this;
        }

        throw new IllegalArgumentException("Shelf should be a valid object");
    }

    public List<Shelf> getShelves() {
        return Collections.unmodifiableList(shelves);
    }

    public Transaction selectShelf(int index) {
        if (index >= 0 && shelves.size() > index) {
            return new BasicTransaction(shelves.get(index), cashHandler, allowedDenominations);
        }

        throw new IndexOutOfBoundsException("There is no shelf at index " + index);
    }

    public CurrencyUnit moneyAmount() {
        return cashHandler.amount();
    }

    @Override
    public Transaction shelfHasBeenSelected(int index) {
        return selectShelf(index);
    }

    void run() {
        display.run(shelves);
    }
}
