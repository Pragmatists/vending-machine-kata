package tdd.vendingMachine.domain.exception;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 * Exception representing that given size has exceeded the configuration permitted
 */
public class InvalidShelfSizeException extends RuntimeException {

    private final int maximumSize;
    private final int givenSize;

    public InvalidShelfSizeException(String message, int maximumSize, int givenSize) {
        super(message);
        this.maximumSize = maximumSize;
        this.givenSize = givenSize;
    }

    public InvalidShelfSizeException(Throwable cause, String message, int maximumSize, int givenSize) {
        super(message, cause);
        this.maximumSize = maximumSize;
        this.givenSize = givenSize;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public int getGivenSize() {
        return givenSize;
    }
}
