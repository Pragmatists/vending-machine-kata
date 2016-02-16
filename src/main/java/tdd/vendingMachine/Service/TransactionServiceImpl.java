package tdd.vendingMachine.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of what the current status of the "buy-transaction" is.
 * Should an MachineException occur, all registered rollback-abe actors will rollback, and
 * the transaction will be closed.
 */
public class TransactionServiceImpl implements TransactionService {
    private boolean inTransaction;
    private int selecedShelf;
    private int selectedPid;
    private int selectedPrice;
    private int insertedMoney;
    private List<Integer> insertedCoins;        //should same coins be returned

    public TransactionServiceImpl() {
        cleanUp();
    }

    @Override
    public boolean isReadyForCommit() {
        return false;
    }

    @Override
    public void insertCoin(int nominal) {

    }

    @Override
    public boolean isInTransaction() {
        return false;
    }

    @Override
    public int getSelectedShelf() {
        return 0;
    }

    @Override
    public int getSelectedPid() {
        return 0;
    }

    @Override
    public int getSelectedPrice() {
        return 0;
    }

    @Override
    public int getInsertedMoney() {
        return 0;
    }

    @Override
    public int stillNeededFunds() {
        return 0;
    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    //--------------
    private void cleanUp() {
        inTransaction = false;
        selecedShelf = -1;
        selectedPid = -1;
        selectedPrice = 0;
        insertedMoney = 0;
        insertedCoins = new ArrayList<>();
    }

}
