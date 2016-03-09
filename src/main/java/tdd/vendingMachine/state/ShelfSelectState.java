package tdd.vendingMachine.state;

import tdd.vendingMachine.VendingMachine;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ShelfSelectState implements VendingMachineState {

    @Override
    public void proceed(VendingMachine vendingMachine) {
        vendingMachine.displayProducts();
        vendingMachine.display("Select product: ");

        vendingMachine.getSelectedProduct()
            .ifPresent(p -> vendingMachine.setState(m -> System.out.println("selected: " + p.getName())));

        vendingMachine.proceed();
    }
}
