package tdd.vendingMachine.state;

/**
 * @author Agustin Cabra on 3/2/2017.
 * @since 2.1
 * Enum to represent the available states on the system for a given vending machine
 */
public enum StateEnum {
    /**
     * Represents technical error
     */
    TECHNICAL_ERROR,

    /**
     * The machine has no products
     */
    SOLD_OUT,

    /**
     * Machine is ready to sell products
     */
    READY,

    /**
     * Pending credit for purchase a given selected product
     */
    INSUFFICIENT_CREDIT,

    /**
     * Credit on the vending machine but no product selected
     */
    CREDIT_NOT_SELECTED_PRODUCT,

    /**
     * No credit but a product was selected
     */
    NO_CREDIT_SELECTED_PRODUCT
}
