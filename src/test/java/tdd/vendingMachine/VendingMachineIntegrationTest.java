package tdd.vendingMachine;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.dto.Coin;
import tdd.vendingMachine.dto.Product;

import java.math.BigDecimal;
import java.security.KeyException;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class VendingMachineIntegrationTest {

    private VendingMachine vendingMachine;
    private final String key = "pass";

    @Before
    public void setup() {
        VendingMachineInjector vendingMachineInjector = new VendingMachineInjector();
        vendingMachineInjector.setKey(this.key);
        Injector injector = Guice.createInjector(vendingMachineInjector);
        this.vendingMachine = injector.getInstance(VendingMachine.class);
    }

    @Test
    public void lookAtDisplayTest() {
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Welcome! Please enter shelf number.")));
    }

    @Test
    public void buyProductNoChangeTest() throws KeyException {
        this.vendingMachine.loadShelf(this.key, Shelf.of("test", BigDecimal.TEN, 10));
        this.vendingMachine.typeShelfNumber(0);
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Product remaining price: 10.00\n")));
        this.vendingMachine.insertCoin(Coin.FIVE);
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Product remaining price: 5.00\n")));
        this.vendingMachine.insertCoin(Coin.DOT_FIVE);
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Product remaining price: 4.50\n")));
        this.vendingMachine.insertCoin(Coin.DOT_TWO);
        this.vendingMachine.insertCoin(Coin.DOT_TWO);
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Product remaining price: 4.10\n")));
        this.vendingMachine.insertCoin(Coin.DOT_ONE);
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Product remaining price: 4.00\n")));
        this.vendingMachine.insertCoin(Coin.TWO);
        this.vendingMachine.insertCoin(Coin.TWO);
        assertThat(this.vendingMachine.retrieveProducts(), is(equalTo(Arrays.asList(Product.of("test", BigDecimal.TEN)))));
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Welcome! Please enter shelf number.")));
    }

    @Test
    public void cancelBeforePurchaseChangeTest() throws KeyException {
        this.vendingMachine.loadShelf(this.key, Shelf.of("test", BigDecimal.TEN, 10));
        this.vendingMachine.typeShelfNumber(0);
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Product remaining price: 10.00\n")));
        this.vendingMachine.insertCoin(Coin.FIVE);
        this.vendingMachine.insertCoin(Coin.TWO);
        this.vendingMachine.insertCoin(Coin.TWO);
        this.vendingMachine.pressCancel();
        assertThat(this.vendingMachine.retrieveProducts(), is(empty()));
        assertThat(this.vendingMachine.retrieveChange(), is(equalTo(Arrays.asList(Coin.FIVE, Coin.TWO, Coin.TWO))));
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Welcome! Please enter shelf number.")));
    }

    @Test
    public void cannotBuyProductNotEnoughChangeTest() throws KeyException {
        this.vendingMachine.loadShelf(this.key, Shelf.of("test", BigDecimal.TEN, 10));
        this.vendingMachine.typeShelfNumber(0);
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Product remaining price: 10.00\n")));
        this.vendingMachine.insertCoin(Coin.FIVE);
        this.vendingMachine.insertCoin(Coin.TWO);
        this.vendingMachine.insertCoin(Coin.TWO);
        this.vendingMachine.insertCoin(Coin.TWO);
        assertThat(this.vendingMachine.retrieveProducts(), is(empty()));
        assertThat(this.vendingMachine.retrieveChange(), is(equalTo(Arrays.asList(Coin.FIVE, Coin.TWO, Coin.TWO, Coin.TWO))));
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Product remaining price: 1.00\nWarning: The change can not be returned. Please take the change and enter new shelf number.\n")));
        this.vendingMachine.pressCancel();
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Welcome! Please enter shelf number.")));
    }

    @Test
    public void buyProductWithChangeTest() throws KeyException {
        this.vendingMachine.loadShelf(this.key, Shelf.of("test", BigDecimal.TEN, 10));
        this.vendingMachine.typeShelfNumber(0);
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Product remaining price: 10.00\n")));
        this.vendingMachine.insertCoin(Coin.FIVE);
        this.vendingMachine.insertCoin(Coin.ONE);
        this.vendingMachine.insertCoin(Coin.ONE);
        this.vendingMachine.insertCoin(Coin.TWO);
        this.vendingMachine.insertCoin(Coin.TWO);
        assertThat(this.vendingMachine.retrieveProducts(), is(equalTo(Arrays.asList(Product.of("test", BigDecimal.TEN)))));
        assertThat(this.vendingMachine.retrieveChange(), is(equalTo(Arrays.asList(Coin.ONE))));
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Welcome! Please enter shelf number.")));
    }

    @Test
    public void buyAllProductsOnShelfTest() throws KeyException {
        this.vendingMachine.loadShelf(this.key, Shelf.of("test", BigDecimal.TEN, 1));
        this.vendingMachine.typeShelfNumber(0);
        this.vendingMachine.insertCoin(Coin.FIVE);
        this.vendingMachine.insertCoin(Coin.FIVE);
        this.vendingMachine.typeShelfNumber(0);
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Warning: Invalid shelf number or shelf is empty. Please enter a different shelf number.\n")));
    }

    @Test
    public void buyProductFromDifferentShelvesTest() throws KeyException {
        this.vendingMachine.loadShelf(this.key, Shelf.of("test1", BigDecimal.TEN, 10));
        this.vendingMachine.loadShelf(this.key, Shelf.of("test2", BigDecimal.ONE, 10));
        this.vendingMachine.typeShelfNumber(0);
        this.vendingMachine.insertCoin(Coin.FIVE);
        this.vendingMachine.insertCoin(Coin.FIVE);
        this.vendingMachine.typeShelfNumber(1);
        this.vendingMachine.insertCoin(Coin.ONE);
        assertThat(this.vendingMachine.retrieveProducts(), is(equalTo(Arrays.asList(Product.of("test1", BigDecimal.TEN), Product.of("test2", BigDecimal.ONE)))));
        assertThat(this.vendingMachine.retrieveChange(), is(empty()));
        assertThat(this.vendingMachine.lookAtDisplay(), is(equalTo("Welcome! Please enter shelf number.")));
    }
}
