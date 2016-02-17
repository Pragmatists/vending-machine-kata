package tdd.vendingMachine.Domain;

import tdd.vendingMachine.Service.CoinChanger;

import java.time.Instant;
import java.util.*;

/**
 * This class deals with coin-storage, and the status of current "buy-transaction"
 */
public class CoinRepo {
    private Map<Integer,Integer> coins;
    private List<String> internalLog;   //registers physical actions of the storage

    public CoinRepo(Collection<Integer> nominals) throws RuntimeException {
        coins = new HashMap<>();
        for(Integer i : nominals) {
            if (coins.containsKey(i))
                throw new RuntimeException(Error.INVALID_COIN_NOMINAL.toString());
            if (i<=0)
                throw new RuntimeException(Error.INVALID_COIN_NOMINAL.toString());
            coins.put(i, 0);
        }
        this.internalLog = new ArrayList<>();
    }

    //admin inserts `howMany` coins of selected nominal (can withdraw)
    public void addCoins(int nominal, int howMany) throws RuntimeException {
        if (!coins.containsKey(nominal))
            throw new RuntimeException(Error.INVALID_COIN_NOMINAL.toString());
        if (coins.get(nominal)+howMany<0)
            throw new RuntimeException(Error.NEGATIVE_NUMBER_OF_COINS.toString());
        coins.put(nominal, coins.get(nominal) + howMany);
    }

    //client inserts single coin
    public void insertCoin(int nominal) throws RuntimeException {
        if (!coins.containsKey(nominal))
            throw new RuntimeException(Error.INVALID_COIN_NOMINAL.toString());
        coins.put(nominal, coins.get(nominal) + 1);
    }


    public Set<Integer> getNominals() {
        return coins.keySet();
    }

    public Map<Integer,Integer> getCoins() {
        return coins;
    }

    public void disburseCoins(List<Integer> toGive) throws RuntimeException {
        for(int i : toGive)
            if (!coins.containsKey(i))
                throw new RuntimeException(Error.INVALID_COIN_NOMINAL.toString());
        Map<Integer, Integer> sorted = new HashMap<>();
        for(int i : toGive) {
            sorted.put(i, sorted.getOrDefault(i, 0) + 1);
        }
        for(int nom : sorted.keySet()) {
            if (coins.getOrDefault(nom,0) < sorted.get(nom))
                throw new RuntimeException(Error.TOO_FEW_COINS_FOR_DISBURSE_ORDER.toString());
        }
        for(int nom : sorted.keySet())
            coins.put(nom, coins.get(nom) - sorted.get(nom));

        internalLog.add(Instant.now().toString() + " disbursed coins:" + toGive);
    }

    public int getMoneySumStored(){
        return 0;
    }
}
