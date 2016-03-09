package tdd.vendingMachine;

import tdd.vendingMachine.io.StdInputKeyboard;
import tdd.vendingMachine.io.StdOutputDisplay;
import tdd.vendingMachine.state.ShelfSelectState;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
class Application {

    public static void main(String[] args) {
        new VendingMachine(5, new StdInputKeyboard(), new StdOutputDisplay())
            .putProductsOnShelf(1, Product.DIET_COKE, 5)
            .putProductsOnShelf(3, Product.KITKAT, 2)
            .start(new ShelfSelectState());
    }
}
