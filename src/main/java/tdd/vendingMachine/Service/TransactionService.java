package tdd.vendingMachine.Service;

/**
 * Operations needed for proper business logic of buying process.
 *
 * Typical sequence of actions:
 *
 * startTransaction
 * loop
 *      insertCoin
 *      isReadyForCommit
 *      getNeededFunds
 *
 * exits from loop: commit (product discharge) or rollback (inserted money back)
 *
 *
 *
 */
public interface TransactionService {

    /**
     * Variables storing the parameters of current transaction, i.e. the buy order.
     * They do reflect the result of all legal actions taken by the client since choosing
     * the product.
     */
    boolean isInTransaction();
    int getSelectedShelf();
    int getSelectedPid();
    int getSelectedPrice();
    int getInsertedMoney();

    void startTransaction(int selectedShelf) throws RuntimeException;

    int getNeededFunds();
    void insertCoin(int nominal) throws RuntimeException;
    boolean isReadyForCommit();     //can discharge product and change

    int getChangeSum();     //money to be paid as change (nominals not specified)

    //discharge the product
    void commit();

    //cancel the transaction; return the inserted money
    void rollback();
}
