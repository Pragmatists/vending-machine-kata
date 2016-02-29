package tdd.vendingMachine.external_interface;

import tdd.vendingMachine.domain.Coin;

import java.util.List;

public interface HardwareInterface {

    void disposeInsertedCoins(List<Coin> coins);

    void disposeProduct(int shelfNumber);

    void displayMessage(String message);
}
