package tdd.vendingMachine.core;

public class IllegalProductNameException extends IllegalArgumentException {

    public IllegalProductNameException(String message) {
        super(message);
    }
}
