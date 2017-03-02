package tdd.vendingMachine;

import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.exception.CashDispenserFullException;
import tdd.vendingMachine.domain.exception.ShelfEmptyNotAvailableForSelectionException;
import tdd.vendingMachine.state.State;
import tdd.vendingMachine.state.StateEnum;

import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 3/2/2017.
 * @since 2.1
 * Interface representing the basic methods to provide by a vending machine.
 */
public interface VendingMachine {

    /**
     * If there is room in the coin dispenser for the given coin is credited otherwise throws exception
     * @param coin coin to insert
     * @throws CashDispenserFullException if shelf coin shelf for the given coin on dispenser is full
     */
    void addCoinToCredit(Coin coin) throws CashDispenserFullException;

    /**
     * Should inform the current state of the vending machine
     * @return State class representing the given state
     */
    State provideCurrentState();

    /**
     * Shows the given message on the vending machine's display
     * @param message the message to display
     */
    void showMessageOnDisplay(String message);

    /**
     * Displays the product price or pending balance based on product selection
     * @param shelfNumber the selected option on the keypad of the vending machine
     * @throws NoSuchElementException in shelf number is not available
     */
    void displayProductPrice(int shelfNumber);

    /**
     * Returns the current product referenced by the selected shelf number containing the product.
     * @return the referenced product or null
     */
    Product provideSelectedProduct();

    /**
     * Returns the available credit on the vending machine
     * @return int must be greater or equal to zero.
     */
    int provideCredit();

    /**
     * Sets the selected product to null
     */
    void undoProductSelection();

    /**
     * Given a shelfNumber selects the product
     * @param shelfNumber the number of the shelve to retrieve the product from.
     * @throws NoSuchElementException if the shelf number is invalid
     * @throws ShelfEmptyNotAvailableForSelectionException if the requested shelf is empty
     */
    void selectProductGivenShelfNumber(int shelfNumber) throws NoSuchElementException, ShelfEmptyNotAvailableForSelectionException;


    /**
     * Informs if the credit stack is empty
     * @return boolean true if isEmpty of false otherwise
     */
    boolean isCreditStackEmpty();

    /**
     * Evaluates if there are products in the vending machine
     * @return true if all product shelves are empty false otherwise
     */
    boolean isSoldOut();

    /**
     * Drops the current credit stack and sets the credit to zero
     */
    void returnAllCreditToBucket();

    /**
     * Provides the credit stack size
     * @return the amount of elements in the credit stack
     */
    int getCreditStackSize();

    /**
     * Provide the label displayed on the display.
     * @return the las label received by the display
     */
    String getDisplayCurrentMessage();

    /**
     * Provides the balance for a current selected product according to the existing credit
     * @return the pending balance as (product price) - credit
     * @throws NoSuchElementException if no product was selected
     */
    int calculatePendingBalance() throws NoSuchElementException;

    /**
     * Counts the total amount of products on the vending machine.
     * As the sum of every shelf's itemCount.
     * @return integer with the total amount of products.
     */
    int countTotalAmountProducts();

    /**
     * Returns the amount of products available in the given shelf
     * @param shelfNumber the shelf to check
     * @return the amount of products on the shelf
     * @throws NoSuchElementException if the shelf number is invalid
     */
    int countProductsOnShelf(int shelfNumber) throws NoSuchElementException;

    /**
     * Attempts to change state of the machine to the given state
     */
    void sendStateTo(StateEnum state);
}
