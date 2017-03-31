package tdd.vendingMachine.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.display.ScreenFactory;
import tdd.vendingMachine.domain.strategy.impl.HighestFirstMoneyChangeStrategy;
import tdd.vendingMachine.exception.MoneyChangeException;
import tdd.vendingMachine.exception.ShelfNotExistException;
import tdd.vendingMachine.listener.VendingMachineNotifier;
import tdd.vendingMachine.listener.VendingMachineObserver;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static tdd.vendingMachine.domain.display.ScreenFactory.ScreenType.*;

/**
 * @author kdkz
 */
public class VendingMachine {

    private final static Logger log = LoggerFactory.getLogger(VendingMachine.class);


    private Set<VendingMachineObserver> observers = new HashSet<>();

    private ScreenFactory screenFactory;

    private BigDecimal productPrice;

    private BigDecimal currentInsertedAmount;

    public void power() {
        VendingMachineNotifier vendingMachineNotifier = new VendingMachineNotifier();
        vendingMachineNotifier.setVendingMachine(this);

        screenFactory = new ScreenFactory();
        CashierPad cashierPad = new CashierPad(new HighestFirstMoneyChangeStrategy(), vendingMachineNotifier);
        ShelfBox shelfBox = new ShelfBox(vendingMachineNotifier);

        observers.add(cashierPad);
        observers.add(shelfBox);
    }

    public void insertCoins(Denomination denomination, Integer quantity) {
        log.debug("{} denomination inserted in quantity {}.", denomination, quantity);
        if (productPrice != null) {
            observers.forEach(observer -> observer.coinInserted(denomination, quantity));
            return;
        }
        log.warn("Inserted money returned. Product must be selected first.");
        screenFactory.displayScreen(SELECT_SHELF_FIRST_SCREEN, null);
    }

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

    public void pressCancelButton() {
        clearContents();
        log.debug("Cancel button selected. Already inserted coins in amount {} returned.", currentInsertedAmount);
        screenFactory.displayScreen(CANCEL_SCREEN, null);
    }

    public void isSufficient(BigDecimal currentInsertedAmount) {
        this.currentInsertedAmount = currentInsertedAmount;
        try {
            if (tryBuyProduct()) {
                log.debug("Product sold!");
                clearContents();
                return;
            }
            log.debug("{} already inserted. {} left.", currentInsertedAmount, productPrice.subtract(currentInsertedAmount));
            screenFactory.displayScreen(
                INSERTED_COINS_STATUS_SCREEN,
                () -> singletonList(productPrice.subtract(currentInsertedAmount).toPlainString()));
        } catch (MoneyChangeException e) {
            log.error("Failed to buy product. Vending machine does not have enough money to get the rest. {}", e.getMessage());
            screenFactory.displayScreen(UNABLE_TO_COUNT_REST_SCREEN, null);
        }
    }

    public void returnRest(Map<Denomination, Integer> rest, BigDecimal amountFromCoins) {
        log.debug("Returning rest {}", amountFromCoins);
        screenFactory.displayScreen(
            PRODUCT_SOLD_SCREEN, () -> asList(amountFromCoins.toPlainString(), rest.toString()));
    }

    public void returnProductPrice(int shelfNumber, BigDecimal productPrice) {
        this.productPrice = productPrice;
        screenFactory.displayScreen(
            SELECTED_SHELF_SCREEN,
            () -> asList(String.valueOf(shelfNumber), productPrice.toPlainString()));
    }

    private void clearContents() {
        productPrice = null;
        currentInsertedAmount = null;
        observers.forEach(VendingMachineObserver::cancelButtonSelected);
    }

    private boolean tryBuyProduct() throws MoneyChangeException {
        if (currentInsertedAmount.compareTo(productPrice) >= 0) {
            log.debug("Inserted amount match product price.");
            log.debug("Buying procedure started. Wait for counting rest.");
            for (VendingMachineObserver observer : observers) {
                observer.sufficientValueInserted(productPrice);
            }
            log.debug("Buying procedure finished. Product and rest spent.");
            return true;
        }
        return false;
    }
}
