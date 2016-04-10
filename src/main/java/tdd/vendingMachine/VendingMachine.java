package tdd.vendingMachine;

import com.google.common.annotations.VisibleForTesting;
import tdd.vendingMachine.calculator.VendingMachineCalculator;
import tdd.vendingMachine.display.VendingMachineDisplay;
import tdd.vendingMachine.domain.Coins;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.ProductWithChange;
import tdd.vendingMachine.inventory.CoinBin;
import tdd.vendingMachine.inventory.Shelfs;

public class VendingMachine {

    @VisibleForChangeState final VendingMachineDisplay display;
    @VisibleForChangeState final Shelfs shelfs;
    @VisibleForChangeState final CoinBin coinBin;
    @VisibleForChangeState final VendingMachineCalculator calculator;

    @VisibleForChangeState Coins credit = Coins.empty();
    @VisibleForChangeState VendingMachineState currentState;

    VendingMachine(VendingMachineDisplay display, Shelfs shelfs, CoinBin coinBin, VendingMachineCalculator calculator) {
        this.display = display;
        this.shelfs = shelfs;
        this.coinBin = coinBin;
        this.calculator = calculator;
    }

    public void selectShelf(int shelfNumber) {
        currentState.selectShelf(shelfNumber);
    }

    public void insertCoin(Coins coins) {
        currentState.insertCoin(coins);
    }

    public ProductWithChange dispense() {
        return currentState.dispense();
    }

    public Coins returnCoins() {
        return currentState.returnCoins();
    }

    public String showDisplay() {
        return display.getDisplayableMessage();
    }

    @VisibleForTesting
    boolean hasProduct(Product product) {
        return shelfs.isProductExistOnAnyShelf(product);
    }

    @VisibleForTesting
    Coins getAvailableCoinsAtBin() {
        return Coins.of(coinBin.getCoins());
    }

}
