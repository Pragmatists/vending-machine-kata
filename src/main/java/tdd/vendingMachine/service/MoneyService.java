package tdd.vendingMachine.service;

import tdd.vendingMachine.service.exception.CoinNotSupportedException;

public class MoneyService implements IMoneyService {

    @Override
    public SupportedCoins getCoinType(float denomination) throws CoinNotSupportedException {
        return SupportedCoins.createSupportedCoins(denomination);
    }
}
