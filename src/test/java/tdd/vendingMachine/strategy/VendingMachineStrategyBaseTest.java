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
import tdd.vendingMachine.shelf.Shelf;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineStrategyBaseTest {

    private VendingMachineStrategyBaseClass vendingStrategyBase = new VendingMachineStrategyBaseClass();

    @Mock
    IDisplay display;
    @Mock
    ICashBox cashBox;
    @Mock
    Shelf shelf;

    @Test
    public void shouldDisplayInvalidActionMessageForBaseMethodSelectProduct() throws Exception {
        //given
        //when
        vendingStrategyBase.selectProduct(display, shelf);
        //then
        verify(display).showInvalidActionForMachineStateMessage();
    }

    @Test
    public void shouldDisplayInvalidActionMessageForBaseCancelRequestMethod() throws Exception {
        //given
        //when
        vendingStrategyBase.cancelRequest(display, cashBox);
        //then
        verify(display).showInvalidActionForMachineStateMessage();
    }

    @Test
    public void shouldDisplayInvalidActionMessageForBaseInsertProductMethod() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        Product product = new Product(ProductType.CHIPS);
        //when
        vendingStrategyBase.insertProduct(display, shelf, product);
        //then
        verify(display).showInvalidActionForMachineStateMessage();
    }

    @Test
    public void shouldDisplayInvalidActionMessageInsertCoinToCashBoxMethod() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        //when
        vendingStrategyBase.insertCoinToCashBox(display, cashBox, new Coin(5.0));
        //then
        verify(display).showInvalidActionForMachineStateMessage();
    }

    private class VendingMachineStrategyBaseClass extends VendingMachineStrategyBase {

        @Override
        public boolean insertCoinForCurrentRequest(IDisplay display, ICashBox cashBox, Coin coin) {
            return false;
        }
    }
}
