package tdd.vendingMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tdd.vendingMachine.io.Display;
import tdd.vendingMachine.io.Keyboard;
import tdd.vendingMachine.state.VendingMachineState;

@Accessors(chain = true)
@RequiredArgsConstructor
public class VendingMachine {

    private final int shelfCount;

    private final Keyboard keyboard;
    private final Display display;

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
        if (!correctShelfNumber(shelfNumber)) {
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

    public void displayProducts() {
        for (int i = 1; i <= shelfCount; i++) {
            if (productMap.containsKey(i)) {
                ProductStack productStack = productMap.get(i);
                display("%s -> %s (%s PLN)\n", i, productStack.getName(), productStack.getPrice());
            } else {
                display("%s -> empty\n", i);
            }
        }
    }

    public void display(String message, Object... args) {
        display.display(message, args);
    }

    public String readInput() {
        return keyboard.readInput();
    }

    private boolean correctShelfNumber(int number) {
        return number >= 1 && number <= shelfCount;
    }
}
