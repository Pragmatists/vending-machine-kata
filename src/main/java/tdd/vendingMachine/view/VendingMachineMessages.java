package tdd.vendingMachine.view;

/**
 * @author Agustin Cabra on 2/23/2017.
 * @since 1.0
 * Message enum intended to be used as keys for translations on language files e.g. en_US, pl_PL ...etc.
 */
public enum VendingMachineMessages { //TODO TODO02 evaluate the possibility to implement internationalization
    CANCEL("Cancelling ..."),
    CASH_ACCEPTED_NEW_CREDIT("Received, credit"),
    CASH_NOT_ACCEPTED_DISPENSER_FULL("returned to bucket (dispenser full try other denominations), credit"),
    CASH_NOT_ACCEPTED_MACHINE_SOLD_OUT("returned back to cash bucket, machine is sold out"),
    DISPENSED_TO_BUCKET("dispensed to pickup bucket"),
    NO_CREDIT_AVAILABLE("WARN: No credit available to return."),
    NO_PRODUCT_SELECTED("No product is selected"),
    NOT_ENOUGH_CASH_TO_GIVE_CHANGE("Not available cash to give change"),
    PENDING("Pending"),
    PRICE("Price"),
    PENDING_BALANCE_RETURNED_TO_BUCKET("Returned to cash dispenser, balance"),
    RETURN_TO_BUCKET_CREDIT("Returned to bucket, credit"),
    SHELF_NUMBER_NOT_AVAILABLE("Shelf number not available");

    private static final String WARN_SUBJECT_STRUCTURE = "WARN: %s [%s]";
    private static final String WARN_NO_SUBJECT_STRUCTURE = "WARN: %s";

    public final String label;

    VendingMachineMessages(String label) {
        this.label = label;
    }

    public static String buildWarningMessageWithSubject(String problem, int subject) {
        return String.format(WARN_SUBJECT_STRUCTURE, problem, subject);
    }

    public static String buildWarningMessageWithoutSubject(String problem) {
        return String.format(WARN_NO_SUBJECT_STRUCTURE, problem);
    }

    /**
     * Parses the given value to from cents to units only for display purposes
     * @param cash the amount of cents to present to screen
     * @return a string representing the amount required
     */
    public static String provideCashToDisplay(int cash) {
        return String.format("%.2f$", cash/100.0);
    }
}
