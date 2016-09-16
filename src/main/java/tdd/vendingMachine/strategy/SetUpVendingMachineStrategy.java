package tdd.vendingMachine.strategy;

import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.cash.register.ICashBox;
import tdd.vendingMachine.display.IDisplay;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;
import tdd.vendingMachine.shelf.IShelf;

public class SetUpVendingMachineStrategy extends VendingMachineStrategyBase {

    public boolean insertCoinForCurrentRequest(IDisplay display, ICashBox cashBox, Coin coin) {
        return false;
    }

    public void insertProduct(IDisplay display, IShelf shelf, Product product) throws CannotChangeShelfProductsTypeException {
        if (shelf != null) {
            shelf.setProductsType(product.getType());
            shelf.push(product);
            display.showInsertProductInformationMessage(shelf.getNumber(), product);
        } else {
            display.showInvalidShelfNumberMessage();
        }
    }

    public void insertCoinToCashBox(IDisplay display, ICashBox cashBox, Coin coin) {
        cashBox.addToCashBoxPocket(coin);
        display.showCoinAddedToCashBoxMessage(coin);
    }
}
