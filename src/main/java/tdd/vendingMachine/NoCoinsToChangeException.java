package tdd.vendingMachine;

/**
 * @author macko
 * @since 2015-08-19
 */
public class NoCoinsToChangeException extends RuntimeException {
    public NoCoinsToChangeException() {
        super("There's no coins to change, sorry");
    }
}
