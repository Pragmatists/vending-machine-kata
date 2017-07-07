package tdd.vendingMachine.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public class CoinsHelper {

    public static List<Double> listWithCoins(double... coins) {
        List<Double> result = new ArrayList<>(coins.length);
        for (double coin : coins) {
            result.add(coin);
        }
        return result;
    }

}
