package tdd.vendingMachine.Transaction;

/**
 * Objects implementing this interface take part in the buy-transaction, and
 * are "rollback-able".
 */
public interface TransactionAware {
    void commit() throws MachineException;
    void rollback() throws MachineException;
}
