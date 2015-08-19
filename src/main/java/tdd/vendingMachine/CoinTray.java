package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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

    public List<Coin> returnCoins() {
        List<Coin> coins = insertedCoins;
        insertedCoins = new ArrayList<Coin>();
        setChanged();
        notifyObservers();
        return coins;
    }
}
