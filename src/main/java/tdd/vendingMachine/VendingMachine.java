package tdd.vendingMachine;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.NonNull;
import org.apache.log4j.Logger;
import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.state.*;
import tdd.vendingMachine.util.Constants;

import java.util.*;

public final class VendingMachine implements State {

    private static final Logger logger = Logger.getLogger(VendingMachine.class);

    public static final VendingMachineConfiguration VENDING_MACHINE_CONFIGURATION = new VendingMachineConfiguration();
    public static final String MSG_NO_CREDIT_AVAILABLE = "WARN: No credit available to return.";

    //States
    public final SoldOutState soldOutState;
    public final NoCreditNoProductSelectedState noCreditNoProductSelectedState;
    public final HasCreditProductSelectedState hasCreditProductSelectedState;
    private final NoCreditProductSelectedState noCreditProductSelectedState;
    private final HasCreditNoProductSelectedState hasCreditNoProductSelectedState;

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
            throw new NoSuchElementException("WARN Shelf number not available:" + shelfNumber);
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

    public VendingMachineDisplay getDisplay() {
        return display;
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

    /**
     * Provides the credit stack of coins
     * @return the credit stack for the current vending machine
     */
    public Stack<Coin> getCreditStack() {
        return creditStack;
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
     * Displays the product price
     * @param shelfNumber the selected option on the keypad of the vending machine
     * @throws NoSuchElementException in shelf number is not available
     */
    public final void displayProductPrice(int shelfNumber) throws NoSuchElementException {
        validShelfNumber(shelfNumber);
        display.update("Price :" + productShelves.get(shelfNumber).getType().provideValue());
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
            this.display.update(String.format("Received %s, credit: %.2f", coin.label, credit.get()));
            return true;
        }
        this.display.update(String.format("WARN: %s returned to bucket (dispenser full try other denominations), credit: %.2f", coin.label, credit.get()));
        return false;
    }

    /**
     * Drops the current credit stack and sets the credit to zero
     */
    public final void returnAllCreditToBucket() {
        if (creditStack.isEmpty()) {
            display.update(MSG_NO_CREDIT_AVAILABLE);
        } else {
            while(!creditStack.isEmpty()) {
                credit.addAndGet(-creditStack.peek().denomination);
                display.update(String.format("Returned %s to bucket, credit: %.2f", creditStack.pop().label, getCredit()));
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
            throw new NoSuchElementException("WARN: No product is selected");
        }
        display.update(String.format("Product %s dispensed to pickup bucket", this.selectedProduct));
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
    protected final boolean dispenserHasCoinSlotAvailable(Coin coin) {
        return coinShelves.get(coin).countFreeSlots() > 0;
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
}
