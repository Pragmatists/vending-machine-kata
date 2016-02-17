package tdd.vendingMachine.Domain;

import org.slf4j.Logger;
import tdd.vendingMachine.Service.CoinChanger;
import tdd.vendingMachine.Service.Utils;

import java.time.Instant;
import java.util.*;

/**
 * This class deals with coin-storage, and the status of current "buy-transaction"
 */
public class CoinRepo {
    private Map<Integer,Integer> coins;

    //Registers physical actions of the coinRepo
    private static Logger log = org.slf4j.LoggerFactory.getLogger(CoinRepo.class);

    public CoinRepo(Collection<Integer> nominals) throws RuntimeException {
        coins = new HashMap<>();
        for(Integer i : nominals) {
            if (coins.containsKey(i))
                throw new RuntimeException(Error.INVALID_COIN_NOMINAL.toString());
            if (i<=0)
                throw new RuntimeException(Error.INVALID_COIN_NOMINAL.toString());
            coins.put(i, 0);
        }
    }

    //Admin operation: insert `howMany` coins of selected nominal (can withdraw).
    public void addCoins(int nominal, int howMany) throws RuntimeException {
        if (!coins.containsKey(nominal))
            throw new RuntimeException(Error.INVALID_COIN_NOMINAL.toString());
        if (coins.get(nominal)+howMany<0)
            throw new RuntimeException(Error.NEGATIVE_NUMBER_OF_COINS.toString());
        coins.put(nominal, coins.get(nominal) + howMany);
        log.info(" Stored " + howMany + " coins of nominal:" + nominal);
    }

    //Single coin inserted into the CoinRepo
    public void insertCoin(int nominal) throws RuntimeException {
        if (!coins.containsKey(nominal))
            throw new RuntimeException(Error.INVALID_COIN_NOMINAL.toString());
        coins.put(nominal, coins.get(nominal) + 1);
        log.info(" Accepted coin:" + nominal);
    }

    public Map<Integer,Integer> getCoins() {
        return coins;
    }


    /**
     * Disburses the coins asked for in `toGive`.
     * User must make sure these coins are in the repo (else: RuntimeException thrown)
     *
     * @param toGive    : nominals of coins to be disbursed
     * @throws RuntimeException (TOO_FEW_COINS_FOR_DISBURSE_ORDER)
     */
    public void disburseCoins(List<Integer> toGive) throws RuntimeException {
        for(int i : toGive) if (!coins.containsKey(i))
                throw new RuntimeException(Error.INVALID_COIN_NOMINAL.toString());

        Map<Integer, Integer> toGiveAsMap = new HashMap<>();
        for(int i : toGive) {
            toGiveAsMap.put(i, toGiveAsMap.getOrDefault(i, 0) + 1);
        }

        for(int coin : toGiveAsMap.keySet()) {
            if (coins.getOrDefault(coin,0) < toGiveAsMap.get(coin))
                throw new RuntimeException(Error.TOO_FEW_COINS_FOR_DISBURSE_ORDER.toString());
        }

        //here it is guaranteed that whole operation will be possible
        for(int nom : toGiveAsMap.keySet())
            coins.put(nom, coins.get(nom) - toGiveAsMap.get(nom));
        log.info("Disbursed coins:" + toGive);
    }

    public int getMoneySumStored(){
        return Utils.sumMap(coins);
    }
}
