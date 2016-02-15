package tdd.vendingMachine.Service;

import java.util.List;

/**
 * Service dealing with the issue of change-calculation.
 * - must be correct
 * - must be fast (many coins, large sums)
 * - must be flexible (strategies for the machine not to run out of coins after few transactions)
 */
public interface CoinChanger {

    /**
     * Prototype of a method to calculate change.
     * Takes a set of avilable coins, and the sum which needs to be paid-out,
     * and returns either null (if the sum cannot be paid) or the "best" distribution
     * of coins to be returned.
     */
    //
    //null if sum cannot be paid with avilableCoins, else list of coins used
    List<Integer> pay(Iterable<Integer> avilableCoins, int sum);

}
