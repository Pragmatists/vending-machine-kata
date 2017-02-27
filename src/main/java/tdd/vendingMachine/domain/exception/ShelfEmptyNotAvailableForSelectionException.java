package tdd.vendingMachine.domain.exception;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 * The exception thrown when the an empty shelf is attempted for selection.
 */
public class ShelfEmptyNotAvailableForSelectionException extends Exception {

    private final int shelfNumber;

    public ShelfEmptyNotAvailableForSelectionException(String message, int shelfNumber) {
        super(message);
        this.shelfNumber = shelfNumber;
    }

    public ShelfEmptyNotAvailableForSelectionException(Throwable cause, String message, int shelfNumber) {
        super(message, cause);
        this.shelfNumber = shelfNumber;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }
}
