package tdd.vendingMachine.state;

import lombok.RequiredArgsConstructor;
import tdd.vendingMachine.Coin;
import tdd.vendingMachine.Product;
import tdd.vendingMachine.VendingMachine;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@RequiredArgsConstructor
public class CoinsInsertState implements VendingMachineState {

    private static final String ABORT = "c";

    private final int selectedShelfNumber;
    private final Product selectedProduct;
    private BigDecimal insertedAmount = BigDecimal.ZERO;

    @Override
    public void proceed(VendingMachine vendingMachine) {
        vendingMachine.display("Selected: %s, insert: %s PLN\n", selectedProduct.getName(),
            selectedProduct.getPrice().subtract(insertedAmount));
        vendingMachine.display("Insert coin (or press 'c' to abort): ");

        String input = vendingMachine.readInput();
        if (ABORT.equals(input) && insertedAmount.compareTo(BigDecimal.ZERO) > 0) {
            vendingMachine.setState(new ReturnMoneyState(insertedAmount)).proceed();
            return;
        } else if (ABORT.equals(input)) {
            vendingMachine.setState(new ProductSelectState()).proceed();
            return;
        }

        Optional<Coin> insertedCoin = Coin.fromDenomination(input);
        if (insertedCoin.isPresent()) {
            insertedAmount = insertedAmount.add(insertedCoin.get().getDenomination());
            vendingMachine.putCoin(insertedCoin.get());
        } else {
            vendingMachine.display("Incorrect input.\nAccepted denominations: 5, 2, 1, 0.5, 0.2, 0.1\n");
            vendingMachine.proceed();
            return;
        }

        if (insertedAmount.compareTo(selectedProduct.getPrice()) == 0) {
            vendingMachine.setState(new ProvideProductState(selectedShelfNumber));
        } else if (insertedAmount.compareTo(selectedProduct.getPrice()) > 0) {
            vendingMachine.setState(new ChangeCheckState(insertedAmount, selectedShelfNumber));
        }

        vendingMachine.proceed();
    }
}
