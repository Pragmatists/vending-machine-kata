package tdd.vendingMachine.strategy;

import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.cash.register.ICashBox;
import tdd.vendingMachine.display.IDisplay;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.request.Request;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;
import tdd.vendingMachine.shelf.IShelf;

public interface IVendingMachineStrategy {
    boolean insertCoinForCurrentRequest(IDisplay display, ICashBox cashBox, Coin coin);

    void insertProduct(IDisplay display, IShelf shelf, Product product) throws CannotChangeShelfProductsTypeException;

    void cancelRequest(IDisplay display, ICashBox cashBox);

    Request selectProduct(IDisplay display, IShelf shelf);

    void insertCoinToCashBox(IDisplay display, ICashBox cashBox, Coin coin);

}
