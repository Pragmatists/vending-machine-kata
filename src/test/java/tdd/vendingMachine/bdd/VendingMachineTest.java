package tdd.vendingMachine.bdd;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import tdd.vendingMachine.*;
import tdd.vendingMachine.model.Product;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class VendingMachineTest {

    @Captor
    private Display display = spy(new LiquidCrystalDisplay());
    private Account account = spy(new CashAccount());
    private Inventory inventory = spy(new ShelveInventory());
    private Bucket bucket = spy(new BucketImpl());
    private Memory memory = spy(new KingstonMemoryCard());
    private HardwareController hardwareController = spy(new SccmController(display, inventory, account, bucket, memory));
    private UserPanel userPanel = spy(new ButtonPanel(hardwareController));
    private MoneyHolder moneyHolder = spy(new CoinHolder(hardwareController));

    @Before
    public void setUp() throws Exception {
        inventory.clean();
        account.withdrawAll();
    }

    @Test
    public void selectProduct_shouldDisplayPrice() {
        // given
        double price = 5;
        Product product = new Product("cola", price);
        inventory.put(1, product);

        // when
        userPanel.selectProduct(1);

        // then
        verify(display).display("Price: " + price);
    }

    @Test
    public void selectProductWithEnoughMoney_shouldPutProductInBucket() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(1, product);
        moneyHolder.insert(5);

        // when
        userPanel.selectProduct(1);

        // then
        verify(bucket).putInto(listWithCoins(5), product);
    }

    @Test
    public void selectProductWithNotEnoughMoney_shouldNotPutProductInBucket() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(1, product);

        // when
        userPanel.selectProduct(1);

        // then
        verify(bucket, never()).putInto(any(), any());
    }

    @Test
    public void selectNotExistingProduct_shouldDisplayWarning() {
        // given
        int shelveNum = 1;

        // when
        userPanel.selectProduct(shelveNum);

        // then
        verify(display).display("There is no product with " + shelveNum + " index");
    }

    @Test
    public void insertCoinSelectedProduct_shouldDisplayAmountThatMustBeAdded() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(1, product);
        userPanel.selectProduct(1);

        // when
        moneyHolder.insert(2);

        // then
        verify(display).display("Please add : " + 3);
    }

    @Test
    public void insertCoinNotSelectedProduct_shouldDisplayBalance() {
        // given
        double coin = 2;

        // when
        moneyHolder.insert(coin);

        // then
        verify(display).display("Balance : " + coin);
    }

    @Test
    public void insertCoinSelectedProductWithEnoughMoney_shouldPutProductAndChange() {
        // given
        Product product = new Product("candy", 1);
        inventory.put(1, product);
        account.makeDeposit(listWithCoins(2, 2)); // for change
        userPanel.selectProduct(1);

        // when
        moneyHolder.insert(5);

        // then
        verify(bucket).putInto(listWithCoins(2, 2), product);
    }

    @Test
    public void insertCoinWithNotSelectedProduct_shouldNotPutProduct() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(1, product);

        // when
        moneyHolder.insert(5);

        // then
        verify(bucket, never()).putInto(any(), product);
    }

    @Test
    public void insertCoinNotEnoughMoneyForChange_shouldReturnInsertedCoins() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(1, product);

        // when
        moneyHolder.insert(2);
        moneyHolder.insert(2);
        moneyHolder.insert(2);

        // then
        verify(bucket).putInto(listWithCoins(2, 2, 2), null);
        verify(display).display("Warning! Doesn't have change");
    }

    @Test
    public void pressCancelDuringBuying_shouldGetMoneyBack() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(1, product);
        moneyHolder.insert(2);

        // when
        userPanel.cancel();

        // then
        verify(bucket).putInto(listWithCoins(2), null);
    }

    @Test
    public void makeDeposit_shouldMakeDepositAfterInsertingCoins() {
        // given
        Product product = new Product("cola", 5);
        inventory.put(1, product);

        // when
        moneyHolder.insert(0.5);
        moneyHolder.insert(0.2);

        // then
        verify(account).makeDeposit(0.5);
        verify(account).makeDeposit(0.2);
    }

    @Test
    public void gettingFromInventory_shouldGetProductFromInventory() {
        // given
        Product insertingProduct = new Product("cola", 5);
        inventory.put(1, insertingProduct);

        // when
        userPanel.selectProduct(1);
        moneyHolder.insert(1);

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
