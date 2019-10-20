package tdd.vendingMachine.strategy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.cash.register.ICashBox;
import tdd.vendingMachine.display.IDisplay;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.ProductType;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;
import tdd.vendingMachine.shelf.IShelf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class SetUpVendingMachineStrategyTest {

    private SetUpVendingMachineStrategy strategy = new SetUpVendingMachineStrategy();

    @Mock
    IDisplay display;
    @Mock
    ICashBox cashBox;
    @Mock
    IShelf shelf;

    @Test
    public void shouldInsertCoinToCashBox() throws Exception {
        //given
        Coin coin = new Coin(5.0);
        //when
        strategy.insertCoinToCashBox(display, cashBox, coin);
        //then
        verify(cashBox).addToCashBoxPocket(coin);
        verify(display).showCoinAddedToCashBoxMessage(coin);
    }

    @Test(expected = CannotChangeShelfProductsTypeException.class)
    public void shouldNotInsertProductWhenShelfTypeIsMismatch() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        Product chips = new Product(ProductType.CHIPS);
        doThrow(new CannotChangeShelfProductsTypeException()).when(shelf).setProductsType(ProductType.CHIPS);
        //when
        strategy.insertProduct(display, shelf, chips);
        //then

    }

    @Test
    public void shouldInsertProduct() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        Product product = new Product(ProductType.CHIPS);
        when(shelf.getNumber()).thenReturn(shelfNumber);
        //when
        strategy.insertProduct(display, shelf, product);
        //then
        verify(shelf).push(product);
        verify(display).showInsertProductInformationMessage(shelfNumber, product);
    }

    @Test
    public void shouldNotInsertProductWhenShelfIsNull() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        Product product = new Product(ProductType.CHIPS);
        //when
        strategy.insertProduct(display, null, product);
        //then
        verify(display).showInvalidShelfNumberMessage();
    }

    @Test
    public void shouldReturnFalseWhenWantToInsertCoinToCurrentRequest() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        //when
        boolean isCoinInserted = strategy.insertCoinForCurrentRequest(display, cashBox, new Coin(5.0));
        //then
        assertThat(isCoinInserted).isFalse();
    }
}
