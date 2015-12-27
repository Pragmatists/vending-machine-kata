package tdd.vendingMachine.domain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public class Coins {

    private final List<Coin> coins;

    private Money moneyValueOfCoins;

    public static Coins of(List<Coin> coins) {
        return new Coins(coins);
    }

    public static Coins of(Coin... coins) {
        return new Coins(Lists.newArrayList(coins));
    }

    public static Coins of(Coins... coinsCollections) {
        List<Coin> allCoins = Lists.newArrayList();
        for (Coins coins : coinsCollections) {
            allCoins.addAll(coins.get());
        }
        return new Coins(allCoins);
    }

    public static Coins empty() {
        return new Coins(Lists.<Coin>newArrayList());
    }

    private Coins(List<Coin> coins) {
        this.coins = coins;
    }

    public Coins collect(Coins aCoins) {
        List<Coin> newCoins = Lists.newArrayList(this.coins);
        newCoins.addAll(aCoins.coins);
        return Coins.of(newCoins);
    }

    public int size() {
        return coins.size();
    }

    public Money asMoney() {
        if (moneyValueOfCoins == null) {
            Money coinsMoney = Money.ZERO;
            for (Coin coin : coins) {
                coinsMoney = coinsMoney.plus(coin.asMoney());
            }
            moneyValueOfCoins = coinsMoney;
        }
        return moneyValueOfCoins;
    }

    public List<Coin> get() {
        return ImmutableList.copyOf(coins);
    }

    @Override
    public String toString() {
        return coins.toString();
    }
}
