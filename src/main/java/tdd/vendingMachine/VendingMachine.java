package tdd.vendingMachine;

import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.cash.register.CashBox;
import tdd.vendingMachine.cash.register.ICashBox;
import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.display.IDisplay;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.request.Request;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;
import tdd.vendingMachine.shelf.IShelf;
import tdd.vendingMachine.shelf.Shelfs;
import tdd.vendingMachine.strategy.IVendingMachineStrategies;
import tdd.vendingMachine.strategy.IVendingMachineStrategy;
import tdd.vendingMachine.strategy.VendingMachineStateStrategies;

import java.math.BigDecimal;

import static tdd.vendingMachine.util.BigDecimalUtil.createBigDecimalWithPrecision;

public class VendingMachine implements IVendingMachine {

    VendingMachineState state = VendingMachineState.WAITING_FOR_SELECT_PRODUCT;
    private ICashBox cashBox = new CashBox();
    private IDisplay display = new Display();
    Shelfs shelfs = new Shelfs();
    Request currentRequest;
    IVendingMachineStrategies vendingMachineStateStrategies = new VendingMachineStateStrategies();

    @Override
    public void insertCoinForCurrentRequest(Coin coin) {
        boolean isCoinInserted = getStrategyForCurrentState().insertCoinForCurrentRequest(display, cashBox, coin);
        if (isCoinInserted) {
            tryToFinalizeRequest();
        } else {
            returnCoin(coin);
        }
    }

    void tryToFinalizeRequest() {
        Double reamingValue = countReamingValueForCurrentRequest();
        if (reamingValue <= 0) {
            finalizeRequest(Math.abs(reamingValue));
        } else {
            display.showRemainingValueForSelectedProductMessage(reamingValue);
        }
    }

    void finalizeRequest(Double changeValue) {
        if (cashBox.isAbleToReturnChangeFor(changeValue)) {
            returnProduct();
            cashBox.depositCurrentRequestCoins();
            returnRestOfMoney(changeValue);
        } else {
            display.showCantReturnChangeMessage();
            getStrategyForCurrentState().cancelRequest(display, cashBox);
        }
        state = VendingMachineState.WAITING_FOR_SELECT_PRODUCT;
        currentRequest = null;
    }

    @Override
    public void cancelRequest() {
        getStrategyForCurrentState().cancelRequest(display, cashBox);
        state = VendingMachineState.WAITING_FOR_SELECT_PRODUCT;
    }

    Double countReamingValueForCurrentRequest() {
        BigDecimal insertedCoinsValueForCurrentRequest = createBigDecimalWithPrecision(cashBox.getInsertedCoinsValueForCurrentRequest());
        return createBigDecimalWithPrecision(getCurrentRequestPrice()).subtract(insertedCoinsValueForCurrentRequest).doubleValue();
    }

    Double getCurrentRequestPrice() {
        return currentRequest.getProduct().getPrice();
    }

    @Override
    public void insertCoinToCashBox(Coin coin) {
        getStrategyForCurrentState().insertCoinToCashBox(display, cashBox, coin);
    }

    @Override
    public void selectProduct(int shelfNumber) {
        IShelf shelf = shelfs.get(shelfNumber);
        Request selectProductRequest = getStrategyForCurrentState().selectProduct(display, shelf);
        if (selectProductRequest != null) {
            currentRequest = selectProductRequest;
            state = VendingMachineState.PRODUCT_SELECTED;
        }
    }

    void returnCoin(Coin coin) {
        display.showReturnCoinMessage(coin);
    }

    @Override
    public void insertProduct(int shelfNumber, Product product) throws CannotChangeShelfProductsTypeException {
        IShelf shelf = shelfs.get(shelfNumber);
        getStrategyForCurrentState().insertProduct(display, shelf, product);
    }

    void returnProduct() {
        display.showDropProductMessage(currentRequest.getProduct());
    }

    void returnRestOfMoney(Double reamingValue) {
        cashBox.withdrawCoinsFor(reamingValue).forEach(this::returnCoin);
    }

    @Override
    public void turnOnMachineSetUpState() {
        state = VendingMachineState.SET_UP_MACHINE;
    }

    @Override
    public void turnOfMachineSetUpState() {
        state = VendingMachineState.WAITING_FOR_SELECT_PRODUCT;
    }

    @Override
    public void displayMachineShelfsInformation() {
        for (IShelf shelf : shelfs.values()) {
            display.showShelfInformation(shelf);
        }
    }

    private IVendingMachineStrategy getStrategyForCurrentState() {
        return vendingMachineStateStrategies.get(state);
    }

}
