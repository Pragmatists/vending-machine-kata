package tdd.vendingMachine.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.CashierPad;
import tdd.vendingMachine.domain.Denomination;
import tdd.vendingMachine.domain.Display;
import tdd.vendingMachine.domain.ShelfBox;
import tdd.vendingMachine.domain.display.impl.*;
import tdd.vendingMachine.domain.strategy.impl.HighestFirstMoneyChangeStrategy;
import tdd.vendingMachine.exception.MoneyChangeException;
import tdd.vendingMachine.exception.ShelfNotExistException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author kdkz
 */
public class VendingMachineListener implements VendingActionListener {

    private final static Logger log = LoggerFactory.getLogger(VendingMachineListener.class);

    private CashierPad cashierPad = new CashierPad();

    private ShelfBox shelfBox = new ShelfBox();

    private Display display = new Display();

    private BigDecimal productPrice;

    private BigDecimal currentInsertedAmount;

    @Override
    public void onCoinInsertion(Denomination denomination, Integer quantity) {
        log.debug("{} denomination inserted in quantity {}.", denomination, quantity);
        if (productPrice != null) {
            currentInsertedAmount = cashierPad.insertCoins(denomination, quantity);
            try {
                if (tryBuyProduct()) {
                    log.debug("Product sold!");
                    clearContents();
                    return;
                }
                log.debug("{} already inserted. {} left.", currentInsertedAmount, productPrice.subtract(currentInsertedAmount));
                display.show(new InsertedCoinsStatusScreen(productPrice.subtract(currentInsertedAmount)));
                return;
            } catch (MoneyChangeException e) {
                log.error("Failed to buy product. Vending machine does not have enough money to get the rest. {}", e.getMessage());
                display.show(new UnableToCountRestScreen());
                return;
            }
        }
        log.warn("Inserted money returned. Product must be selected first.");
        display.show(new SelectShelfFirstScreen());
    }

    @Override
    public void onShelfSelected(int shelfNumber) {
        try {
            productPrice = shelfBox.selectShelfAndGetPrice(shelfNumber);
            display.show(new SelectedShelfScreen(shelfNumber, productPrice));
        } catch (ShelfNotExistException e) {
            log.error("Failed to select shelf. {}", e.getMessage());
        }
    }

    @Override
    public void onCancelButtonClicked() {
        clearContents();
        log.debug("Cancel button selected. Already inserted coins in amount {} returned.", currentInsertedAmount);
        display.show(new CancelScreen());
    }

    private void clearContents() {
        productPrice = null;
        currentInsertedAmount = null;
        cashierPad.returnInsertedCoins();
    }

    private boolean tryBuyProduct() throws MoneyChangeException {
        if (currentInsertedAmount.compareTo(productPrice) >= 0) {
            log.debug("Inserted amount match product price.");
            log.debug("Buying procedure started. Wait for counting rest.");
            Map<Denomination, Integer> rest = cashierPad.payAndReturnChange(productPrice, new HighestFirstMoneyChangeStrategy());
            log.debug("Buying procedure finished. Product and rest spent.");
            display.show(new ProductSoldScreen(rest, cashierPad.getAmountFromCoins(rest)));
            return true;
        }
        return false;
    }
}
