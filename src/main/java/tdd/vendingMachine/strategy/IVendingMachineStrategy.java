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

    void insertProduct(IShelf shelf, Product product, IDisplay display) throws CannotChangeShelfProductsTypeException;

    void cancelRequest(ICashBox cashBox, IDisplay display);

    Request selectProduct(IShelf shelf, IDisplay display);

    void insertCoinToCashBox(IDisplay display, ICashBox cashBox, Coin coin);

}
