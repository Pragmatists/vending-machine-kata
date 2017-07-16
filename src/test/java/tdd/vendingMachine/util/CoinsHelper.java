package tdd.vendingMachine.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public class CoinsHelper {

    public static List<Integer> listWithCoins(Integer... coins) {
        List<Integer> result = new ArrayList<>(coins.length);
        Collections.addAll(result, coins);
        return result;
    }

}
