package tdd.vendingMachine;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tdd.vendingMachine.dto.Coin;
import tdd.vendingMachine.dto.Product;

import java.math.BigDecimal;
import java.security.KeyException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class VendingMachineTest {

    private final String key = "pass";
    private CashRegister cashRegister;
    private Display display;
    private ChangeDispenser changeDispenser;
    private ProductDispenser productDispenser;
    private VendingMachine vendingMachine;

    @Before
    public void setup() {
        AbstractModule abstractModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(Display.class).toInstance(mock(DisplayImpl.class));
                bind(CashRegister.class).toInstance(mock(CashRegisterImpl.class));
                bind(ChangeDispenser.class).toInstance(mock(ChangeDispenserImpl.class));
                bind(ProductDispenser.class).toInstance(mock(ProductDispenserImpl.class));
                bind(VendingMachine.class).to(VendingMachineImpl.class);
                bind(PasswordEncoder.class).toInstance(new BCryptPasswordEncoder());
            }

            @Provides
            @Named("key")
            public String getKey() {
                return "pass";
            }
        };
        Injector injector = Guice.createInjector(abstractModule);
        this.cashRegister = injector.getInstance(CashRegister.class);
        this.display = injector.getInstance(Display.class);
        this.changeDispenser = injector.getInstance(ChangeDispenser.class);
        this.productDispenser = injector.getInstance(ProductDispenser.class);
        this.vendingMachine = injector.getInstance(VendingMachine.class);
    }

    @Test
    public void lookAtDisplayTest() {
        this.vendingMachine.lookAtDisplay();
        verify(this.display, times(1)).view();
    }

    @Test
    public void pressCancelTest() {
        this.vendingMachine.pressCancel();
        verify(this.cashRegister, times(1)).reset();
    }

    @Test
    public void retrieveChangeTest() {
        this.vendingMachine.retrieveChange();
        verify(this.changeDispenser, times(1)).retrieveCoins();
    }

    @Test
    public void retrieveProductTest() {
        this.vendingMachine.retrieveProducts();
        verify(this.productDispenser, times(1)).retrieveProducts();
    }

    @Test
    public void shelfTest() throws KeyException {
        this.vendingMachine.loadShelf(this.key, Shelf.of("test", BigDecimal.TEN, 10));
        this.vendingMachine.loadShelf(this.key, Shelf.of("test", BigDecimal.ONE, 1));
        this.vendingMachine.typeShelfNumber(0);
        ArgumentCaptor<BigDecimal> productPriceArgumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        verify(this.cashRegister, times(1)).setProductPrice(productPriceArgumentCaptor.capture());
        assertThat(productPriceArgumentCaptor.getValue(), is(equalTo(BigDecimal.TEN.setScale(2, BigDecimal.ROUND_HALF_EVEN))));

        this.vendingMachine.typeShelfNumber(1);
        verify(this.cashRegister, times(2)).setProductPrice(productPriceArgumentCaptor.capture());
        assertThat(productPriceArgumentCaptor.getValue(), is(equalTo(BigDecimal.ONE.setScale(2, BigDecimal.ROUND_HALF_EVEN))));

        this.vendingMachine.typeShelfNumber(2);
        ArgumentCaptor<String> warningArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(this.display, times(1)).writeWarning(warningArgumentCaptor.capture());
        assertThat(warningArgumentCaptor.getValue(), is(equalTo("Invalid shelf number or shelf is empty. Please enter a different shelf number.")));
    }

    @Test(expected = KeyException.class)
    public void shelfInvalidKeyTest() throws KeyException {
        this.vendingMachine.loadShelf("invalidKey", Shelf.of("test", BigDecimal.TEN, 10));
    }

    @Test
    public void insertCoinTest() throws KeyException {
        this.vendingMachine.insertCoin(Coin.FIVE);
        ArgumentCaptor<Coin> coinArgumentCaptor = ArgumentCaptor.forClass(Coin.class);
        verify(this.cashRegister, times(1)).insertCoin(coinArgumentCaptor.capture());
        assertThat(coinArgumentCaptor.getValue(), is(equalTo(Coin.FIVE)));

        this.vendingMachine.loadShelf(this.key, Shelf.of("test", BigDecimal.valueOf(5), 10));
        this.vendingMachine.typeShelfNumber(0);
        when(this.cashRegister.insertCoin(Coin.FIVE)).thenReturn(true);
        this.vendingMachine.insertCoin(Coin.FIVE);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(this.productDispenser, times(1)).ejectProduct(productArgumentCaptor.capture());
        assertThat(productArgumentCaptor.getValue(), is(equalTo(Product.of("test", BigDecimal.valueOf(5)))));
    }
}
