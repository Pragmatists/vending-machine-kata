package tdd.vendingMachine.external_interface;

import tdd.vendingMachine.domain.parts.money.Coin;

import java.util.Map;

public interface HardwareInterface {

    void disposeInsertedCoins();

    void disposeChange(Map<Coin, Integer> returnedCoins);

    void disposeProduct(int shelfNumber);

    void displayMessage(String message);
}
