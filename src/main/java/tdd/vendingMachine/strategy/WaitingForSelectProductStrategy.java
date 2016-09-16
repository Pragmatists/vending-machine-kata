package tdd.vendingMachine.strategy;

import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.cash.register.ICashBox;
import tdd.vendingMachine.display.IDisplay;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.request.Request;
import tdd.vendingMachine.shelf.IShelf;

public class WaitingForSelectProductStrategy extends VendingMachineStrategyBase {

    public boolean insertCoinForCurrentRequest(IDisplay display, ICashBox cashBox, Coin coin) {
        displayFirstSelectProductMessage(display);
        return false;
    }

    public void cancelRequest(ICashBox cashBox, IDisplay display) {
        displayFirstSelectProductMessage(display);
    }

    public Request selectProduct(IShelf shelf, IDisplay display) {
        if (shelf != null && !shelf.isEmpty()) {
            Product productSelected = shelf.pop();
            display.showProductSelectedMessage(productSelected);
            return new Request(shelf.getNumber(), productSelected);
        }
        display.showIncorrectProductSelectMessage();
        return null;
    }

    private void displayFirstSelectProductMessage(IDisplay display) {
        display.showFirstSelectProductMessage();
    }


}
