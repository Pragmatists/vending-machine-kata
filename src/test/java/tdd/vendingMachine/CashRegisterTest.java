package tdd.vendingMachine;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import tdd.vendingMachine.dto.Coin;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class CashRegisterTest {

    private CashRegister cashRegister;
    private Display display;
    private ChangeDispenser changeDispenser;

    @Before
    public void setup() {
        AbstractModule abstractModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(Display.class).toInstance(mock(DisplayImpl.class));
                bind(CashRegister.class).to(CashRegisterImpl.class);
                bind(ChangeDispenser.class).toInstance(mock(ChangeDispenserImpl.class));
            }
        };
        Injector injector = Guice.createInjector(abstractModule);
        this.cashRegister = injector.getInstance(CashRegister.class);
        this.display = injector.getInstance(Display.class);
        this.changeDispenser = injector.getInstance(ChangeDispenser.class);
    }

    @Test
    public void emptyCashRegisterTest() {
        assertThat(this.cashRegister.insertCoin(Coin.FIVE), is(false));

        ArgumentCaptor<String> warningArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(this.display, times(1)).writeWarning(warningArgumentCaptor.capture());
        assertThat(warningArgumentCaptor.getValue(), is(equalTo("Please choose product.")));

        ArgumentCaptor<Coin> coinArgumentCaptor = ArgumentCaptor.forClass(Coin.class);
        verify(this.changeDispenser, times(1)).ejectChange(coinArgumentCaptor.capture());
        assertThat(coinArgumentCaptor.getValue(), is(equalTo(Coin.FIVE)));
    }

    @Test
    public void resetCashRegisterTest() {
        this.cashRegister.setProductPrice(BigDecimal.TEN);
        this.cashRegister.insertCoin(Coin.FIVE);
        this.cashRegister.reset();

        ArgumentCaptor<List> coinListArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.changeDispenser, times(1)).ejectChange(coinListArgumentCaptor.capture());
        assertThat(coinListArgumentCaptor.getValue(), is(equalTo(Arrays.asList(Coin.FIVE))));
        verify(this.display, times(2)).reset();
    }

    @Test
    public void filledCashRegisterTest() {
        this.cashRegister.setProductPrice(BigDecimal.TEN);

        ArgumentCaptor<BigDecimal> productPriceArgumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        verify(this.display, times(1)).reset();
        verify(this.display, times(1)).writeProductPrice(productPriceArgumentCaptor.capture());
        assertThat(productPriceArgumentCaptor.getValue(), is(equalTo(BigDecimal.TEN)));

        assertThat(this.cashRegister.insertCoin(Coin.FIVE), is(false));

        verify(this.display, times(2)).writeProductPrice(productPriceArgumentCaptor.capture());
        assertThat(productPriceArgumentCaptor.getValue(), is(equalTo(BigDecimal.valueOf(5))));

        assertThat(this.cashRegister.insertCoin(Coin.FIVE), is(true));
    }

    @Test
    public void filledCashRegisterMultipleChangeTest() {
        this.cashRegister.setProductPrice(BigDecimal.TEN);
        this.cashRegister.insertCoin(Coin.FIVE);
        this.cashRegister.insertCoin(Coin.TWO);
        this.cashRegister.insertCoin(Coin.ONE);
        this.cashRegister.insertCoin(Coin.ONE);

        assertThat(this.cashRegister.insertCoin(Coin.TWO), is(true));

        ArgumentCaptor<List> coinListArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.changeDispenser, times(1)).ejectChange(coinListArgumentCaptor.capture());
        assertThat(coinListArgumentCaptor.getValue(), is(equalTo(Arrays.asList(Coin.ONE))));
        verify(this.display, times(2)).reset();

        this.cashRegister.setProductPrice(BigDecimal.valueOf(7));
        this.cashRegister.insertCoin(Coin.FIVE);
        assertThat(this.cashRegister.insertCoin(Coin.FIVE), is(true));
        verify(this.changeDispenser, times(2)).ejectChange(coinListArgumentCaptor.capture());
        assertThat(coinListArgumentCaptor.getValue(), is(equalTo(Arrays.asList(Coin.TWO, Coin.ONE))));
        verify(this.display, times(4)).reset();
    }

    @Test
    public void filledCashRegisterMultipleChange2Test() {
        this.cashRegister.setProductPrice(BigDecimal.valueOf(5));
        this.cashRegister.insertCoin(Coin.ONE);
        this.cashRegister.insertCoin(Coin.ONE);
        this.cashRegister.insertCoin(Coin.ONE);
        this.cashRegister.insertCoin(Coin.ONE);
        this.cashRegister.insertCoin(Coin.ONE);

        this.cashRegister.setProductPrice(BigDecimal.valueOf(3));
        this.cashRegister.insertCoin(Coin.TWO);
        assertThat(this.cashRegister.insertCoin(Coin.FIVE), is(true));
        ArgumentCaptor<List> coinListArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.changeDispenser, times(1)).ejectChange(coinListArgumentCaptor.capture());
        assertThat(coinListArgumentCaptor.getValue(), is(equalTo(Arrays.asList(Coin.TWO, Coin.ONE, Coin.ONE))));
        verify(this.display, times(4)).reset();
    }

    @Test
    public void filledCashRegisterNotEnoughChangeTest() {
        this.cashRegister.setProductPrice(BigDecimal.TEN);
        this.cashRegister.insertCoin(Coin.FIVE);
        this.cashRegister.insertCoin(Coin.TWO);
        this.cashRegister.insertCoin(Coin.TWO);

        assertThat(this.cashRegister.insertCoin(Coin.TWO), is(false));

        ArgumentCaptor<List> coinListArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.changeDispenser, times(1)).ejectChange(coinListArgumentCaptor.capture());
        assertThat(coinListArgumentCaptor.getValue(), is(equalTo(Arrays.asList(Coin.FIVE, Coin.TWO, Coin.TWO, Coin.TWO))));

        ArgumentCaptor<String> warningArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(this.display, times(1)).writeWarning(warningArgumentCaptor.capture());
        assertThat(warningArgumentCaptor.getValue(), is(equalTo("The change can not be returned. Please take the change and enter new shelf number.")));
    }
}
