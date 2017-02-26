package tdd.vendingMachine.domain.exception;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 * Represents the exception thrown when the cash dispenser is full and can't accept
 * additional cash.
 */
public class CashDispenserFullException extends Exception {

    private final int amountDeclined;

    public CashDispenserFullException(String message, int amountDeclined) {
        super(message);
        this.amountDeclined = amountDeclined;
    }
    public CashDispenserFullException(Throwable cause, String message, int amountDeclined) {
        super(message, cause);
        this.amountDeclined = amountDeclined;
    }

    public int getAmountDeclined() {
        return amountDeclined;
    }
}
