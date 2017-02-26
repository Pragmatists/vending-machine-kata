package tdd.vendingMachine.view;

/**
 * @author Agustin Cabra on 2/23/2017.
 * @since 1.0
 * Message enum intended to be used as keys for translations on language files e.g. en_US, pl_PL ...etc.
 */
public enum VendingMachineMessages { //TODO TODO02 evaluate the possibility to implement internationalization
    AVAILABLE("available"),
    CANCEL("Cancelling ..."),
    CASH_ACCEPTED_NEW_CREDIT("Received, credit"),
    CASH_NOT_ACCEPTED_DISPENSER_FULL("returned to bucket (dispenser full try other denominations), credit"),
    CASH_NOT_ACCEPTED_MACHINE_SOLD_OUT("returned back to cash bucket, machine is sold out"),
    COIN_SHELF_SIZE_INCOMPATIBLE("Coin shelves size different from the actual coin denominations on the system"),
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
    SHELF_TYPE_MUST_NOT_BE_EMPTY("Invalid shelfItem must not be empty"),
    UNABLE_TO_CREATE_VENDING_MACHINE_EXCEEDED_PRODUCT_SHELF_CAPACITY("Unable to create Vending Machine given product shelves contains shelf with exceeded capacity"),
    UNABLE_TO_CREATE_VENDING_MACHINE_EXCEEDED_COIN_SHELF_CAPACITY("Unable to create Vending Machine given coin shelves contains shelf with exceeded capacity"),
    UNABLE_TO_SELECT_EMPTY_SHELF("The selected shelf is empty");

    private static final String WARN_SUBJECT_STRUCTURE = "WARN: %s [%s]";
    private static final String WARN_NO_SUBJECT_STRUCTURE = "WARN: %s";

    public final String label;

    VendingMachineMessages(String label) {
        this.label = label;
    }

    /**
     * Builds a warning message and displays the subject as currency if required
     * @param problem the problem description of the warning
     * @param subject the subject of the warning
     * @param isSubjectCurrency describes if the subject means currency
     * @return
     */
    public static String buildWarningMessageWithSubject(String problem, int subject, boolean isSubjectCurrency) {
        return String.format(WARN_SUBJECT_STRUCTURE, problem, isSubjectCurrency ? provideCashToDisplay(subject) : subject);
    }

    /**
     * Builds a warning message and displays the subject as currency if required
     * @param problem the problem description of the warning
     * @param subject the subject of the warning treated as currency
     * @return a warning message
     */
    public static String buildWarningMessageWithSubject(String problem, int subject) {
        return buildWarningMessageWithSubject(problem, subject, true);
    }

    /**
     * Builds a warning message and displays the subject as currency if required
     * @param problem the problem description of the warning
     * @return a warning message
     */
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
