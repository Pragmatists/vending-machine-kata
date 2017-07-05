package tdd.vendingMachine.bdd;

import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.*;
import tdd.vendingMachine.model.Product;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class VendingMachineTest {

    private Display display;
    private Account account;
    private Inventory inventory;
    private UserController userController;
    private CashHolder cashHolder;
    private Bucket bucket;

    @Before
    public void setUp() throws Exception {
        inventory.clean();
        account.withdraw();
    }

    @Test
    public void selectProduct_shouldDisplayPrice() {
        // given
        double price = 5;
        Product product = new Product("cola", price);
        inventory.put(product);

        // when
        userController.selectProduct(1);

        // then
        verify(display).display("Price: " + price);
    }

    @Test
    public void selectProductWithEnoughMoney_shouldPutProductInBucket() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(product);
        cashHolder.insertCoin(5);

        // when
        userController.selectProduct(1);

        // then
        verify(bucket).putInto(listWithCoins(5), product);
    }

    @Test
    public void selectProductWithNotEnoughMoney_shouldNotPutProductInBucket() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(product);

        // when
        userController.selectProduct(1);

        // then
        verify(bucket, never()).putInto(any(), any());
    }

    @Test
    public void selectNotExistingProduct_shouldDisplayWarning() {
        // given
        int shelveNum = 1;

        // when
        userController.selectProduct(shelveNum);

        // then
        verify(display).display("There is no product with " + shelveNum + " index");
    }

    @Test
    public void insertCoinSelectedProduct_shouldDisplayAmountThatMustBeAdded() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(product);
        userController.selectProduct(1);

        // when
        cashHolder.insertCoin(2);

        // then
        verify(display).display("Please add : " + 3);
    }

    @Test
    public void insertCoinNotSelectedProduct_shouldDisplayBalance() {
        // given
        double coin = 2;

        // when
        cashHolder.insertCoin(coin);

        // then
        verify(display).display("Balance : " + coin);
    }

    @Test
    public void insertCoinSelectedProductWithEnoughMoney_shouldPutProductAndChange() {
        // given
        Product product = new Product("candy", 1);
        inventory.put(product);
        account.makeDeposit(listWithCoins(2, 2)); // for change
        userController.selectProduct(1);

        // when
        cashHolder.insertCoin(5);

        // then
        verify(bucket).putInto(listWithCoins(2, 2), product);
    }

    @Test
    public void insertCoinWithNotSelectedProduct_shouldNotPutProduct() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(product);

        // when
        cashHolder.insertCoin(5);

        // then
        verify(bucket, never()).putInto(any(), product);
    }

    @Test
    public void insertCoinNotEnoughMoneyForChange_shouldReturnInsertedCoins() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(product);

        // when
        cashHolder.insertCoin(2);
        cashHolder.insertCoin(2);
        cashHolder.insertCoin(2);

        // then
        verify(bucket).putInto(listWithCoins(2, 2, 2), null);
        verify(display).display("Warning! Doesn't have change");
    }

    @Test
    public void pressCancelDuringBuying_shouldGetMoneyBack() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(product);
        cashHolder.insertCoin(2);

        // when
        userController.cancel();

        // then
        verify(bucket).putInto(listWithCoins(2), null);
    }

    @Test
    public void makeDeposit_shouldMakeDepositAfterInsertingCoins() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(product);

        // when
        cashHolder.insertCoin(1);
        cashHolder.insertCoin(2);

        // then
        verify(account).makeDeposit(2);
        verify(account).makeDeposit(2);
    }

    @Test
    public void gettingFromInventory_shouldGetProductFromInventory() {
        // given
        Product insertingProduct = new Product("cola", 5);
        inventory.put(insertingProduct);

        // when
        userController.selectProduct(1);
        cashHolder.insertCoin(1);

        // then
        Product gettingProduct = verify(inventory).get(1);
        assertEquals(insertingProduct, gettingProduct);
    }

    private List<Double> listWithCoins(double... coins) {
        List<Double> result = new ArrayList<>(coins.length);
        for (double coin : coins) {
            result.add(coin);
        }
        return result;
    }

}
