package tdd.vendingMachine.bdd;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import tdd.vendingMachine.*;
import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.util.CoinsHelper;

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
        Integer price = 500;
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
        Product product = new Product("cola", 500);
        inventory.put(1, product);
        moneyHolder.insert(500);

        // when
        userPanel.selectProduct(1);

        // then
        verify(bucket).putInto(null, product);
    }

    @Test
    public void selectProductWithNotEnoughMoney_shouldNotPutProductInBucket() {
        // given
        Product product = new Product("cola", 500);
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
        Product product = new Product("cola", 500);
        inventory.put(1, product);
        userPanel.selectProduct(1);

        // when
        moneyHolder.insert(200);

        // then
        verify(display).display("Please add : 300");
    }

    @Test
    public void insertCoinNotSelectedProduct_shouldDisplayBalance() {
        // given
        Integer coin = 200;

        // when
        moneyHolder.insert(coin);

        // then
        verify(display).display("Balance : " + coin);
    }

    @Test
    public void insertCoinSelectedProductWithEnoughMoney_shouldPutProductAndChange() {
        // given
        Product product = new Product("candy", 100);
        inventory.put(1, product);
        account.makeDeposit(CoinsHelper.listWithCoins(200, 200)); // for change
        userPanel.selectProduct(1);

        // when
        moneyHolder.insert(500);

        // then
        verify(bucket).putInto(CoinsHelper.listWithCoins(200, 200), product);
    }

    @Test
    public void insertCoinWithNotSelectedProduct_shouldNotPutProduct() {
        // given
        Product product = new Product("cola", 500);
        inventory.put(1, product);

        // when
        moneyHolder.insert(500);

        // then
        verify(bucket, never()).putInto(any(), eq(product));
    }

    @Test
    public void insertCoinNotEnoughMoneyForChange_shouldReturnInsertedCoins() {
        // given
        Product product = new Product("cola", 500);
        inventory.put(1, product);

        // when
        moneyHolder.insert(200);
        moneyHolder.insert(200);
        moneyHolder.insert(200);
        userPanel.selectProduct(1);

        // then
        verify(bucket).putInto(CoinsHelper.listWithCoins(200, 200, 200), null);
        verify(display).display("Balance : 200");
        verify(display).display("Balance : 400");
        verify(display).display("Balance : 600");
        verify(display).display("Warning! Doesn't have change");
    }

    @Test
    public void pressCancelDuringBuying_shouldGetMoneyBack() {
        // given
        Product product = new Product("cola", 500);
        inventory.put(1, product);
        moneyHolder.insert(200);

        // when
        userPanel.cancel();

        // then
        verify(bucket).putInto(CoinsHelper.listWithCoins(200), null);
    }

    @Test
    public void makeDeposit_shouldMakeDepositAfterInsertingCoins() {
        // given
        Product product = new Product("cola", 500);
        inventory.put(1, product);

        // when
        moneyHolder.insert(500);
        moneyHolder.insert(200);

        // then
        verify(account).makeDeposit(500);
        verify(account).makeDeposit(200);
    }

    @Test
    public void gettingFromInventory_shouldGetProductFromInventory() {
        // given
        Product insertingProduct = new Product("cola", 500);
        inventory.put(1, insertingProduct);

        // when
        userPanel.selectProduct(1);
        moneyHolder.insert(500);

        // then
        verify(inventory).getAndDelete(1);
    }

}
