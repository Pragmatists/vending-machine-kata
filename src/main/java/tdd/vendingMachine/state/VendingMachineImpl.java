package tdd.vendingMachine.state;

import lombok.NonNull;
import org.apache.log4j.Logger;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.domain.exception.CashDispenserFullException;
import tdd.vendingMachine.domain.exception.NotEnoughSlotsAvailableDispenserException;
import tdd.vendingMachine.domain.exception.ShelfEmptyNotAvailableForSelectionException;
import tdd.vendingMachine.domain.exception.UnableToProvideBalanceException;
import tdd.vendingMachine.util.Constants;
import tdd.vendingMachine.validation.VendingMachineValidator;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class VendingMachineImpl implements VendingMachine {

    private static final Logger logger = Logger.getLogger(VendingMachineImpl.class);

    private final VendingMachineConfiguration vendingMachineConfiguration;

    //States
    private final State soldOutState;
    private final State readyState;
    private final State insufficientCreditState;
    private final State noCreditSelectedProductState;
    private final State creditNotSelectedProductState;
    private final State technicalErrorState;

    //states
    private final VendingMachineDisplay display;
    private final AtomicInteger credit;
    private final Stack<Coin> creditStack;
    private final Map<Integer, Shelf<Product>> productShelves;
    private final Map<Coin, Shelf<Coin>> coinShelves;

    //mutable fields
    private Shelf<Product> selectedShelf;
    private State currentState;

    VendingMachineImpl(@NonNull Map<Integer, Shelf<Product>> productShelves, @NonNull Map<Coin, Shelf<Coin>> coinShelves) {
        vendingMachineConfiguration = new VendingMachineConfiguration();
        VendingMachineValidator.validateNewVendingMachineParameters(vendingMachineConfiguration, productShelves, coinShelves);
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

    public final void insertCoin(Coin money) { currentState.insertCoin(money); }

    public final void selectShelfNumber(int shelfNumber) { currentState.selectShelfNumber(shelfNumber); }

    public final void cancel() { currentState.cancel(); }

    /**
     * Throws exception if given shelfNumber is invalid
     *
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
     *
     * @throws NoSuchElementException when no product was selected
     */
    private void validateSelectedProduct() throws NoSuchElementException {
        if (null == selectedShelf) {
            throw new NoSuchElementException(VendingMachineMessages.buildWarningMessageWithoutSubject(VendingMachineMessages.NO_PRODUCT_SELECTED.label));
        }
    }

    /**
     * Utility method to evaluate if there is room to store given coin
     * @param coin coin to insert in the machine
     * @return true if room is available for given coin
     */
    final boolean dispenserHasCoinSlotAvailable(final Coin coin) {
        long existingCoinsOnCreditStack = creditStack.stream().filter(coin::equals).count();
        return coinShelves.get(coin).countFreeSlots() - existingCoinsOnCreditStack > 0;
    }

    /**
     * Counts the total cash in the cashDispenser
     * @return a int resulting of the sum of every coin.shelf.denomination times amounts of coins of that denomination
     */
    final int countCashInDispenser() {
        return coinShelves.entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .mapToInt(nonEmptyEntry -> nonEmptyEntry.getKey().denomination * nonEmptyEntry.getValue().getItemCount())
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
    }

    /**
     * Validates if is possible to give change when pending balance < 0 [the sames as if credit is
     * less than selectedProduct.price] based on the existing cash on the coin dispenser.
     * @param changeRequested the requested amount to return from the cash dispenser
     * @return true if possible or false otherwise
     */
    final boolean canGiveChangeFromCashDispenser(int changeRequested) {
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
     * Moves the money from the stack to the vending machine's cash dispenser, the credit
     * remains the same since no product has been given.
     * @throws NotEnoughSlotsAvailableDispenserException if unable to provision because no free slots are available
     */
    final void provisionCreditStackCashToDispenser() throws NotEnoughSlotsAvailableDispenserException {
        for (Coin coin: creditStack) {
            coinShelves.get(coin).provision();
        }
    }
    /**
     * Drops the pending balance to the coin dispense bucket if is possible to build the amount from the
     * coins available on the cash dispenser otherwise throws an exception.
     * @throws UnableToProvideBalanceException if is not possible to provide such amount from the cash dispenser.
     */
    final void dispenseCurrentBalance() throws UnableToProvideBalanceException {
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
     * Dispenses product to the pickup bucket
     * @throws NoSuchElementException if no product was previously selected
     */
    final void dispenseSelectedProductToBucketAndClearCreditStack() throws NoSuchElementException {
        validateSelectedProduct();
        Product product = this.selectedShelf.getType();
        this.selectedShelf.dispense();
        while (!creditStack.isEmpty()) creditStack.pop();
        display.update(String.format("[%s] %s", product.getType(), VendingMachineMessages.DISPENSED_TO_BUCKET.label));
    }

    /**
     * Regular sell execution method described as follows:
     *  1. If there is a selected product and the credit covers the price of that product the sell procedure:
     *      a. Provision all the credit from the user to the dispenser
     *      b. dispense if possible the balance to the cash pickup bucket
     *      c. dispense the product to the pickup bucket
     *      d. remove the selection of the product
     *      e. pass the machine to readyState
     *      f. return true
     *   or return false.
     * @throws UnableToProvideBalanceException If unable to provide change
     */
    final void attemptSell() throws UnableToProvideBalanceException {
        if (this.provideSelectedProduct() != null
            && Integer.compare(this.provideCredit(), this.provideSelectedProduct().getPrice()) >= 0) {
            this.provisionCreditStackCashToDispenser();
            this.dispenseCurrentBalance();
            this.dispenseSelectedProductToBucketAndClearCreditStack();
            this.undoProductSelection();
            if (this.isSoldOut()) {
                this.currentState = soldOutState;
            } else {
                this.setStateToReadyState();
            }
        }
    }

    /**
     * After having credited users stack to dispenser, and unable to dispense product, should return
     * the total cash to pickup cash bucket and compensate the cash dispenser
     */
    final void returnCreditStackToBucketUpdatingCashDispenser() {
        while (!creditStack.isEmpty()) {
            this.coinShelves.get(creditStack.peek()).dispense();
            this.credit.addAndGet(-creditStack.peek().denomination);
            display.update(String.format("[%s] %s: %s", creditStack.pop().label, VendingMachineMessages.RETURN_TO_BUCKET_CREDIT.label,
                VendingMachineMessages.provideCashToDisplay(this.credit.get())));
        }
    }

    /**
     * Compensation task meant to be run in case of failure to perform sell
     * if state unable to perform task machine will enter on technical error state
     * @param exceptionMessage the exception message received from sell transaction
     * @param pendingBalance the current pending balance
     */
    final void rollBackSell(String exceptionMessage, int pendingBalance) {
        this.showMessageOnDisplay(
            VendingMachineMessages.buildWarningMessageWithSubject(
                String.format("%s [%s] %s", exceptionMessage,
                    VendingMachineMessages.provideCashToDisplay(pendingBalance),
                    VendingMachineMessages.RETURNING_TOTAL_CASH_TO_BUCKET.label),
                this.provideCredit()
            )
        );
        this.undoProductSelection();
        this.returnCreditStackToBucketUpdatingCashDispenser();
        this.setStateToReadyState();
    }

    @Override
    public final State provideCurrentState() {
        return currentState;
    }

    @Override
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

    @Override
    public final void showMessageOnDisplay(String message) {
        this.display.update(message);
    }

    @Override
    public final void displayProductPrice(int shelfNumber) throws NoSuchElementException {
        validShelfNumber(shelfNumber);
        Product product = productShelves.get(shelfNumber).getType();
        int toDisplay = this.selectedShelf == null ? product.getPrice() : calculatePendingBalance();
        String message = this.selectedShelf == null ? VendingMachineMessages.PRICE.label : VendingMachineMessages.PENDING.label;
        display.update(String.format("[%s] %s: %s", product.provideType(), message, VendingMachineMessages.provideCashToDisplay(toDisplay)));
    }

    @Override
    public Product provideSelectedProduct() {
        return selectedShelf != null ? this.selectedShelf.getType() : null;
    }

    @Override
    public final int provideCredit() {
        return credit.get();
    }

    @Override
    public final void undoProductSelection() {
        this.selectedShelf = null;
    }

    @Override
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

    @Override
    public final void selectProductGivenShelfNumber(int shelfNumber) throws NoSuchElementException, ShelfEmptyNotAvailableForSelectionException {
        validShelfNumber(shelfNumber);
        Shelf<Product> shelf = productShelves.get(shelfNumber);
        if (shelf.getItemCount() <= 0) {
            throw new ShelfEmptyNotAvailableForSelectionException(
                VendingMachineMessages.UNABLE_TO_SELECT_EMPTY_SHELF.label, shelfNumber);
        }
        this.selectedShelf = shelf;
    }

    @Override
    public final boolean isCreditStackEmpty() {
        return creditStack.empty();
    }

    @Override
    public boolean isSoldOut() {
        return countTotalAmountProducts() == 0;
    }

    @Override
    public final int getCreditStackSize() {
        return creditStack.size();
    }

    @Override
    public String getDisplayCurrentMessage() {
        return display.getCurrentMessage();
    }

    @Override
    public int calculatePendingBalance() throws NoSuchElementException{
        validateSelectedProduct();
        return this.selectedShelf.getType().getPrice() - this.credit.get();
    }

    @Override
    public int countTotalAmountProducts() {
        return productShelves.values().stream()
            .mapToInt(Shelf::getItemCount)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
    }

    @Override
    public int countProductsOnShelf(int shelfNumber) throws NoSuchElementException{
        validShelfNumber(shelfNumber);
        return productShelves.get(shelfNumber).getItemCount();
    }

    /**
     * Attempts to set the vending machine to soldOut state
     */
    private void setStateToSoldOutState() {
        VendingMachineValidator.validateToSoldOutState(this);
        this.currentState = soldOutState;
    }

    /**
     * Attempts to send the vending machine to ReadyState
     */
    private void setStateToReadyState() {
        VendingMachineValidator.validateToReadyState(this);
        this.currentState = readyState;
    }

    /**
     * Attempts to send the vending machine to InsufficientCreditState
     */
    private void setStateToInsufficientCreditState() {
        VendingMachineValidator.validateToInsufficientCreditState(this);
        this.currentState = insufficientCreditState;
    }

    /**
     * Attempts to send the vending machine to NoCreditSelectedProductState
     */
    private void setStateToNoCreditSelectedProductState() {
        VendingMachineValidator.validateToNoCreditSelectedProductState(this);
        this.currentState = noCreditSelectedProductState;
    }

    /**
     * Attempts to send the vending machine to NotSelectedProductState
     */
    private void setStateToCreditNotSelectedProductState() {
        VendingMachineValidator.validateCreditNotSelectedProductState(this);
        this.currentState = creditNotSelectedProductState;
    }

    /**
     * Sets the machine state to technical error state
     */
    private void setStateToTechnicalErrorState() {
        this.currentState = technicalErrorState;
    }

    @Override
    public void sendStateTo(StateEnum state) {
        switch (state) {
            case TECHNICAL_ERROR:
                setStateToTechnicalErrorState();
                break;
            case SOLD_OUT:
                setStateToSoldOutState();
                break;
            case READY:
                setStateToReadyState();
                break;
            case INSUFFICIENT_CREDIT:
                setStateToInsufficientCreditState();
                break;
            case CREDIT_NOT_SELECTED_PRODUCT:
                setStateToCreditNotSelectedProductState();
                break;
            case NO_CREDIT_SELECTED_PRODUCT:
                setStateToNoCreditSelectedProductState();
                break;
            default: throw new NoSuchElementException("the given state was not found in the system");
        }
    }
}
