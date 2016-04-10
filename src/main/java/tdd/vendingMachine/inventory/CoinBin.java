package tdd.vendingMachine.inventory;

import com.google.common.collect.Lists;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Coins;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CoinBin {

    private final List<Coin> coins;

    public static CoinBin with(Coins aCoins) {
        return new CoinBin(aCoins);
    }

    private CoinBin(Coins aCoins) {
        this.coins = Lists.newArrayList(aCoins.get());
    }

    public void accept(Coins coins) {
        this.coins.addAll(coins.get());
    }

    public Coins take(Coins aCoins) {
        List<Coin> resultCoins = Lists.newArrayList();
        for (Coin coin : aCoins.get()) {
            if (this.coins.remove(coin)) {
                resultCoins.add(coin);
            }
        }
        return Coins.of(resultCoins);
    }

    public List<Coin> getCoins() {
        List<Coin> copyOfCoins = Lists.newArrayList(coins);
        Collections.sort(copyOfCoins, new MaxCoinCollectionComparator());
        return copyOfCoins;
    }

    private static final class MaxCoinCollectionComparator implements Comparator<Coin> {
        @Override
        public int compare(Coin coin1, Coin coin2) {
            return -1 * coin1.asMoney().compareTo(coin2.asMoney());
        }
    }
}
