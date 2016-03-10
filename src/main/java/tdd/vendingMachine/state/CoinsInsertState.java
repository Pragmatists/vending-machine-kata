package tdd.vendingMachine.state;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import tdd.vendingMachine.Coin;
import tdd.vendingMachine.Product;
import tdd.vendingMachine.VendingMachine;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@RequiredArgsConstructor
public class CoinsInsertState implements VendingMachineState {

    private static final String ABORT = "c";

    private final Product selectedProduct;

    @Override
    public void proceed(VendingMachine vendingMachine) {
        vendingMachine.display("Selected: %s, insert: %s PLN\n", selectedProduct.getName(), selectedProduct.getPrice());
        vendingMachine.display("Insert coin (or press 'c' to abort): ");

        String input = vendingMachine.readInput();
        if (ABORT.equals(input)) {
            vendingMachine.setState(new ShelfSelectState()).proceed();
        }

        Optional<Coin> insertedCoin = Coin.fromDenomination(input);
        if (insertedCoin.isPresent()) {
            throw new UnsupportedOperationException("Not implemented yet!");
        } else {
            vendingMachine.display("Incorrect input.\nAccepted denominations: 5, 2, 1, 0.5, 0.2, 0.1\n");
        }

        vendingMachine.proceed();
    }
}
