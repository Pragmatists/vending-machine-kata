package tdd.vendingMachine.util;

import tdd.vendingMachine.domain.currency.Coins;

import java.util.EnumMap;

public class ChangeCalculator {
    public static EnumMap calculateChange(EnumMap moneyBox, int change) {
        return new EnumMap<Coins, Integer>(Coins.class);
    }
}
