package tdd.vendingMachine.Service;

/**
 * Messages served to client
 */
public enum ResponseMessage {
    PRODUCT_NOT_SELECTED,
    MACHINE_IN_BUY_TRANSACTION,
    PRODUCT_SERVED_CHANGE_DISBURSED,
    COIN_INSERTED_MORE_NEEDED,
    COIN_INSERTED_CANNOT_PAY_CHANGE_CANCELLING
}
