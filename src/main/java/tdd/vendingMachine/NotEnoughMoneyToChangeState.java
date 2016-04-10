package tdd.vendingMachine;

import com.google.common.base.Optional;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.ProductWithChange;

import static tdd.vendingMachine.VendingMachineStateFactory.machineReadyInitialState;

public class NotEnoughMoneyToChangeState extends VendingMachineState {

    private static final String INSUFFICIENT_NUMBER_OF_COINS_TO_CHANGE_MESSAGE = "Brakuje monet do wydania reszty.";

    protected NotEnoughMoneyToChangeState(VendingMachine vendingMachine) {
        super(vendingMachine);
        vendingMachine.display.displayText(INSUFFICIENT_NUMBER_OF_COINS_TO_CHANGE_MESSAGE);
    }

    @Override
    protected ProductWithChange dispense() {
        ProductWithChange productWithChange = new ProductWithChange(Optional.<Product>absent(), vendingMachine.credit);
        vendingMachine.currentState = machineReadyInitialState(vendingMachine);
        return productWithChange;
    }
}
