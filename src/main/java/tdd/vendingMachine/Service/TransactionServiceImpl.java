package tdd.vendingMachine.Service;

import tdd.vendingMachine.Domain.CoinRepo;
import tdd.vendingMachine.Domain.Product;
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
    private CoinChanger changer;

    private boolean inTransaction;
    private boolean readyForCommit;
    private int selecedShelf;
    private int selectedPid;
    private int selectedPrice;
    private int insertedMoney;
    private List<Integer> insertedCoins;        //should same coins be returned
    private List<Integer> change;   //computed only once in transaction, as insertedMony>=selectedPrice

    public TransactionServiceImpl(ProductRepo productRepo, StorageRepo storageRepo, CoinRepo coinRepo,
                                  CoinChanger changer) {
        this.productRepo = productRepo;
        this.storageRepo = storageRepo;
        this.coinRepo = coinRepo;
        this.changer = changer;
        cleanUp();
    }


    @Override
    public boolean isInTransaction() {
        return inTransaction;
    }

    @Override
    public int getSelectedShelf() {
        return selecedShelf;
    }

    @Override
    public int getSelectedPid() {
        return selectedPid;
    }

    @Override
    public int getSelectedPrice() {
        return selectedPrice;
    }

    @Override
    public int getInsertedMoney() {
        int sum = 0;
        for(int i : insertedCoins) sum += i;
        return sum;
    }

    @Override
    public void startTransaction(int selectedShelf) throws RuntimeException {
        if (isInTransaction())
            throw new RuntimeException(SrvError.TRANSACTION_IN_PROGRESS_CANT_START_NEW_ONE.toString());
        int pid = storageRepo.getPidAtShelf(selectedShelf);
        if (pid==0) throw new RuntimeException(SrvError.SELECTING_EMPTY_SHELF_NOT_ALLOWED.toString());
        cleanUp();  //insertedMoney=0, insertedCoins={}
        Product p = productRepo.findOne(pid);
        this.selectedPid = pid;
        this.selectedPrice = p.getPrice();
        this.selecedShelf = selectedShelf;
        this.inTransaction = true;
    }

    @Override
    public int getNeededFunds() {
        return selectedPrice - insertedMoney;
    }

    @Override
    public void insertCoin(int nominal) throws RuntimeException {
        if (!inTransaction)
            throw new RuntimeException(SrvError.CANT_INSERT_COINS_BEFORE_SELECTING_PRODUCT.toString());
        coinRepo.insertCoin(nominal);   //throws on bad nominal
        insertedCoins.add(nominal);
        insertedMoney += nominal;
        if (getNeededFunds()>0) return;
        change = changer.distribute(coinRepo.getCoins(), getNeededFunds());
        if (change==null) {
            rollback();
            throw new RuntimeException(SrvError.CANT_PAY_CHANGE.toString());
        }
        System.out.println("COMMIT-READY");
        readyForCommit = true;
    }

    @Override
    public boolean isReadyForCommit() {
        return readyForCommit;
    }

    @Override
    public List<Integer> getChange() {
        return change;
    }

    @Override
    public int getChangeSum() {
        return Utils.sumList(change);
    }

    @Override
    public void commit() throws RuntimeException {
        if (!readyForCommit)
            throw new RuntimeException(SrvError.CANT_COMMIT_WHILE_NOT_READY.toString());
        coinRepo.disburseCoins(change);
        cleanUp();
    }

    @Override
    public void rollback() {
        if (!inTransaction) return;
        coinRepo.disburseCoins(insertedCoins);
        cleanUp();
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
