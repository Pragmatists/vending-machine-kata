package tdd.vendingMachine.state;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tdd.vendingMachine.Product;
import tdd.vendingMachine.VendingMachine;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@RunWith(JUnitParamsRunner.class)
public class CoinsInsertStateTest implements WithBDDMockito {

    private VendingMachine vendingMachineMock;
    private CoinsInsertState coinsInsertState;

    @Before
    public void setUp() throws Exception {
        vendingMachineMock = mock(VendingMachine.class);
        coinsInsertState = new CoinsInsertState(Product.DIET_COKE);
    }

    @Test
    public void should_return_to_shelf_select_state_when_abort() throws Exception {
        // given
        given(vendingMachineMock.readInput()).willReturn("c");
        given(vendingMachineMock.setState(isA(ProductSelectState.class))).willReturn(vendingMachineMock);
        // expect
        coinsInsertState.proceed(vendingMachineMock);
    }

    @Test
    @Parameters({"asd", "3", "0\\,5", "0.23", "-1"})
    @TestCaseName("\"{0}\" should be incorrect")
    public void should_stay_in_state_when_incorrect_input(String input) throws Exception {
        // given
        given(vendingMachineMock.readInput()).willReturn(input);
        // when
        coinsInsertState.proceed(vendingMachineMock);
        // then
        verify(vendingMachineMock, never()).setState(any(VendingMachineState.class));
        verify(vendingMachineMock).proceed();
    }
}
