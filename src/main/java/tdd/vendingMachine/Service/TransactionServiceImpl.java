package tdd.vendingMachine.Service;

import tdd.vendingMachine.Domain.CoinRepo;
import tdd.vendingMachine.Domain.ProductRepo;
import tdd.vendingMachine.Domain.StorageRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of what the current status of the "buy-transaction" is.
 * Should an MachineException occur, all registered rollback-abe actors will rollback, and
 * the transaction will be closed.
 */
public class TransactionServiceImpl implements TransactionService {
    private ProductRepo productRepo;
    private StorageRepo storageRepo;
    private CoinRepo coinRepo;

    private boolean inTransaction;
    private int selecedShelf;
    private int selectedPid;
    private int selectedPrice;
    private int insertedMoney;
    private List<Integer> insertedCoins;        //should same coins be returned

    public TransactionServiceImpl(ProductRepo productRepo, StorageRepo storageRepo, CoinRepo coinRepo) {
        this.productRepo = productRepo;
        this.storageRepo = storageRepo;
        this.coinRepo = coinRepo;
        cleanUp();
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
    public void startTransaction(int selectedShelf) throws RuntimeException {

    }

    @Override
    public int getChangeSum() {
        return 0;
    }

    @Override
    public int getNeededFunds() {
        return 0;
    }

    @Override
    public void insertCoin(int nominal) throws RuntimeException {

    }

    @Override
    public boolean isReadyForCommit() {
        return false;
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
