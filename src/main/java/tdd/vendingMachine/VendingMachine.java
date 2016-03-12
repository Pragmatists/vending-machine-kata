package tdd.vendingMachine;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import tdd.vendingMachine.io.Display;
import tdd.vendingMachine.io.Keyboard;
import tdd.vendingMachine.state.VendingMachineState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Accessors(chain = true)
@RequiredArgsConstructor
public class VendingMachine {

    private final int shelfCount;

    @Delegate
    private final Keyboard keyboard;

    @Delegate
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
            throw new IllegalArgumentException("Incorrect shelf number: " + shelfNumber);
        }

        productMap.put(shelfNumber, ProductStack.of(product, count));
        return this;
    }

    public Optional<Product> getSelectedProduct() {
        final int selectedShelfNumber = readNumber();
        if (correctShelfNumber(selectedShelfNumber) && productMap.containsKey(selectedShelfNumber)) {
            return productMap.get(selectedShelfNumber).popOptional();
        }

        return Optional.empty();
    }

    public void displayProducts() {
        for (int i = 1; i <= shelfCount; i++) {
            if (productMap.containsKey(i)) {
                ProductStack productStack = productMap.get(i);
                display("%s -> %s (%s PLN) x%s\n", i, productStack.getName(), productStack.getPrice(), productStack.size());
            } else {
                display("%s -> empty\n", i);
            }
        }
    }

    public void display(String messageTemplate, Object... args) {
        display(String.format(messageTemplate, args));
    }

    private boolean correctShelfNumber(int number) {
        return number >= 1 && number <= shelfCount;
    }
}
