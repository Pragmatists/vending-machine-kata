package tdd.vendingMachine.Domain;

import tdd.vendingMachine.Service.CoinChanger;

import java.time.Instant;
import java.util.*;

/**
 * This class deals with coin-storage, and the status of current "buy-transaction"
 */
public class CoinRepo {
    Map<Integer,Integer> coins;
    List<String> internalLog;   //registers physical actions of the storage

    CoinChanger changer;    //tells if a sum `sum` can be constructed (and how best)

    public CoinRepo(Collection<Integer> nominals) throws RuntimeException {
        coins = new HashMap<>();
        for(Integer i : nominals) {

            coins.put(i, 0);
        }
        this.internalLog = new ArrayList<>();
    }

    //admin inserts `howMany` coins of selected nominal (can withdraw)
    public void addCoins(int nominal, int howMany) throws RuntimeException {
        //transaction cannot be running
    }

    //client inserts single coin
    public void insertCoin(int nominal) throws RuntimeException {
        //
    }


    public Set<Integer> getNominals() {
        return coins.keySet();
    }

    public Map<Integer,Integer> getCoins() {
        return coins;
    }

    public void disburseCoins(List<Integer> coins) throws RuntimeException {
        internalLog.add(Instant.now().toString());
    }



}
