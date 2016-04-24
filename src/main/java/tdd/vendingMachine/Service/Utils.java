package tdd.vendingMachine.Service;

import java.util.List;
import java.util.Map;

/**
 * Helpers for tests.
 * (Help not to pollute Services)
 */
public class Utils {
    public static int sumList(List<Integer> coins) {
        int sum = 0;
        for(int i : coins) sum += i;
        return sum;
    }
    public static int sumMap(Map<Integer,Integer> coinStore) {
        int sum = 0;
        for(int nom : coinStore.keySet()) sum += coinStore.get(nom) * nom;
        return sum;
    }
}
