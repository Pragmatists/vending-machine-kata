package tdd.vendingMachine.Service;

import java.util.List;

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
    boolean isInTransaction();

    void startTransaction(int selectedShelf) throws RuntimeException;

    /*
     * Methods with meaningful return value only in transaction.
     * [Else: they return default values]
     */
    int getSelectedShelf();
    int getSelectedPid();
    int getSelectedPrice();
    int getInsertedMoney();
    int getNeededFunds();

    void insertCoin(int nominal) throws RuntimeException;   // "not in transaction"

    //can discharge product and change
    boolean isReadyForCommit();

    /*
     * Methods with meaningful return value only after isReadyForCommit()==true.
     * [Else: they return {}, or 0 respectively]
     */
    List<Integer> getChange();
    int getChangeSum();

    //Discharge the product, discharge the change
    void commit();

    //Cancel the transaction; discharge inserted money
    void rollback();
}
