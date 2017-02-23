package tdd.vendingMachine;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.NonNull;
import org.apache.log4j.Logger;
import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.state.*;
import tdd.vendingMachine.util.Constants;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.*;

public final class VendingMachine implements State {

    private static final Logger logger = Logger.getLogger(VendingMachine.class);

    public static final VendingMachineConfiguration VENDING_MACHINE_CONFIGURATION = new VendingMachineConfiguration();

    //States
    private final SoldOutState soldOutState;
    private final NoCreditNoProductSelectedState noCreditNoProductSelectedState;
    private final HasCreditProductSelectedState hasCreditProductSelectedState;
    private final NoCreditProductSelectedState noCreditProductSelectedState;
    private final HasCreditNoProductSelectedState hasCreditNoProductSelectedState;

    //states

    private final VendingMachineDisplay display;
    private Product selectedProduct;
    private AtomicDouble credit;
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
        this.credit = new AtomicDouble(0.0);
        this.selectedProduct = null;
        this.display = new VendingMachineDisplay();
        this.creditStack = new Stack<>();

        this.soldOutState = new SoldOutState(this);
        this.noCreditNoProductSelectedState = new NoCreditNoProductSelectedState(this);
        this.noCreditProductSelectedState = new NoCreditProductSelectedState(this);
        this.hasCreditProductSelectedState = new HasCreditProductSelectedState(this);
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

    private void validShelfNumber(int shelfNumber) {
        if (null == productShelves.get(shelfNumber)) {
            throw new NoSuchElementException("WARN " + VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE + ":" + shelfNumber);
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

    public HasCreditProductSelectedState getHasCreditProductSelectedState() {
        return this.hasCreditProductSelectedState;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * Returns the available credit on the vending machine
     * @return double must be greater or equal to zero.
     */
    public double getCredit() {
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
        double toDisplay = this.selectedProduct == null ? product.getPrice() : product.getPrice() - getCredit();
        String message = this.selectedProduct == null ? VendingMachineMessages.PRICE.label : VendingMachineMessages.PENDING.label;
        display.update(String.format("[%s] %s: %.2f$", product.provideType(), message, toDisplay));
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
            this.display.update(String.format("%s %s: %.2f", coin.label, VendingMachineMessages.CASH_ACCEPTED_NEW_CREDIT.label, credit.get()));
            return true;
        }
        this.display.update(String.format("%s %s: %.2f", coin.label, VendingMachineMessages.CASH_NOT_ACCEPTED_DISPENSER_FULL.label, credit.get()));
        return false;
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
                display.update(String.format("[%s] %s: %.2f", creditStack.pop().label, VendingMachineMessages.RETURN_TO_BUCKET_CREDIT.label, getCredit()));
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
        if(null == this.selectedProduct) {
            throw new NoSuchElementException("WARN: " + VendingMachineMessages.NO_PRODUCT_SELECTED.label);
        }
        display.update(String.format("[%s] %s", this.selectedProduct, VendingMachineMessages.DISPENSED_TO_BUCKET.label));
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
     * @return a double resulting of the sum of every coin.shelf.denomination times amounts of coins of that denomination
     */
    protected final double countCashInDispenser() {
        return coinShelves.entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .mapToDouble(nonEmptyEntry -> nonEmptyEntry.getKey().denomination * nonEmptyEntry.getValue().getItemCount())
            .reduce(Constants.SUM_DOUBLE_IDENTITY, Constants.SUM_DOUBLE_BINARY_OPERATOR);
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
}
