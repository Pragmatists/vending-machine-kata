package tdd.vendingMachine;

import tdd.vendingMachine.core.*;
import tdd.vendingMachine.impl.AllowedDenominations;
import tdd.vendingMachine.impl.BasicDisplay;
import tdd.vendingMachine.impl.BasicTransaction;
import tdd.vendingMachine.impl.ShelveContainer;

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
    private final ShelveContainer shelveContainer;
    private final AllowedDenominations allowedDenominations;

    public VendingMachine(CashHandler cashHandler, ShelveContainer shelveContainer, AllowedDenominations allowedDenominations) {
        this.cashHandler = cashHandler;
        this.shelveContainer = shelveContainer;
        this.allowedDenominations = allowedDenominations;
    }

    public Transaction selectShelf(int index) {
        return new BasicTransaction(shelveContainer.get(index), cashHandler, allowedDenominations);
    }

    @Override
    public Transaction shelfHasBeenSelected(int index) {
        return selectShelf(index);
    }

    void run() {
        display.run(shelveContainer.getReadonlyShelves());
    }
}
