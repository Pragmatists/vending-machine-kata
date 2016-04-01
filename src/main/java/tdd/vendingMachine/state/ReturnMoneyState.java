package tdd.vendingMachine.state;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import tdd.vendingMachine.VendingMachine;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>.
 */
@RequiredArgsConstructor
public class ReturnMoneyState implements VendingMachineState {

    private final BigDecimal returnAmount;

    @Override
    public void proceed(VendingMachine vendingMachine) {
        vendingMachine.removeValueInCoins(returnAmount);
        vendingMachine.display("Please take back your money, come back soon\n");
        vendingMachine.setState(new ProductSelectState()).proceed();
    }
}
