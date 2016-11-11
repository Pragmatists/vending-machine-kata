package tdd.vendingMachine.domain;

import tdd.vendingMachine.domain.currency.Coins;

import java.util.EnumMap;

public class MoneyBox {

    private EnumMap<Coins, Integer> coins = new EnumMap<Coins, Integer>(Coins.class) {{
        put(Coins.COIN_0_1, 0);
        put(Coins.COIN_0_2, 0);
        put(Coins.COIN_0_5, 0);
        put(Coins.COIN_1, 0);
        put(Coins.COIN_2, 0);
        put(Coins.COIN_5, 0);
    }};


    public int getCoinCount(Coins coins) {
        return 0;
    }

    public MoneyBox insert(Coins coin, int count) {
        return this;
    }

    public boolean isEmpty(Coins coin01) {
        return false;
    }

    public MoneyBox remove(Coins coin, int count) {
        return this;
    }
}
