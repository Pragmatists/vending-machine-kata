package tdd.vendingMachine.Service;

import org.junit.Before;

import org.junit.Test;
import tdd.vendingMachine.Domain.CoinRepo;
import tdd.vendingMachine.Domain.Product;
import tdd.vendingMachine.Domain.ProductRepo;
import tdd.vendingMachine.Domain.StorageRepo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests elementary operations, and the whole sequence of "buy-transaction", with
 * various rollback conditions, as specified by the business requirements.
 */

public class TransactionServiceTest {
    ProductRepo productRepo;
    StorageRepo storageRepo;
    CoinRepo coinRepo;
    TransactionService tSrv;

    @Before
    public void setUp() {
        productRepo = new ProductRepo();
        productRepo.save(new Product("Sprite", 30));
        productRepo.save(new Product("Pizza", 100));

        storageRepo = new StorageRepo(4, 5);
        storageRepo.setProductAtShelf(0, 1, 3);
        storageRepo.setProductAtShelf(1, 2, 1);

        Integer[] nn = new Integer[]{10, 20, 50, 100, 200, 500};
        List<Integer> nom = Arrays.asList(nn);
        coinRepo = new CoinRepo(nom);

        tSrv = new TransactionServiceImpl(productRepo, storageRepo, coinRepo, new CoinChangerImpl(10));
    }

    @Test
    public void simpleBuyTransactionRunsOK() {
        tSrv.startTransaction(0);
        assertThat(tSrv.isInTransaction()).isEqualTo(true);
        assertThat(tSrv.getSelectedPid()).isEqualTo(1);
        assertThat(tSrv.getSelectedPrice()).isEqualTo(30);
        assertThat(tSrv.getSelectedShelf()).isEqualTo(0);

        tSrv.insertCoin(10);
        assertThat(tSrv.getInsertedMoney()).isEqualTo(10);
        assertThat(tSrv.isReadyForCommit()).isEqualTo(false);
        assertThat(tSrv.getNeededFunds()).isEqualTo(20);

        tSrv.insertCoin(20);
        assertThat(tSrv.getInsertedMoney()).isEqualTo(30);
        assertThat(tSrv.isReadyForCommit()).isEqualTo(true);
        assertThat(tSrv.getNeededFunds()).isEqualTo(0);
        assertThat(tSrv.getChangeSum()).isEqualTo(0);

        tSrv.commit();
        assertThat(tSrv.isInTransaction()).isEqualTo(false);
        assertThat(storageRepo.getCountAtShelf(0)).isEqualTo(2);
        assertThat(coinRepo.getMoneySumStored()).isEqualTo(30);

    }

    @Test
    public void buyTransactionWithChange() {
        coinRepo.addCoins(10, 3);
        tSrv.startTransaction(0);
        tSrv.insertCoin(50);

        assertThat(tSrv.isReadyForCommit()).isEqualTo(true);
        assertThat(tSrv.getNeededFunds()).isEqualTo(-20);
        assertThat(tSrv.getChangeSum()).isEqualTo(0);

        tSrv.commit();
        assertThat(storageRepo.getCountAtShelf(0)).isEqualTo(2);
        assertThat(coinRepo.getMoneySumStored()).isEqualTo(60);
        Map<Integer, Integer> coins = coinRepo.getCoins();
        assertThat(coins.get(10)).isEqualTo(1);
        assertThat(coins.get(50)).isEqualTo(1);
    }

    @Test
    public void buyTransactionCancel() {
        tSrv.startTransaction(0);   //needs 30; not coins in CoinRepo
        tSrv.insertCoin(10);
        tSrv.insertCoin(10);
        tSrv.rollback();
        assertThat(tSrv.isInTransaction()).isEqualTo(false);
        assertThat(storageRepo.getCountAtShelf(0)).isEqualTo(3);
        assertThat(coinRepo.getMoneySumStored()).isEqualTo(30);
        Map<Integer, Integer> coins = coinRepo.getCoins();
        assertThat(coins.get(10)).isEqualTo(3);
    }

    @Test
    public void buyTransactionCancelOnNotPayableChange() {
        coinRepo.addCoins(10, 3);
        tSrv.startTransaction(0);
        tSrv.insertCoin(200);
        assertThat(tSrv.isInTransaction()).isEqualTo(false);
        assertThat(tSrv.isReadyForCommit()).isEqualTo(false);
        assertThat(storageRepo.getCountAtShelf(0)).isEqualTo(3);
        assertThat(coinRepo.getMoneySumStored()).isEqualTo(30);
        Map<Integer, Integer> coins = coinRepo.getCoins();
        assertThat(coins.get(10)).isEqualTo(3);
        assertThat(coins.get(50)).isEqualTo(0);
    }

    @Test
    public void buyTransactionLockOnRepos() {
        coinRepo.addCoins(10, 3);
        tSrv.startTransaction(0);
        tSrv.insertCoin(10);
        assertThat(tSrv.isInTransaction()).isEqualTo(true);
        assertThatThrownBy(() -> {
            coinRepo.addCoins(10, 5);
        }).hasMessage(SrvError.TRANSACTION_IN_PROGRESS.toString());
    }



}
