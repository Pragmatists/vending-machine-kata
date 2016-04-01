package tdd.vendingMachine.state;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import tdd.vendingMachine.Product;
import tdd.vendingMachine.VendingMachine;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>.
 */
@RequiredArgsConstructor
public class ChangeCheckState implements VendingMachineState {

    private final BigDecimal insertedAmount;
    private final int selectedShelfNumber;

    @Override
    public void proceed(VendingMachine vendingMachine) {
        Product selectedProduct = vendingMachine.getProductInfo(selectedShelfNumber)
            .orElseThrow(() -> new IllegalStateException("Product not found on shelf number " + selectedShelfNumber));

        BigDecimal changeValue = insertedAmount.subtract(selectedProduct.getPrice());
        if (vendingMachine.calculator().hasCoinsForValue(changeValue)) {
            vendingMachine.removeValueInCoins(changeValue);
            vendingMachine.display("Please take your change %s PLN\n", changeValue);
            vendingMachine.setState(new ProvideProductState(selectedShelfNumber)).proceed();
        } else {
            vendingMachine.display("Not enough coins for change\n");
            vendingMachine.setState(new ReturnMoneyState(insertedAmount)).proceed();
        }
    }
}
