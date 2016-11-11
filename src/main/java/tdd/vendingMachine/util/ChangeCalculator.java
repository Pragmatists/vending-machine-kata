package tdd.vendingMachine.util;

import tdd.vendingMachine.domain.currency.Coins;

import java.util.EnumMap;

public class ChangeCalculator {

    public static EnumMap calculateChange(EnumMap<Coins, Integer> moneyBox, int change) {
        EnumMap<Coins, Integer> coinsToReturn = new EnumMap<Coins, Integer>(Coins.class) {{
            put(Coins.COIN_0_1, 0);
            put(Coins.COIN_0_2, 0);
            put(Coins.COIN_0_5, 0);
            put(Coins.COIN_1, 0);
            put(Coins.COIN_2, 0);
            put(Coins.COIN_5, 0);
        }};

        EnumMap<Coins, Integer> virtualMoneyBox = moneyBox.clone();

        for (Coins coin : Coins.values()) {
            while (true) {
                if (virtualMoneyBox.get(coin) <= 0 || change - coin.getValue() < 0) {
                    break;
                }
                coinsToReturn.put(coin, coinsToReturn.get(coin) + 1);
                virtualMoneyBox.put(coin, virtualMoneyBox.get(coin) - 1);
                change -= coin.getValue();
            }

            if (0 == change) {
                break;
            }
        }

        if (change != 0) {
            return null;
        }

        return coinsToReturn;
    }
}
