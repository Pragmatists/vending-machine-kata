package tdd.vendingMachine.Service;

/**
 * Operations needed for proper business logic of buying process.
 */
public interface TransactionService {

    //transactionstatus
    boolean isInTransaction();
    int getSelectedShelf();
    int getSelectedPid();
    int getSelectedPrice();
    int getInsertedMoney();

    int stillNeededFunds();

    boolean isReadyForCommit();

    void insertCoin(int nominal);

    //sell the product
    void commit();

    //cancel the transaction; return the inserted money
    void rollback();
}
