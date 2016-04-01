package tdd.vendingMachine;

import tdd.vendingMachine.io.StdInputKeyboard;
import tdd.vendingMachine.io.StdOutputDisplay;
import tdd.vendingMachine.state.ProductSelectState;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
class DemoApp {

    public static void main(String[] args) {

        new VendingMachine(10, new StdInputKeyboard(), new StdOutputDisplay())
            .putProductsOnShelf(1, Product.DIET_COKE, 5)
            .putProductsOnShelf(2, Product.DIET_COKE, 2)
            .putProductsOnShelf(3, Product.REDBULL, 5)
            .putProductsOnShelf(4, Product.WATER, 5)
            .putProductsOnShelf(5, Product.WATER, 5)
            .putProductsOnShelf(6, Product.LAYS, 3)
            .putProductsOnShelf(7, Product.LAYS, 2)
            .putProductsOnShelf(8, Product.KITKAT, 2)
            .start(new ProductSelectState());
    }
}
