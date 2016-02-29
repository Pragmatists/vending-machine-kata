package tdd.vendingMachine.external_interface;

public interface HardwareInterface {

    void disposeInsertedCoins();

    void disposeProduct(int shelfNumber);

    void displayMessage(String message);
}
