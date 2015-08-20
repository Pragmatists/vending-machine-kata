package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author macko
 * @since 2015-08-20
 */
public class CoinVault {

    private EnumMap<Coin, Integer> coins;

    public CoinVault() {
        coins = new EnumMap<Coin, Integer>(Coin.class);
        for (Coin coin : Coin.values()) {
            coins.put(coin, 0);
        }
    }

    public void add(List<Coin> coinsToAdd) {
        for (Coin coin : coinsToAdd) {
            coins.put(coin, coins.get(coin) + 1);
        }
    }

    public List<Coin> getCoinsToChange(BigDecimal changeAmount) {
        List<Coin> coinsToReturn = new ArrayList<Coin>();

        for (Map.Entry<Coin, Integer> entry : coins.entrySet()) {
            while (entry.getKey().getValue().compareTo(changeAmount) <= 0 && entry.getValue() > 0) {
                changeAmount = changeAmount.subtract(entry.getKey().getValue());
                coinsToReturn.add(entry.getKey());
                entry.setValue(entry.getValue() - 1);
            }
        }

        if (changeAmount.signum() > 0) {
            add(coinsToReturn);
            throw new NoCoinsToChangeException();
        }

        return coinsToReturn;
    }
}
