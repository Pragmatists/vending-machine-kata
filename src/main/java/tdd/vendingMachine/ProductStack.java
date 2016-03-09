package tdd.vendingMachine;

import java.util.Optional;
import java.util.Stack;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class ProductStack extends Stack<Product> {

    public static ProductStack of(Product product, int count) {
        ProductStack productStack = new ProductStack();
        for (int i = 0; i < count; i++) {
            productStack.push(product);
        }
        return productStack;
    }

    public Optional<Product> popOptional() {
        return isEmpty() ? Optional.empty() : Optional.of(pop());
    }

    private ProductStack() {

    }
}
