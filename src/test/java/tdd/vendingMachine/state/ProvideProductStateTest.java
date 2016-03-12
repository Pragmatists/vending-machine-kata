package tdd.vendingMachine.state;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.junit.Test;
import tdd.vendingMachine.VendingMachine;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ProvideProductStateTest implements WithBDDMockito {
    @Test
    public void should_pop_product_and_change_to_shelf_select_state() {
        // given
        VendingMachine vendingMachineMock = mock(VendingMachine.class);
        given(vendingMachineMock.setState(isA(ProductSelectState.class))).willReturn(vendingMachineMock);
        VendingMachineState vendingMachineState = new ProvideProductState(1);
        // when
        vendingMachineState.proceed(vendingMachineMock);
        // then
        verify(vendingMachineMock).popProduct(1);
    }
}
