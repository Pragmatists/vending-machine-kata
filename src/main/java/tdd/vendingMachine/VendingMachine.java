package tdd.vendingMachine;

import tdd.vendingMachine.dto.Coin;
import tdd.vendingMachine.dto.Product;

import java.security.KeyException;
import java.util.List;

interface VendingMachine {

    void loadShelf(String key, Shelf shelf) throws KeyException;

    void insertCoin(Coin coin);

    String lookAtDisplay();

    void typeShelfNumber(int shelfNumber);

    void pressCancel();

    List<Coin> retrieveChange();

    List<Product> retrieveProducts();
}
