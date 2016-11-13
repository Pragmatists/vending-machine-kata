package tdd.vendingMachine.util;

import tdd.vendingMachine.domain.money.Coins;
import tdd.vendingMachine.domain.money.MoneyBox;

import java.util.EnumMap;
import java.util.Map;

public class ChangeCalculator {

    public static MoneyBox calculateChange(MoneyBox moneyBox, final int change) {
        MoneyBox coinsToReturn = new MoneyBox();

        if (change == 0) {
            return coinsToReturn;
        }

        MoneyBox virtualMoneyBox = new MoneyBox(moneyBox);

        int changeLeft = change;
        for (Coins coin : Coins.values()) {
            while (true) {
                if (virtualMoneyBox.getCoinCount(coin) <= 0 || changeLeft - coin.getValue() < 0) {
                    break;
                }
                coinsToReturn.insert(coin, 1);
                virtualMoneyBox.remove(coin, 1);
                changeLeft -= coin.getValue();
            }

            if (0 == changeLeft) {
                break;
            }
        }

        if (changeLeft != 0) {
            return null;
        }

        return coinsToReturn;
    }
}
