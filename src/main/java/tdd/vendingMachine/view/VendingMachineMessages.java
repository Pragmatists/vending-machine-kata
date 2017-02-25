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
    COIN_SHELF_SIZE_EXCEEDS_MAX("Coin shelves size exceeds available coin values"),
    DISPENSED_TO_BUCKET("dispensed to pickup bucket"),
    NO_CREDIT_AVAILABLE("WARN: No credit available to return."),
    NO_PRODUCT_SELECTED("No product is selected"),
    NOT_ENOUGH_CASH_TO_GIVE_CHANGE("Not available cash to give change"),
    NOT_ENOUGH_SLOTS_AVAILABLE_DISPENSER("Dispenser has not available slots to provision amount"),
    PENDING("Pending"),
    PRICE("Price"),
    PRODUCT_SHELF_SIZE_EXCEEDS_MAX("The given product shelf size exceeds maximum"),
    PENDING_BALANCE_RETURNED_TO_BUCKET("Returned to cash to pickup bucket, balance"),
    RETURN_TO_BUCKET_CREDIT("Returned to bucket, credit"),
    RETURNING_TOTAL_CASH_TO_BUCKET("All credit will be returned back to bucket"),
    SHELF_NUMBER_NOT_AVAILABLE("Shelf number not available"),
    UNABLE_TO_SELECT_EMPTY_SHELF("No products to select on shelf");

    private static final String WARN_SUBJECT_STRUCTURE = "WARN: %s [%s]";
    private static final String WARN_NO_SUBJECT_STRUCTURE = "WARN: %s";

    public final String label;

    VendingMachineMessages(String label) {
        this.label = label;
    }

    public static String buildWarningMessageWithSubject(String problem, int subject) {
        return String.format(WARN_SUBJECT_STRUCTURE, problem, provideCashToDisplay(subject));
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
