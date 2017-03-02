package tdd.vendingMachine.domain.exception;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 * Represents the exception thrown when the cash dispenser has not available slots
 * to receive a given amount of cash.
 */
public class NotEnoughSlotsAvailableDispenserException extends RuntimeException {

    public final int availableSlots;
    private final int amountToProvision;

    public NotEnoughSlotsAvailableDispenserException(String message, int availableSlots, int amountToProvision) {
        super(message);
        this.availableSlots = availableSlots;
        this.amountToProvision = amountToProvision;
    }

    public NotEnoughSlotsAvailableDispenserException(Throwable cause, String message, int availableSlots, int amountToProvision) {
        super(message, cause);
        this.availableSlots = availableSlots;
        this.amountToProvision = amountToProvision;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public int getAmountToProvision() {
        return amountToProvision;
    }
}
