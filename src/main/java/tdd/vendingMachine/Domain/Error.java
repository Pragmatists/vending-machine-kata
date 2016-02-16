package tdd.vendingMachine.Domain;

/**
 * Codes of errors occuring in Domain
 */
public enum Error {
    INVALID_SHELF_NUMBER,
    INVALID_NUMBER_OF_ITEMS_AT_SHELF,
    CANNOT_ADD_TO_EMPTY_SHELF,
    COIN_INSERTION_ERROR,
    ERROR_SERVING_PRODUCT,
    ERROR_DISBURSING_COINS
}
