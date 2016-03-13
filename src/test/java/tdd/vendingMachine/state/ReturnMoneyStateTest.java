package tdd.vendingMachine.state;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import java.math.BigDecimal;
import org.junit.Test;
import tdd.vendingMachine.VendingMachine;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ReturnMoneyStateTest implements WithBDDMockito {

    @Test
    public void should_remove_money_to_return_from_machine_wallet() {
        // given
        BigDecimal amountToReturn = new BigDecimal("1.50");
        // setup
        VendingMachine vendingMachineMock = mock(VendingMachine.class);
        given(vendingMachineMock.setState(isA(ProductSelectState.class))).willReturn(vendingMachineMock);
        VendingMachineState vendingMachineState = new ReturnMoneyState(amountToReturn);
        // when
        vendingMachineState.proceed(vendingMachineMock);
        // then
        verify(vendingMachineMock).removeValueInCoins(eq(amountToReturn));
    }
}
