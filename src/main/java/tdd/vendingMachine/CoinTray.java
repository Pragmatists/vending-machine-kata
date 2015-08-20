package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author macko
 * @since 2015-08-19
 */
public class CoinTray extends Observable {

    private List<Coin> insertedCoins;

    public CoinTray() {
        insertedCoins = new ArrayList<Coin>();
    }

    public void putCoin(Coin coin) {
        insertedCoins.add(coin);
        setChanged();
        notifyObservers();
    }

    public BigDecimal getInsertedAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (Coin coin : insertedCoins) {
            amount = amount.add(coin.getValue());
        }
        return amount;
    }

    public List<Coin> returnInsertedCoins() {
        List<Coin> coins = takeAllInsertedCoins();
        setChanged();
        notifyObservers();
        return coins;
    }

    public List<Coin> takeAllInsertedCoins() {
        List<Coin> coins = insertedCoins;
        insertedCoins = new ArrayList<Coin>();
        return coins;
    }

    public void giveChange(List<Coin> coins) {
        for (Coin coin : coins) {
            System.out.println("Giving change of: " + coin.getValue());
        }
    }
}
