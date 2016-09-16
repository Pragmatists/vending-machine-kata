package tdd.vendingMachine;

import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;

public interface IVendingMachine {
    void insertCoinToCashBox(Coin coin);

    void insertCoinForCurrentRequest(Coin coin);

    void selectProduct(int shelfNumber);

    void insertProduct(int shelfNumber, Product product) throws CannotChangeShelfProductsTypeException;

    void cancelRequest();

    void displayMachineShelfsInformation();

    void turnOnMachineSetUpState();

    void turnOfMachineSetUpState();


}
