package tdd.vendingMachine.Domain;

/**
 * Codes of errors occuring in Domain
 */
public enum Error {
    INVALID_SHELF_NUMBER,
    INVALID_NUMBER_OF_ITEMS_AT_SHELF,
    CANNOT_ADD_TO_EMPTY_SHELF,
    NEGATIVE_NUMBER_OF_COINS,
    INVALID_COIN_NOMINAL,
    ERROR_SERVING_PRODUCT,
    ERROR_DISBURSING_COINS
}
