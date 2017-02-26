package tdd.vendingMachine.domain.exception;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 * Represents the exception when the vending machine has not available cash from the dispenser
 * to provide a given balance.
 */
public class UnableToProvideBalanceException extends Exception {

    private final int pendingBalance;

    public UnableToProvideBalanceException(String message, int pendingBalance) {
        super(message);
        this.pendingBalance = pendingBalance;
    }
    public UnableToProvideBalanceException(Throwable cause, String message, int pendingBalance) {
        super(message, cause);
        this.pendingBalance = pendingBalance;
    }

    public int getPendingBalance() {
        return pendingBalance;
    }
}
