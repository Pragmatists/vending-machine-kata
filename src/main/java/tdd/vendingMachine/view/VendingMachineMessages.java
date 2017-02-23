package tdd.vendingMachine.view;

/**
 * @author Agustin Cabra on 2/23/2017.
 * @since 1.0
 * Message enum intended to be used as keys for translations on language files e.g. en_US, pl_PL ...etc.
 */
public enum VendingMachineMessages { //TODO TODO02 evaluate the possibility to implement internationalization
    CASH_ACCEPTED_NEW_CREDIT("Received, credit"),
    CASH_NOT_ACCEPTED_DISPENSER_FULL("returned to bucket (dispenser full try other denominations), credit"),
    CASH_NOT_ACCEPTED_MACHINE_SOLD_OUT("returned back to cash bucket, machine is sold out"),
    DISPENSED_TO_BUCKET("dispensed to pickup bucket"),
    NO_CREDIT_AVAILABLE("WARN: No credit available to return."),
    PENDING("Pending"),
    PRICE("Price"),
    RETURN_TO_BUCKET_CREDIT("Returned to bucket, credit"),
    SHELF_NUMBER_NOT_AVAILABLE("Shelf number not available"),
    NO_PRODUCT_SELECTED("No product is selected");

    private static final String WARN_CAUSE_STRUCTURE = "WARN: %s [%s]";
    public final String label;

    VendingMachineMessages(String label) {
        this.label = label;
    }

    public static String buildWarningMessageWithCause(String problem, int subject) {
        return String.format(WARN_CAUSE_STRUCTURE, problem, subject);
    }
}
