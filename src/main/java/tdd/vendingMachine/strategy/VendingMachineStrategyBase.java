package tdd.vendingMachine.strategy;

import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.cash.register.ICashBox;
import tdd.vendingMachine.display.IDisplay;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.request.Request;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;
import tdd.vendingMachine.shelf.IShelf;

public abstract class VendingMachineStrategyBase implements IVendingMachineStrategy {
    public void insertProduct(IDisplay display, IShelf shelf, Product product) throws CannotChangeShelfProductsTypeException {
        showInvalidStateMessage(display);
    }

    public void cancelRequest(IDisplay display, ICashBox cashBox) {
        showInvalidStateMessage(display);
    }

    public Request selectProduct(IDisplay display, IShelf shelf) {
        showInvalidStateMessage(display);
        return null;
    }

    public void insertCoinToCashBox(IDisplay display, ICashBox cashBox, Coin coin) {
        showInvalidStateMessage(display);
    }

    private void showInvalidStateMessage(IDisplay display) {
        display.showInvalidActionForMachineStateMessage();
    }

}
