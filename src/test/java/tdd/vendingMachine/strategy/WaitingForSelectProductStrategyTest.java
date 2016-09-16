package tdd.vendingMachine.strategy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.cash.register.CashBox;
import tdd.vendingMachine.display.IDisplay;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.ProductType;
import tdd.vendingMachine.request.Request;
import tdd.vendingMachine.shelf.IShelf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WaitingForSelectProductStrategyTest {

    private IVendingMachineStrategy strategy = new WaitingForSelectProductStrategy();

    @Mock
    IDisplay display;
    @Mock
    CashBox cashBox;
    @Mock
    IShelf shelf;

    @Test
    public void shouldCreateRequestIfChosenShelfIsNotEmpty() throws Exception {
        //given
        Product sampleProduct = new Product(ProductType.CHIPS);
        int shelfNumber = 5;
        when(shelf.getNumber()).thenReturn(shelfNumber);
        when(shelf.isEmpty()).thenReturn(false);
        when(shelf.pop()).thenReturn(sampleProduct);
        //when
        Request request = strategy.selectProduct(display, shelf);
        //then
        assertThat(request.getShelfNumber()).isEqualTo(shelfNumber);
        assertThat(request.getProduct()).isSameAs(sampleProduct);
        verify(display).showProductSelectedMessage(sampleProduct);
    }

    @Test
    public void shouldDisplayInvalidShelfChooseMessageWhenShelfIsNull() throws Exception {
        //given

        //when
        Request request = strategy.selectProduct(display, null);
        //then
        assertThat(request).isNull();
        verify(display).showIncorrectProductSelectMessage();
    }

    @Test
    public void shouldDisplayFirstSelectProductMessageOnInsertCoin() throws Exception {
        //given

        //when
        strategy.insertCoinForCurrentRequest(display, cashBox, new Coin(5.0));
        //then
        verify(display).showFirstSelectProductMessage();
    }

    @Test
    public void shouldDisplayFirstSelectProductMessageOnCancelRequest() throws Exception {
        //given

        //when
        strategy.cancelRequest(display, cashBox);
        //then
        verify(display).showFirstSelectProductMessage();
    }

}
