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

    private void sellProduct() {
        vendingMachine.provisionCreditStackToDispenser();
        vendingMachine.dispenseSelectedProductToBucket();
        vendingMachine.undoProductSelection();
        vendingMachine.setCurrentState(vendingMachine.getNoCreditNoProductSelectedState());
    }

    @Override
    public void insertCoin(Coin coin) {
        int compare = Integer.compare(vendingMachine.getSelectedProduct().getPrice(), vendingMachine.getCredit() + coin.denomination);
        switch (compare) {
            case -1:
                vendingMachine.addCoinToCredit(coin);
                System.out.println("le alcanza dele cambio");
                break;
            case 1:
                vendingMachine.addCoinToCredit(coin);
                System.out.println("no le alcanza");
                break;
            default:
                if (vendingMachine.addCoinToCredit(coin)){
                    this.sellProduct();
                }
                break;
        }
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {
        Product prev = vendingMachine.getSelectedProduct();
        try {
            vendingMachine.selectProductGivenShelfNumber(shelfNumber);
            vendingMachine.displayProductPrice(shelfNumber);
            if (vendingMachine.getSelectedProduct().getPrice() == vendingMachine.getCredit()) {

            }
        } catch (NoSuchElementException nse) {
            logger.error(nse);
            vendingMachine.showMessageOnDisplay(String.format("%s, [%s] %s: %s",
                VendingMachineMessages.buildWarningMessageWithSubject(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label, shelfNumber),
                prev.provideType(),
                VendingMachineMessages.PENDING.label,
                VendingMachineMessages.provideCashToDisplay(vendingMachine.calculatePendingBalance()))
            );
        }
    }

    @Override
    public void cancel() {
        vendingMachine.showMessageOnDisplay(VendingMachineMessages.CANCEL.label);
        vendingMachine.returnAllCreditToBucket();
        vendingMachine.setCurrentState(vendingMachine.getNoCreditNoProductSelectedState());
    }
}
