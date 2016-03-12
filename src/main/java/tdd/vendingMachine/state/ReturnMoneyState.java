package tdd.vendingMachine.state;

import lombok.RequiredArgsConstructor;
import tdd.vendingMachine.VendingMachine;

import java.math.BigDecimal;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>.
 */
@RequiredArgsConstructor
public class ReturnMoneyState implements VendingMachineState {

    private final BigDecimal returnAmount;

    @Override
    public void proceed(VendingMachine vendingMachine) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
