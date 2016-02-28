package tdd.vendingMachine.external_interface;

import tdd.vendingMachine.domain.Coin;

import java.util.List;

public interface CoinTray {

    void disposeInsertedCoins(List<Coin> coins);
}
