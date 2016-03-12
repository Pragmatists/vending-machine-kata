package tdd.vendingMachine.state;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.Product;
import tdd.vendingMachine.VendingMachine;

import java.util.Optional;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ShelfSelectStateTest implements WithBDDMockito {

    private ShelfSelectState shelfSelectState;

    @Before
    public void setUp() throws Exception {
        shelfSelectState = new ShelfSelectState();
    }

    @Test
    public void should_change_state_after_selecting_product() {
        // given
        VendingMachine vendingMachineMock = mock(VendingMachine.class);
        given(vendingMachineMock.selectProductShelf()).willReturn(1);
        given(vendingMachineMock.getProductInfo(eq(1))).willReturn(Optional.of(Product.DIET_COKE));
        // when
        shelfSelectState.proceed(vendingMachineMock);
        // then
        verify(vendingMachineMock).setState(isA(CoinsInsertState.class));
        verify(vendingMachineMock).proceed();
    }

    @Test
    public void should_stay_in_state_if_selected_product_is_not_available() {
        // given
        VendingMachine vendingMachineMock = mock(VendingMachine.class);
        given(vendingMachineMock.selectProductShelf()).willReturn(1);
        given(vendingMachineMock.getProductInfo(eq(1))).willReturn(Optional.empty());
        // when
        shelfSelectState.proceed(vendingMachineMock);
        // then
        verify(vendingMachineMock, never()).setState(any(VendingMachineState.class));
        verify(vendingMachineMock).proceed();
    }
}
