package tdd.vendingMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tdd.vendingMachine.state.VendingMachineState;

@Accessors(chain = true)
@RequiredArgsConstructor
public class VendingMachine {

    private final int shelfCount;
    private final Keyboard keyboard;

    private Map<Integer, ProductStack> productMap = new HashMap<>();

    @Setter
    private VendingMachineState state;

    public void start(VendingMachineState state) {
        setState(state).proceed();
    }

    public void proceed() {
        state.proceed(this);
    }

    public VendingMachine putProductsOnShelf(int shelfNumber, Product product, int count) {
        if (shelfNumber < 1 || shelfNumber > shelfCount) {
            throw new IllegalArgumentException("Illegal shelf number: " + shelfNumber);
        }

        productMap.put(shelfNumber, ProductStack.of(product, count));
        return this;
    }

    public Optional<Product> getSelectedProduct() {
        final int selectedShelfNumber = keyboard.readNumber();
        if (correctShelfNumber(selectedShelfNumber) && productMap.containsKey(selectedShelfNumber)) {
            return productMap.get(selectedShelfNumber).popOptional();
        }

        return Optional.empty();
    }

    private boolean correctShelfNumber(int number) {
        return number >= 1 && number <= shelfCount;
    }
}
