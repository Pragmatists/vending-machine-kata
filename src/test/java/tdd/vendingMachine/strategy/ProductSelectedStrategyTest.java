package tdd.vendingMachine.strategy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.cash.register.ICashBox;
import tdd.vendingMachine.display.IDisplay;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;
import tdd.vendingMachine.shelf.IShelf;

import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductSelectedStrategyTest {

    private ProductSelectedStrategy strategy = new ProductSelectedStrategy();

    @Mock
    IDisplay display;
    @Mock
    IShelf shelf;
    @Mock
    ICashBox cashBox;

    @Test
    public void shouldNeverInsertCoinToCashBox() throws Exception {
        //given

        Coin coin = new Coin(5.0);
        //when
        strategy.insertCoinToCashBox(display, cashBox, coin);
        //then
        verify(cashBox, never()).addToCashBoxPocket(coin);
        verify(display).showInvalidActionForMachineStateMessage();
    }

    @Test
    public void shouldSuccessfulInsertCoinToCurrentRequestPocketWhenCoinIsValid() throws Exception {
        //given
        Coin coin = new Coin(5.0);
        when(cashBox.isValidCoin(coin)).thenReturn(true);
        //when
        boolean result = strategy.insertCoinForCurrentRequest(display, cashBox, coin);
        //then
        verify(cashBox).addToCurrentRequestPocket(coin);
        assertThat(result).isTrue();
    }

    @Test
    public void shouldNotInsertCoinToCurrentRequestPocketWhenCoinIsInvalid() throws Exception {
        //given
        Coin coin = new Coin(5.0);
        when(cashBox.isValidCoin(coin)).thenReturn(false);
        //when
        boolean result = strategy.insertCoinForCurrentRequest(display, cashBox, coin);
        //then
        verify(cashBox, never()).addToCurrentRequestPocket(coin);
        verify(display).showInvalidCoinFormatMessage();
        assertThat(result).isFalse();
    }

    @Test
    public void shouldNotInsertProductOnOtherThanSetupMachineState() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        Product product = mock(Product.class);
        //when
        strategy.insertProduct(display, shelf, product);
        //then
        verify(shelf, never()).push(product);
        verify(display).showInvalidActionForMachineStateMessage();
    }

    @Test()
    public void shouldReturnInsertedCoins() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        Coin firstCoin = new Coin(5.0);
        Coin secondCoin = new Coin(2.0);
        Stack<Coin> coins = new Stack();
        coins.push(firstCoin);
        coins.push(secondCoin);
        when(cashBox.getCurrentRequestPocket()).thenReturn(coins);
        //when
        strategy.cancelRequest(display, cashBox);
        //then
        verify(display).showReturnCoinMessage(firstCoin);
        verify(display).showReturnCoinMessage(secondCoin);
        verify(display).showRequestCanceledMessage();
    }

    @Test
    public void shouldDisplayMessageAboutTheCoinIsInvalid() throws Exception {
        //given
        Coin coin = new Coin(0.05);
        when(cashBox.isValidCoin(coin)).thenReturn(false);
        //when
        strategy.insertCoinForCurrentRequest(display, cashBox, coin);
        //then
        verify(display).showInvalidCoinFormatMessage();
    }

}
