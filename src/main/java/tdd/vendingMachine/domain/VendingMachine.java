package tdd.vendingMachine.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.display.ScreenFactory;
import tdd.vendingMachine.exception.MoneyChangeException;
import tdd.vendingMachine.exception.ShelfNotExistException;
import tdd.vendingMachine.listener.VendingMachineObserver;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static tdd.vendingMachine.domain.display.ScreenFactory.ScreenType.*;

/**
 * Class represents Vending Machine terminal on which the client tries to make transactions.
 *
 * @author kdkz
 */
public class VendingMachine {

    private final static Logger log = LoggerFactory.getLogger(VendingMachine.class);

    private Set<VendingMachineObserver> observers = new HashSet<>();

    private ScreenFactory screenFactory;

    private BigDecimal productPrice;

    private BigDecimal currentInsertedAmount;

    /**
     * Inserts coins into Vending Machine
     *
     * @param denomination coin denomination
     * @param quantity     coins quantity - For ease testing. On real world coins are inserted one by one
     */
    public void insertCoins(Denomination denomination, Integer quantity) {
        log.debug("{} denomination inserted in quantity {}.", denomination, quantity);
        if (productPrice != null) {
            observers.forEach(observer -> observer.coinInserted(denomination, quantity));
            return;
        }
        log.warn("Inserted money returned. Product must be selected first.");
        screenFactory.displayScreen(SELECT_SHELF_FIRST_SCREEN, null);
    }

    /**
     * Selects shelf with products in Vending Machine
     *
     * @param shelfNumber shelf number
     */
    public void selectShelf(int shelfNumber) {
        try {
            log.debug("Shelf number {} selected.");
            for (VendingMachineObserver observer : observers) {
                observer.shelfSelected(shelfNumber);
            }
        } catch (ShelfNotExistException e) {
            log.error("Failed to select shelf. {}", e.getMessage());
        }
    }

    /**
     * Cancels the transaction in Vending Machine
     */
    public void pressCancelButton() {
        clearContents();
        observers.forEach(VendingMachineObserver::cancelButtonSelected);
        log.debug("Cancel button selected. Already inserted coins in amount {} returned.", currentInsertedAmount);
        screenFactory.displayScreen(CANCEL_SCREEN, null);
    }

    /**
     * Checks if already inserted amount of money is sufficient to buy a product.
     *
     * @param alreadyInsertedAmount already inserted amount
     */
    public void isSufficient(BigDecimal alreadyInsertedAmount) {
        this.currentInsertedAmount = alreadyInsertedAmount;
        try {
            if (tryBuyProduct()) {
                log.debug("Product sold!");
                clearContents();
                return;
            }
            log.debug("{} already inserted. {} left.", alreadyInsertedAmount, productPrice.subtract(alreadyInsertedAmount));
            screenFactory.displayScreen(
                INSERTED_COINS_STATUS_SCREEN,
                () -> singletonList(productPrice.subtract(alreadyInsertedAmount).toPlainString()));
        } catch (MoneyChangeException e) {
            log.error("Failed to buy product. Vending machine does not have enough money to get the rest. {}", e.getMessage());
            screenFactory.displayScreen(UNABLE_TO_COUNT_REST_SCREEN, null);
        }
    }

    /**
     * Notifies correctly counted rest is returned.
     *
     * @param rest            returned rest in coins
     * @param amountFromCoins rest amount
     */
    public void restReturned(Map<Denomination, Integer> rest, BigDecimal amountFromCoins) {
        log.debug("Returning rest {}", amountFromCoins);
        screenFactory.displayScreen(
            PRODUCT_SOLD_SCREEN, () -> asList(amountFromCoins.toPlainString(), rest.toString()));
    }

    /**
     * Notifies about selected product price
     *
     * @param shelfNumber  shelf number
     * @param productPrice product price
     */
    public void returnProductPrice(int shelfNumber, BigDecimal productPrice) {
        this.productPrice = productPrice;
        screenFactory.displayScreen(
            SELECTED_SHELF_SCREEN,
            () -> asList(String.valueOf(shelfNumber), productPrice.toPlainString()));
    }

    /**
     * Clears temporary saved state about product price and already inserted amount
     */
    private void clearContents() {
        productPrice = null;
        currentInsertedAmount = null;
    }

    /**
     * Tries to make transaction.
     *
     * @return true if transaction was made successfully, false otherwise
     * @throws MoneyChangeException
     */
    private boolean tryBuyProduct() throws MoneyChangeException {
        if (currentInsertedAmount.compareTo(productPrice) >= 0) {
            log.debug("Inserted amount match product price.");
            log.debug("Buying procedure started. Wait for counting rest.");
            for (VendingMachineObserver observer : observers) {
                observer.sufficientAmountInserted(productPrice);
            }
            log.debug("Buying procedure finished. Product and rest spent.");
            return true;
        }
        return false;
    }

    public void registerObserver(VendingMachineObserver observer) {
        observers.add(observer);
    }

    public void adDisplayMechanism(ScreenFactory screenFactory) {
        this.screenFactory = screenFactory;
    }
}
