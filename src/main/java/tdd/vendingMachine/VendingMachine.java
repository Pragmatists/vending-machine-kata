package tdd.vendingMachine;

import lombok.NonNull;
import org.apache.log4j.Logger;
import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.domain.exception.*;
import tdd.vendingMachine.state.*;
import tdd.vendingMachine.util.Constants;
import tdd.vendingMachine.validation.VendingMachineValidator;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class VendingMachine {

    private static final Logger logger = Logger.getLogger(VendingMachine.class);

    public final VendingMachineConfiguration vendingMachineConfiguration;

    //States
    private final SoldOutState soldOutState;
    private final ReadyState readyState;
    private final InsufficientCreditState insufficientCreditState;
    private final NoCreditSelectedProductState noCreditSelectedProductState;
    private final CreditNotSelectedProductState creditNotSelectedProductState;
    private final TechnicalErrorState technicalErrorState;

    //states
    private final VendingMachineDisplay display;
    private Shelf<Product> selectedShelf;
    private AtomicInteger credit;
    private Stack<Coin> creditStack;
    private final Map<Integer, Shelf<Product>> productShelves;

    private final Map<Coin, Shelf<Coin>> coinShelves;
    private State currentState;

    VendingMachine(@NonNull Map<Integer, Shelf<Product>> productShelves, @NonNull Map<Coin, Shelf<Coin>> coinShelves) {
        vendingMachineConfiguration = new VendingMachineConfiguration();
        VendingMachineValidator.validate(vendingMachineConfiguration, productShelves, coinShelves);
        this.productShelves = productShelves;
        this.coinShelves = Collections.unmodifiableMap(coinShelves);
        this.credit = new AtomicInteger(0);
        this.selectedShelf = null;
        this.display = new VendingMachineDisplay();
        this.creditStack = new Stack<>();

        this.soldOutState = new SoldOutState(this);
        this.readyState = new ReadyState(this);
        this.noCreditSelectedProductState = new NoCreditSelectedProductState(this);
        this.insufficientCreditState = new InsufficientCreditState(this);
        this.creditNotSelectedProductState = new CreditNotSelectedProductState(this);
        this.technicalErrorState = new TechnicalErrorState(this);

        int availableProducts = productShelves.values().stream()
            .mapToInt(Shelf::getItemCount)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
        this.currentState = availableProducts > 0 ? readyState : soldOutState;
    }

    public void insertCoin(Coin money) { currentState.insertCoin(money); }

    public void selectShelfNumber(int shelfNumber) { currentState.selectShelfNumber(shelfNumber); }

    public void cancel() { currentState.cancel(); }

    /**
     * Throws exception if given shelfNumber is invalid
     * @param shelfNumber the shelf to be checked
     * @throws NoSuchElementException if shelf is not available on the vending machine
     */
    private void validShelfNumber(int shelfNumber) throws NoSuchElementException {
        if (null == productShelves.get(shelfNumber)) {
            throw new NoSuchElementException(
                VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber, false)
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

    public CreditNotSelectedProductState getCreditNotSelectedProductState() {
        return creditNotSelectedProductState;
    }

    public ReadyState getReadyState() {
        return readyState;
    }

    public NoCreditSelectedProductState getNoCreditSelectedProductState() {
        return noCreditSelectedProductState;
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
        display.update(String.format("[%s] %s: %s", product.provideType(), message, VendingMachineMessages.provideCashToDisplay(toDisplay)));
    }

    public final void showMessageOnDisplay(String message) {
        this.display.update(message);
    }


    /**
     * If there is room in the coin dispenser for the given coin is credited otherwise throws exception
     * @param coin coin to insert
     * @throws CashDispenserFullException if shelf coin shelf for the given coin on dispenser is full
     */
    public final void addCoinToCredit(Coin coin) throws CashDispenserFullException {
        if (!dispenserHasCoinSlotAvailable(coin)) {
            throw new CashDispenserFullException(VendingMachineMessages.CASH_NOT_ACCEPTED_DISPENSER_FULL.label, coin.denomination);
        }
        credit.addAndGet(coin.denomination);
        creditStack.push(coin);
        if (null == selectedShelf) {
            this.display.update(String.format("%s %s: %s", coin.label, VendingMachineMessages.CASH_ACCEPTED_NEW_CREDIT.label,
                VendingMachineMessages.provideCashToDisplay(credit.get())));
        } else {
            this.display.update(String.format("[%s], %s: %s", selectedShelf.getType().getType(), VendingMachineMessages.PENDING.label,
                VendingMachineMessages.provideCashToDisplay(calculatePendingBalance())));
        }
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
                display.update(String.format("[%s] %s: %s", creditStack.pop().label, VendingMachineMessages.RETURN_TO_BUCKET_CREDIT.label,
                    VendingMachineMessages.provideCashToDisplay(this.credit.get())));
            }
        }
    }

    /**
     * Given a shelfNumber selects the product
     * @param shelfNumber the number of the shelve to retrieve the product from.
     * @throws NoSuchElementException if the shelf number is invalid
     * @throws ShelfEmptyNotAvailableForSelectionException if the requested shelf is empty
     */
    public final void selectProductGivenShelfNumber(int shelfNumber) throws NoSuchElementException, ShelfEmptyNotAvailableForSelectionException {
        validShelfNumber(shelfNumber);
        Shelf<Product> shelf = productShelves.get(shelfNumber);
        if (shelf.getItemCount() <= 0) {
            throw new ShelfEmptyNotAvailableForSelectionException(
                VendingMachineMessages.UNABLE_TO_SELECT_EMPTY_SHELF.label, shelfNumber);
        }
        this.selectedShelf = shelf;
    }

    /**
     * Moves the money from the stack to the vending machine's cash dispenser, the credit
     * remains the same since no product has been given.
     * @throws NotEnoughSlotsAvailableDispenserException if unable to provision because no free slots are available
     */
    public final void provisionCreditStackCashToDispenser() throws NotEnoughSlotsAvailableDispenserException {
        for (Coin coin: creditStack) {
            coinShelves.get(coin).provision();
        }
    }

    /**
     * Dispenses product to the pickup bucket
     * @throws NoSuchElementException if no product was previously selected
     */
    public final void dispenseSelectedProductToBucketAndClearCreditStack() throws NoSuchElementException {
        validateSelectedProduct();
        Product product = this.selectedShelf.getType();
        this.selectedShelf.dispense();
        while (!creditStack.isEmpty()) creditStack.pop();
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
        Iterator<Coin> order = Coin.descendingDenominationIterable().iterator();
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
     * @throws UnableToProvideBalanceException if is not possible to provide such amount from the cash dispenser.
     */
    public void dispenseCurrentBalance() throws UnableToProvideBalanceException {
        int balance = calculatePendingBalance();
        if (!canGiveChangeFromCashDispenser(balance)) {
            throw new UnableToProvideBalanceException(VendingMachineMessages.NOT_ENOUGH_CASH_TO_GIVE_CHANGE.label, balance);
        }
        int pending = Math.abs(balance);
        Iterator<Coin> order = Coin.descendingDenominationIterable().iterator();
        while(pending > 0 && order.hasNext()) {
            Coin coin = order.next();
            Shelf<Coin> coinShelf = coinShelves.get(coin);
            while(!coinShelf.isEmpty() && pending >= coin.denomination) {
                coinShelf.dispense();
                pending -= coin.denomination;
                this.display.update(
                    String.format("[%s] %s: %s", VendingMachineMessages.provideCashToDisplay(coin.denomination),
                        VendingMachineMessages.PENDING_BALANCE_RETURNED_TO_BUCKET.label,
                        VendingMachineMessages.provideCashToDisplay(-pending))
                );
            }
        }
        this.credit.set(0);//credit goes to zero since all the cash has been credited
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

    /**
     * After having credited users stack to dispenser, and unable to dispense product, should return
     * the total cash to pickup cash bucket and compensate the cash dispenser
     */
    public final void returnCreditStackToBucketUpdatingCashDispenser() {
        while (!creditStack.isEmpty()) {
            this.coinShelves.get(creditStack.peek()).dispense();
            this.credit.addAndGet(-creditStack.peek().denomination);
            display.update(String.format("[%s] %s: %s", creditStack.pop().label, VendingMachineMessages.RETURN_TO_BUCKET_CREDIT.label,
                VendingMachineMessages.provideCashToDisplay(this.credit.get())));
        }
    }

    public State getTechnicalErrorState() {
        return technicalErrorState;
    }
}
