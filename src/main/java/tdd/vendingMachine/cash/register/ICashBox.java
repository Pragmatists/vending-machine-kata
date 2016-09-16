package tdd.vendingMachine.cash.register;

import tdd.vendingMachine.cash.coin.Coin;

import java.util.List;
import java.util.Stack;


public interface ICashBox {
    boolean isValidCoin(Coin coin);

    void addToCurrentRequestPocket(Coin coin);

    void addToCashBoxPocket(Coin coin);

    Double getInsertedCoinsValueForCurrentRequest();

    Stack<Coin> getCurrentRequestPocket();

    boolean isAbleToReturnChangeFor(Double currentRequest);

    List<Coin> withdrawCoinsFor(double value);

    void depositCurrentRequestCoins();
}
