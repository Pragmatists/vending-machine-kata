package tdd.vendingMachine.Service;

/**
 * Service layer error conditions
 */
public enum SrvError {
    TRANSACTION_IN_PROGRESS,
    NOMINALS_NOT_DIVISIBLE_BY_DMONEY,
    SUM_NOT_DIVISIBLE_BY_DMONEY
}
