package tdd.vendingMachine;

import lombok.NonNull;
import org.apache.log4j.Logger;
import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.state.*;
import tdd.vendingMachine.util.Constants;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class VendingMachine implements State {

    private static final Logger logger = Logger.getLogger(VendingMachine.class);

    public static final VendingMachineConfiguration VENDING_MACHINE_CONFIGURATION = new VendingMachineConfiguration();

    //States
    private final SoldOutState soldOutState;
    private final NoCreditNoProductSelectedState noCreditNoProductSelectedState;
    private final InsufficientCreditState insufficientCreditState;
    private final NoCreditProductSelectedState noCreditProductSelectedState;
    private final HasCreditNoProductSelectedState hasCreditNoProductSelectedState;

    //states

    private final VendingMachineDisplay display;
    private Shelf<Product> selectedShelf;
    private AtomicInteger credit;
    private Stack<Coin> creditStack;
    private final Map<Integer, Shelf<Product>> productShelves;
    private final Map<Coin, Shelf<Coin>> coinShelves;

    private State currentState;

    public VendingMachine(@NonNull Map<Integer, Shelf<Product>> productShelves, @NonNull Map<Coin, Shelf<Coin>> coinShelves) {
        if(productShelves.size() > VENDING_MACHINE_CONFIGURATION.getShelfCount()) {
            throw new InputMismatchException(String.format("Unable to create Vending Machine configuration mismatch for shelves expected %d given %d ",
                VENDING_MACHINE_CONFIGURATION.getShelfCount(), productShelves.size()));
        }
        this.productShelves = productShelves;
        this.coinShelves = coinShelves;
        this.credit = new AtomicInteger(0);
        this.selectedShelf = null;
        this.display = new VendingMachineDisplay();
        this.creditStack = new Stack<>();

        this.soldOutState = new SoldOutState(this);
        this.noCreditNoProductSelectedState = new NoCreditNoProductSelectedState(this);
        this.noCreditProductSelectedState = new NoCreditProductSelectedState(this);
        this.insufficientCreditState = new InsufficientCreditState(this);
        this.hasCreditNoProductSelectedState = new HasCreditNoProductSelectedState(this);

        if (productShelves.size() == 0) {
            this.currentState = soldOutState;
        } else {
            int availableProducts = productShelves.values().stream()
                .mapToInt(Shelf::getItemCount)
                .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
            this.currentState = availableProducts > 0 ? noCreditNoProductSelectedState : soldOutState;
        }
    }

    /**
     * Throws exception if given shelfNumber is invalid
     * @param shelfNumber the shelf to be checked
     * @throws NoSuchElementException if shelf is not available on the vending machine
     */
    private void validShelfNumber(int shelfNumber) throws NoSuchElementException {
        if (null == productShelves.get(shelfNumber)) {
            throw new NoSuchElementException(
                VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber)
            );
        }
    }

    /**
     * Throws exception is no product has been selected
     * @throws NoSuchElementException when no product was selected
     */
    private void validateSelectedProduct() throws NoSuchElementException {
        if (null == selectedShelf) {
            throw new NoSuchElementException(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.NO_PRODUCT_SELECTED.label));
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public SoldOutState getSoldOutState() {
        return soldOutState;
    }

    public HasCreditNoProductSelectedState getHasCreditNoProductSelectedState() {
        return hasCreditNoProductSelectedState;
    }

    public NoCreditNoProductSelectedState getNoCreditNoProductSelectedState() {
        return noCreditNoProductSelectedState;
    }

    public NoCreditProductSelectedState getNoCreditProductSelectedState() {
        return noCreditProductSelectedState;
    }

    public InsufficientCreditState getInsufficientCreditState() {
        return this.insufficientCreditState;
    }

    public Product getSelectedProduct() {
        return selectedShelf != null ? this.selectedShelf.getType() : null;
    }

    /**
     * Returns the available credit on the vending machine
     * @return int must be greater or equal to zero.
     */
    public int getCredit() {
        return credit.get();
    }

    @Override
    public void insertCoin(Coin money) {
        currentState.insertCoin(money);
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        currentState.selectShelfNumber(shelfNumber);
    }

    @Override
    public void cancel() {
        currentState.cancel();
    }

    /**
     * Displays the product price or pending balance based on product selection
     * @param shelfNumber the selected option on the keypad of the vending machine
     * @throws NoSuchElementException in shelf number is not available
     */
    public final void displayProductPrice(int shelfNumber) throws NoSuchElementException {
        validShelfNumber(shelfNumber);
        Product product = productShelves.get(shelfNumber).getType();
        int toDisplay = this.selectedShelf == null ? product.getPrice() : calculatePendingBalance();
        String message = this.selectedShelf == null ? VendingMachineMessages.PRICE.label : VendingMachineMessages.PENDING.label;
        display.update(String.format("[%s] %s: %s", product.provideType(), message, provideCashToDisplay(toDisplay)));
    }

    public final void showMessageOnDisplay(String message) {
        this.display.update(message);
    }


    /**
     * If there is room in the coin dispenser for the given coin is credited otherwise is sent back to cash bucket
     * @param coin coin to insert
     * @return boolean indicating whether the coin was inserted or not
     */
    public final boolean addCoinToCredit(Coin coin) {
        if (dispenserHasCoinSlotAvailable(coin)) {
            credit.addAndGet(coin.denomination);
            creditStack.push(coin);
            this.display.update(String.format("%s %s: %s", coin.label, VendingMachineMessages.CASH_ACCEPTED_NEW_CREDIT.label, provideCashToDisplay(this.credit.get())));
            return true;
        }
        this.display.update(String.format("%s %s: %s", coin.label, VendingMachineMessages.CASH_NOT_ACCEPTED_DISPENSER_FULL.label, provideCashToDisplay(this.credit.get())));
        return false;
    }

    /**
     * Parses the given value to from cents to units only for display purposes
     * @param cash the amount of cents to present to screen
     * @return a string representing the amount required
     */
    protected static String provideCashToDisplay(int cash) {
        return String.format("%.2f$", cash/100.0);
    }

    /**
     * Drops the current credit stack and sets the credit to zero
     */
    public final void returnAllCreditToBucket() {
        if (creditStack.isEmpty()) {
            display.update(VendingMachineMessages.NO_CREDIT_AVAILABLE.label);
        } else {
            while(!creditStack.isEmpty()) {
                credit.addAndGet(-creditStack.peek().denomination);
                display.update(String.format("[%s] %s: %s", creditStack.pop().label, VendingMachineMessages.RETURN_TO_BUCKET_CREDIT.label, provideCashToDisplay(this.credit.get())));
            }
        }
    }

    /**
     * Given a shelfNumber selects the product
     * @param shelfNumber the number of the shelve to retrieve the product from.
     * @throws NoSuchElementException if shelfNumber is not valid
     */
    public final void selectProductGivenShelfNumber(int shelfNumber)throws NoSuchElementException {
        validShelfNumber(shelfNumber);
        this.selectedShelf = productShelves.get(shelfNumber);
    }

    /**
     * Moves the money from the stack to the vending machine's cash dispenser, the credit
     * remains the same since no cash has been given out.
     * @throws InputMismatchException if unable to provision because no free slots are available
     */
    public final void provisionCreditStackToDispenser() throws InputMismatchException {
        while (!creditStack.isEmpty()) {
            coinShelves.get(creditStack.pop()).provision();
        }
    }

    /**
     * Dispenses product to the pickup bucket
     * @throws NoSuchElementException if no product was previously selected
     */
    public final void dispenseSelectedProductToBucket() throws NoSuchElementException {
        validateSelectedProduct();
        Product product = this.selectedShelf.getType();
        this.selectedShelf.dispense();
        display.update(String.format("[%s] %s", product.getType(), VendingMachineMessages.DISPENSED_TO_BUCKET.label));
    }

    /**
     * Sets the selected product to null
     */
    public final void undoProductSelection() {
        this.selectedShelf = null;
    }

    /**
     * Utility method to evaluate if there is room to store given coin
     * @param coin coin to insert in the machine
     * @return true if room is available for given coin
     */
    protected final boolean dispenserHasCoinSlotAvailable(final Coin coin) {
        long existingCoinsOnCreditStack = creditStack.stream().filter(coin::equals).count();
        return coinShelves.get(coin).countFreeSlots() - existingCoinsOnCreditStack > 0;
    }

    /**
     * Counts the total cash in the cashDispenser
     * @return a int resulting of the sum of every coin.shelf.denomination times amounts of coins of that denomination
     */
    protected final int countCashInDispenser() {
        return coinShelves.entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .mapToInt(nonEmptyEntry -> nonEmptyEntry.getKey().denomination * nonEmptyEntry.getValue().getItemCount())
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
    }

    /**
     * Informs if the credit stack is empty
     * @return boolean true if isEmpty of false otherwise
     */
    public final boolean isCreditStackEmpty() {
        return creditStack.empty();
    }


    /**
     * Provides the credit stack size
     * @return the amount of elements in the credit stack
     */
    public final int getCreditStackSize() {
        return creditStack.size();
    }

    /**
     * Provide the label displayed on the display.
     * @return the las label received by the display
     */
    public String getDisplayCurrentMessage() {
        return display.getCurrentMessage();
    }

    /**
     * Provides the balance for a current selected product according to the existing credit
     * @return the pending balance as (product price) - credit
     * @throws NoSuchElementException if no product was selected
     */
    public int calculatePendingBalance() throws NoSuchElementException{
        validateSelectedProduct();
        return this.selectedShelf.getType().getPrice() - this.credit.get();
    }

    /**
     * Validates if is possible to give change when pending balance < 0 [the sames as if credit is
     * less than selectedProduct.price] based on the existing cash on the coin dispenser.
     * @param changeRequested the requested amount to return from the cash dispenser
     * @return true if possible or false otherwise
     */
    public boolean canGiveChangeFromCashDispenser(int changeRequested) {
        if (changeRequested > 0) return true;
        if (countCashInDispenser() <= Math.abs(changeRequested)) return false;

        Stack<Coin> changeStack = new Stack<>();
        int pending = Math.abs(changeRequested);
        Iterator<Coin> order = Coin.retrieveOrderDescendingIterator();
        while(pending > 0 && order.hasNext()) {
            Coin coin = order.next();
            Shelf<Coin> coinShelf = coinShelves.get(coin);
            while(!coinShelf.isEmpty() && pending >= coin.denomination) {
                coinShelf.dispense();
                changeStack.push(coin);
                pending -= changeStack.peek().denomination;
            }
        }
        while(!changeStack.isEmpty()) { //provision back the cash dispenser with exact amount taken
            coinShelves.get(changeStack.pop()).provision();
        }
        return pending == 0;
    }

    /**
     * Drops the pending balance to the coin dispense bucket if is possible to build the amount from the
     * coins available on the cash dispenser otherwise throws an exception.
     * @throws UnsupportedOperationException if is not possible to build such amount from the cash dispenser.
     */
    public void dispenseCurrentBalance() throws UnsupportedOperationException {
        int balance = calculatePendingBalance();
        if (!canGiveChangeFromCashDispenser(balance)) {
            throw new UnsupportedOperationException("ERR: " + VendingMachineMessages.NOT_ENOUGH_CASH_TO_GIVE_CHANGE.label);
        }
        int pending = Math.abs(balance);
        Iterator<Coin> order = Coin.retrieveOrderDescendingIterator();
        while(pending > 0 && order.hasNext()) {
            Coin coin = order.next();
            Shelf<Coin> coinShelf = coinShelves.get(coin);
            while(!coinShelf.isEmpty() && pending >= coin.denomination) {
                coinShelf.dispense();
                pending -= coin.denomination;
                this.display.update(
                    String.format("[%s] %s: %s", provideCashToDisplay(coin.denomination),
                        VendingMachineMessages.PENDING_BALANCE_RETURNED_TO_BUCKET.label,
                        provideCashToDisplay(-pending))
                );
            }
        }
    }

    /**
     * Returns the amount of products available in the given shelf
     * @param shelfNumber the shelf to check
     * @return the amount of products on the shelf
     * @throws NoSuchElementException if the shelf number is invalid
     */
    public int countProductsOnShelf(int shelfNumber) throws NoSuchElementException{
        validShelfNumber(shelfNumber);
        return productShelves.get(shelfNumber).getItemCount();
    }

    /**
     * Counts the total amount of products on the vending machine.
     * As the sum of every shelf's itemCount.
     * @return integer with the total amount of products.
     */
    public int countTotalAmountProducts() {
        return productShelves.values().stream()
            .mapToInt(Shelf::getItemCount)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
    }
}
