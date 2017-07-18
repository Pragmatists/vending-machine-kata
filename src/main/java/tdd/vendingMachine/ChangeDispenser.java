package tdd.vendingMachine;

import tdd.vendingMachine.dto.Coin;

import java.util.List;

interface ChangeDispenser {
    void ejectChange(List<Coin> change);

    void ejectChange(Coin coin);

    List<Coin> retrieveCoins();
}
