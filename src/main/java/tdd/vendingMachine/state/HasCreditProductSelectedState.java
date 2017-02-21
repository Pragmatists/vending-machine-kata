package tdd.vendingMachine.state;

import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class HasCreditProductSelectedState implements State {

    public static final String label = "HAS CREDIT PRODUCT SELECTED";
    private final VendingMachine vendingMachine;

    public HasCreditProductSelectedState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        if (vendingMachine.getSelectedProduct().getPrice() == vendingMachine.getCredit() + coin.denomination) {
            if (vendingMachine.addCoinToCredit(coin)){
                vendingMachine.provisionCreditStackToDispenser();
                vendingMachine.dispenseSelectedProductToBucket();
                vendingMachine.undoProductSelection();
                vendingMachine.setCurrentState(vendingMachine.noCreditNoProductSelectedState);
            }
        }
    }

    @Override
    public void selectShelfNumber(int shelfNumber) {

    }

    @Override
    public void cancel() {
        vendingMachine.returnAllCreditToBucket();
        vendingMachine.setCurrentState(vendingMachine.getNoCreditNoProductSelectedState());
    }
}
