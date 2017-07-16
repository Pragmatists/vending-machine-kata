package tdd.vendingMachine.util;

import java.util.List;

/**
 * @author Yevhen Sukhomud
 */
public class CoinsCalculator {

    public static int calculateSum(List<Integer> money) {
        return money.stream().mapToInt(Integer::intValue).sum();
    }

    public static int calculateChange(int given, int price) {
        return given - price;
    }

}
