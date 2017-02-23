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
    private Product selectedProduct;
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
        this.selectedProduct = null;
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
        if (null == selectedProduct) {
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
        return selectedProduct;
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
        int toDisplay = this.selectedProduct == null ? product.getPrice() : calculatePendingBalance();
        String message = this.selectedProduct == null ? VendingMachineMessages.PRICE.label : VendingMachineMessages.PENDING.label;
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
        selectedProduct = productShelves.get(shelfNumber).getType();
    }

    /**
     * Moves the money from the stack to the vending machine's cash dispenser and clears the credit
     * @throws InputMismatchException if unable to provision because no free slots are available
     */
    public final void provisionCreditStackToDispenser() throws InputMismatchException {
        while (!creditStack.isEmpty()) {
            Coin pop = creditStack.pop();
            coinShelves.get(pop).provision();
            credit.addAndGet(-pop.denomination);
        }
    }

    /**
     * Dispenses product to the pickup bucket
     * @throws NoSuchElementException if no product was previously selected
     */
    public final void dispenseSelectedProductToBucket() throws NoSuchElementException {
        validateSelectedProduct();
        display.update(String.format("[%s] %s", this.selectedProduct.getType(), VendingMachineMessages.DISPENSED_TO_BUCKET.label));
    }

    /**
     * Sets the selected product to null
     */
    public final void undoProductSelection() {
        this.selectedProduct = null;
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
     * Returns the credit stack, this method is intended to be used by the class vendingMachine
     * the use of helper methods getCreditStackSize and isCreditStackEmpty should be enough.
     * @return stack of credit inserted to the machine.
     */
    protected Stack<Coin> getCreditStack() {
        return creditStack;
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
        return this.selectedProduct.getPrice() - this.credit.get();
    }

    /**
     * Validates if is possible to give change when pending balance < 0 [the sames as if credit is
     * less than selectedProduct.price] based on the existing cash on the coin dispenser.
     * @param changeRequested the amount to check against the coin dispenser
     * @return true if possible or false otherwise
     */
    public boolean canGiveChange(int changeRequested) {
        if (changeRequested > 0) return true;
        int amountToReturn = Math.abs(changeRequested);
        if (countCashInDispenser() <= amountToReturn) return false;

        Stack<Coin> changeStack = new Stack<>();
        int inChangeStack = 0;
        boolean pending = Math.abs(amountToReturn - inChangeStack) > 0.0001;
        int order = coinShelves.size() - 1;
        while(pending && order >= 0) {
            Coin coin = Coin.retrieveCoinByOrder(order);
            Shelf<Coin> coinShelf = coinShelves.get(coin);
            while(coinShelf.getItemCount() > 0 && amountToReturn > coin.denomination) {
                coinShelf.dispense();
                changeStack.push(coin);
                inChangeStack += coin.denomination;
                amountToReturn -= coin.denomination;
            }
            pending = Math.abs(amountToReturn - inChangeStack) > 0.0001;
        }
        while(!changeStack.isEmpty()) {
            coinShelves.get(changeStack.peek()).provision();
        }
        return !pending;
    }
}
