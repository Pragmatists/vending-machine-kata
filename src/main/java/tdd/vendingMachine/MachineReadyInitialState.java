package tdd.vendingMachine;

import tdd.vendingMachine.domain.Coins;

import static tdd.vendingMachine.VendingMachineStateFactory.productSelectedState;

public class MachineReadyInitialState extends VendingMachineState {

    private static final String CHOOSE_A_PRODUCT_MESSAGE = "Wybierz produkt...";

    public MachineReadyInitialState(VendingMachine vendingMachine) {
        super(vendingMachine);
        this.vendingMachine.credit = Coins.empty();
        this.vendingMachine.display.displayText(CHOOSE_A_PRODUCT_MESSAGE);
    }

    @Override
    public void selectShelf(int shelfNumber) {
        super.selectShelf(shelfNumber);
        checkIfValidShelfWasSelected();
    }

    private void checkIfValidShelfWasSelected() {
        if (vendingMachine.shelfs.isShelfWasSelected()) {
            vendingMachine.currentState = productSelectedState(vendingMachine);
        }
    }

}
