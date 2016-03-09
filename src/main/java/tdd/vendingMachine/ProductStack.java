package tdd.vendingMachine;

import java.util.Optional;
import java.util.Stack;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductStack extends Stack<Product> {

    public static ProductStack of(Product product, int count) {
        ProductStack productStack = new ProductStack(product);
        for (int i = 0; i < count; i++) {
            productStack.push(product);
        }
        return productStack;
    }

    @Delegate
    private final Product product;

    public Optional<Product> popOptional() {
        return isEmpty() ? Optional.empty() : Optional.of(pop());
    }
}
