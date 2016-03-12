package tdd.vendingMachine.state;

import lombok.RequiredArgsConstructor;
import tdd.vendingMachine.VendingMachine;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>.
 */
@RequiredArgsConstructor
public class ProvideProductState implements VendingMachineState {

    private final int selectedShelfNumber;

    @Override
    public void proceed(VendingMachine vendingMachine) {
        vendingMachine.popProduct(selectedShelfNumber);
        vendingMachine.display("Take your product, thank you for purchase\n");
        vendingMachine.setState(new ProductSelectState()).proceed();
    }
}
