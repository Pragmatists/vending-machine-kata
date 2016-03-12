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
public class ProductSelectStateTest implements WithBDDMockito {

    private ProductSelectState productSelectState;

    @Before
    public void setUp() throws Exception {
        productSelectState = new ProductSelectState();
    }

    @Test
    public void should_change_state_after_selecting_product() {
        // given
        VendingMachine vendingMachineMock = mock(VendingMachine.class);
        given(vendingMachineMock.selectProductShelf()).willReturn(1);
        given(vendingMachineMock.getProductInfo(eq(1))).willReturn(Optional.of(Product.DIET_COKE));
        given(vendingMachineMock.setState(isA(CoinsInsertState.class))).willReturn(vendingMachineMock);
        // when
        productSelectState.proceed(vendingMachineMock);
        // then
        verify(vendingMachineMock).proceed();
    }
}
