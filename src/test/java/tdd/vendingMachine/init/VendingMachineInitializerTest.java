package tdd.vendingMachine.init;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineInitializerTest {
    VendingMachineInitializer vendingMachineInitializer = new VendingMachineInitializer();
    @Mock
    VendingMachine vendingMachine = new VendingMachine();

    @Test
    public void shouldInsertProducts() throws Exception, CannotChangeShelfProductsTypeException {
        List<VendingMachineInitializer.ProductConfiguration> productConfigurations = vendingMachineInitializer.productConfigurations;
        //when
        vendingMachineInitializer.init(vendingMachine);
        //then
        verify(vendingMachine).insertProduct(productConfigurations.get(0).getShelfNumber(), productConfigurations.get(0).getProduct());
        verify(vendingMachine).insertProduct(productConfigurations.get(1).getShelfNumber(), productConfigurations.get(1).getProduct());
    }

    @Test
    public void shouldInsertCoinsToMachine() throws Exception, CannotChangeShelfProductsTypeException {
        //when
        vendingMachineInitializer.init(vendingMachine);
        //then
        int wantedCreatedCoins = VendingMachineInitializer.EACH_COIN_COUNT * vendingMachineInitializer.coinsDenominations.size();
        verify(vendingMachine, times(wantedCreatedCoins)).insertCoinToCashBox(any(Coin.class));
    }

    @Test
    public void shouldExecuteMethodInRightOrder() throws Exception, CannotChangeShelfProductsTypeException {
        //when
        InOrder inOrder = Mockito.inOrder(vendingMachine);
        vendingMachineInitializer.init(vendingMachine);
        //then
        int wantedCreatedCoins = VendingMachineInitializer.EACH_COIN_COUNT * vendingMachineInitializer.coinsDenominations.size();
        inOrder.verify(vendingMachine).turnOnMachineSetUpState();
        inOrder.verify(vendingMachine, times(vendingMachineInitializer.productConfigurations.size())).insertProduct(anyInt(), any(Product.class));
        inOrder.verify(vendingMachine, times(wantedCreatedCoins)).insertCoinToCashBox(any(Coin.class));
        inOrder.verify(vendingMachine).turnOfMachineSetUpState();
    }

}
