package tdd.vendingMachine.Service;

import tdd.vendingMachine.Config.MachineConfig;
import tdd.vendingMachine.Domain.CoinRepo;
import tdd.vendingMachine.Domain.ProductRepo;
import tdd.vendingMachine.Domain.StorageRepo;

/**
 * The main implementation of this vending machine.
 * Allows Client- and Admin- operations.
 */
public class MainControllerImpl implements MachineClientController {
    TransactionServiceImpl transaction;
    ProductRepo productRepo;
    StorageRepo storageRepo;
    CoinRepo coinRepo;

    public MainControllerImpl(ProductRepo productRepo, StorageRepo storageRepo, CoinRepo coinRepo) {
        this.productRepo = productRepo;
        this.storageRepo = storageRepo;
        this.coinRepo = coinRepo;
        transaction = new TransactionServiceImpl(productRepo, storageRepo, coinRepo);
    }

    public MainControllerImpl(MachineConfig cfg) {
        this.productRepo = cfg.getProductRepo();
        this.storageRepo = cfg.getStorageRepo();
        this.coinRepo = cfg.getCoinRepo();
        transaction = new TransactionServiceImpl(productRepo, storageRepo, coinRepo);
    }

    @Override
    public Response insertCoin(int nominal) {
        //must be in transaction
        //Stub of implementation
//        if (!transaction.isInTransaction())
//            return new Response(ResponseMessage.PRODUCT_NOT_SELECTED.toString());
//        try {
//            transaction.insertCoin(nominal);
//        } catch (RuntimeException e) {
//            return new Response(e.toString()); //need better verbal message
//        }
//        if (transaction.isReadyForCommit()) {
//            transaction.commit();
//            Response r = new Response();
//            r.setResult(ResponseMessage.PRODUCT_SERVED_CHANGE_DISBURSED);
//        }
//        Response r = new Response();
//        r.setResult(ResponseMessage.COIN_INSERTED_MORE_NEEDED);
        return null;
    }


    @Override
    public Response getShelfContents() {
        return null;
    }

    @Override
    public Response selectShelf(int shelf) {
        return null;
    }

    @Override
    public Response cancelTransaction() {
        return null;
    }
}

