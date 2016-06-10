package tdd.vendingMachine.core;

public class IllegalProductPriceException extends IllegalArgumentException {

    public IllegalProductPriceException(String message) {
        super(message);
    }
}
