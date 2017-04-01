package tdd.vendingMachine.exception;

/**
 * @author kdkz
 */
public class ShelfNotExistException extends Exception {
    public ShelfNotExistException(String message) {
        super(message);
    }
}
