package tdd.vendingMachine;

import com.google.common.base.Optional;
import tdd.vendingMachine.domain.Coins;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.ProductWithChange;

public abstract class VendingMachineState {

    protected VendingMachine vendingMachine;

    protected VendingMachineState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    protected void notSupportedOperation() {
        vendingMachine.display.displayText("Nie obsługiwana operacja...");
    }

    protected ProductWithChange dispense() {
        return new ProductWithChange(Optional.<Product>absent(), Coins.empty());
    }

    protected Coins returnCoins() {
        return Coins.empty();
    }

    protected void insertCoin(Coins coins) {
        notSupportedOperation();
    }

    protected void selectShelf(int shelfNumber) {
        vendingMachine.shelfs.setSelectedShelf(shelfNumber);
    }

}
