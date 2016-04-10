package tdd.vendingMachine;

import tdd.vendingMachine.calculator.DefaultVendingMachineCalculator;
import tdd.vendingMachine.display.DefaultVendingMachineDisplay;
import tdd.vendingMachine.domain.Coins;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.inventory.CoinBin;
import tdd.vendingMachine.inventory.Shelfs;

public final class VendingMachineFactory {

    public static VendingMachine create(Coins coins, Product... products) {
        VendingMachine vendingMachine = new VendingMachine(
            new DefaultVendingMachineDisplay(),
            Shelfs.of(products),
            CoinBin.with(coins),
            new DefaultVendingMachineCalculator());
        vendingMachine.currentState = new MachineReadyInitialState(vendingMachine);
        return vendingMachine;
    }

    private VendingMachineFactory() {
    }
}
