package tdd.vendingMachine.Transaction;

/**
 * Keeps track of what the current status of the "buy-transaction" is.
 * Should an MachineException occur, all registered rollback-abe actors will rollback, and
 * the transaction will be closed.
 */
public class MachineTransactionManager implements TransactionAware {
    //transactionstatus
    boolean isInTransaction() {
        return false;
    }
    //list of registered actors


    public void commit() {
        //stub
    }

    public void rollback() {
        //stub
    }
}
