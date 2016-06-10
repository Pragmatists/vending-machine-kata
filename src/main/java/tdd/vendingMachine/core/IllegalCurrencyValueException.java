package tdd.vendingMachine.core;

public class IllegalCurrencyValueException extends IllegalArgumentException {

    public IllegalCurrencyValueException(String message) {
        super(message);
    }
}
