package tdd.vendingMachine.Transaction;

/**
 * Keeps track of what the current status of the "buy-transaction" is.
 * Should an MachineException occur, all registered rollback-abe actors will rollback, and
 * the transaction will be closed.
 */
public class BuyTransaction {
    boolean inTransaction;
    int selecedShelf;
    int selectedPid;
    int selectedPrice;
    int moneyInserted;


    //transactionstatus
    boolean isInTransaction() {
        return inTransaction;
    }


    public void commit() {
        //stub
    }

    public void rollback() {
        //stub
    }
}
