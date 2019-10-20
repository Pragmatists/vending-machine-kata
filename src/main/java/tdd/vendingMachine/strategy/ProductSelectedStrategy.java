package tdd.vendingMachine.strategy;

import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.cash.register.ICashBox;
import tdd.vendingMachine.display.IDisplay;

import java.util.Stack;

public class ProductSelectedStrategy extends VendingMachineStrategyBase {

    public boolean insertCoinForCurrentRequest(IDisplay display, ICashBox cashBox, Coin coin) {
        if (cashBox.isValidCoin(coin)) {
            cashBox.addToCurrentRequestPocket(coin);
            return true;
        }
        display.showInvalidCoinFormatMessage();
        return false;
    }

    public void cancelRequest(IDisplay display, ICashBox cashBox) {
        Stack<Coin> currentRequestPocket = cashBox.getCurrentRequestPocket();
        while (!currentRequestPocket.isEmpty()) {
            display.showReturnCoinMessage(currentRequestPocket.pop());
        }
        display.showRequestCanceledMessage();
    }
}
