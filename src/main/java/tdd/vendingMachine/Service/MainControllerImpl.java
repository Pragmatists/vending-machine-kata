package tdd.vendingMachine.Service;

import tdd.vendingMachine.Config.MachineConfig;
import tdd.vendingMachine.Domain.CoinRepo;
import tdd.vendingMachine.Domain.Product;
import tdd.vendingMachine.Domain.ProductRepo;
import tdd.vendingMachine.Domain.StorageRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * The main implementation of this vending machine.
 * Allows Client- and Admin- operations.
 */
public class MainControllerImpl implements MachineClientController {
    TransactionServiceImpl transSrv;
    ProductRepo productRepo;
    StorageRepo storageRepo;
    CoinRepo coinRepo;

    public MainControllerImpl(ProductRepo productRepo, StorageRepo storageRepo, CoinRepo coinRepo,
                              CoinChanger changer) {
        this.productRepo = productRepo;
        this.storageRepo = storageRepo;
        this.coinRepo = coinRepo;
        transSrv = new TransactionServiceImpl(productRepo, storageRepo, coinRepo, changer);
    }

    public MainControllerImpl(MachineConfig cfg) {
        this.productRepo = cfg.getProductRepo();
        this.storageRepo = cfg.getStorageRepo();
        this.coinRepo = cfg.getCoinRepo();
        transSrv = new TransactionServiceImpl(productRepo, storageRepo, coinRepo, cfg.getCoinChanger());
    }

    @Override
    public Response insertCoin(int nominal) {
        if (!transSrv.isInTransaction())
            return new Response(ResponseMessage.PRODUCT_NOT_SELECTED.toString());
        try {
            transSrv.insertCoin(nominal);
        } catch (RuntimeException e) {
            return new Response(e.getMessage());
        }
        if (transSrv.isReadyForCommit()) {
            transSrv.commit();
            Response r = new Response();
            r.setResult(ResponseMessage.PRODUCT_SERVED_CHANGE_DISBURSED);
            return r;
        }
        Response r = new Response();
        int needed = transSrv.getNeededFunds();
        r.setResult(ResponseMessage.COIN_INSERTED_MORE_NEEDED.toString() + " (need: " + needed + ")");
        return r;
    }

    @Override
    public Response getShelfContents() {
        List<ShelfTransferObject> contents = formStoreContentsForView();
        Response r = new Response();
        r.setResult(contents);
        return r;
    }

    @Override
    public Response selectShelf(int shelf) {
        //starts transaction
        if (transSrv.isInTransaction())
            return new Response(ResponseMessage.MACHINE_IN_BUY_TRANSACTION.toString());
        try {
            transSrv.startTransaction(shelf);
        } catch (RuntimeException e) {
            return new Response(e.getMessage());
        }
        Integer fundsNeeded = transSrv.getNeededFunds();
        Response r = new Response();
        r.setResult("Shelf " + shelf + " selected. Insert " + fundsNeeded + ".");
        return r;
    }

    @Override
    public Response cancelTransaction() {
        Response r = new Response();
        if (transSrv.isInTransaction()) {
            transSrv.rollback();
            r.setResult(ResponseMessage.TRANSACTION_CANCELLED_INSERTED_MONEY_DISBURSED);
        } else {
            r.setResult(ResponseMessage.OK);
        }
        return r;
    }

    //-------------PRV
    List<ShelfTransferObject> formStoreContentsForView() {
        List<ShelfTransferObject> res = new ArrayList<>();
        int nshelves = storageRepo.getnShelves();
        for (int i = 0; i < nshelves; i++) {
            int pid = storageRepo.getPidAtShelf(i);
            if (pid==0) continue;
            Product p = productRepo.findOne(pid);
            if (p==null) throw new RuntimeException("Product on shelf not in catalog; should never happen");
            res.add(new ShelfTransferObject(i, p.getName(), p.getPrice()));
        }
        return res;
    }
}

