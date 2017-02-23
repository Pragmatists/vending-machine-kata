package tdd.vendingMachine.state;

import org.apache.log4j.Logger;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 * State representing a machine with a selected product but with credit lower than
 * the selected product's price
 */
public class InsufficientCreditState implements State {

    private static final Logger logger = Logger.getLogger(InsufficientCreditState.class);
    public static final String label = "HAS CREDIT PRODUCT SELECTED";
    final VendingMachine vendingMachine;

    public InsufficientCreditState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        if (vendingMachine.getSelectedProduct().getPrice() == vendingMachine.getCredit() + coin.denomination) {
            if (vendingMachine.addCoinToCredit(coin)){
                vendingMachine.provisionCreditStackToDispenser();
                vendingMachine.dispenseSelectedProductToBucket();
                vendingMachine.undoProductSelection();
                vendingMachine.setCurrentState(vendingMachine.getNoCreditNoProductSelectedState());
            }
        }
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        Product prev = vendingMachine.getSelectedProduct();
        try {
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.displayProductPrice(shelfNumber);
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(String.format("%s, [%s] %s: %.2f$",
                VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber),
                prev.provideType(),
                VendingMachineMessages.PENDING.label,
                vendingMachine.calculatePendingBalance()/100.0)
            );
        }
    }

    @Override
    public void cancel() {
        vendingMachine.returnAllCreditToBucket();
        vendingMachine.setCurrentState(vendingMachine.getNoCreditNoProductSelectedState());
    }
}
