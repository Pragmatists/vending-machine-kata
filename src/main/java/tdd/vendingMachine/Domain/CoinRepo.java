package tdd.vendingMachine.Domain;

import tdd.vendingMachine.Service.CoinChanger;

import java.util.List;
import java.util.Set;

/**
 * This class deals with coin-storage, and the status of current "buy-transaction"
 */
public class CoinRepo {
    Set<Integer> nominals;
    List<Integer> coins;    //available coins
    List<String> internalLog;   //registers physical actions of the storage

    CoinChanger changer;    //tells if a sum `sum` can be constructed (and how best)

    //admin
    public void addCoins(int nominal, int howMany) {
        //transaction cannot be running
    }

    //client
    public void insertCoin(int nominal) throws RuntimeException {
        //
    }

    public void disburseCoins(List<Integer> coins) throws RuntimeException {

    }



}
