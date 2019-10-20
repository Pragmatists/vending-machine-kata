package tdd.vendingMachine.strategy;


import org.junit.Test;
import tdd.vendingMachine.VendingMachineState;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class VendingMachineStateStrategiesTest {

    @Test
    public void shouldHasOneStrategyForEachMachineStateType() throws Exception {
        //given
        VendingMachineState[] machineStates = VendingMachineState.values();

        //when
        VendingMachineStateStrategies vendingMachineStateStrategies = new VendingMachineStateStrategies();

        //then
        assertThat(vendingMachineStateStrategies.size()).isEqualTo(machineStates.length);
        assertThat(vendingMachineStateStrategies.get(VendingMachineState.WAITING_FOR_SELECT_PRODUCT)).isInstanceOf(WaitingForSelectProductStrategy.class);
        assertThat(vendingMachineStateStrategies.get(VendingMachineState.PRODUCT_SELECTED)).isInstanceOf(ProductSelectedStrategy.class);
        assertThat(vendingMachineStateStrategies.get(VendingMachineState.SET_UP_MACHINE)).isInstanceOf(SetUpVendingMachineStrategy.class);
    }

}
