package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by okraskat on 11.02.16.
 */
public class ChangeCalculator {

    public boolean canGiveAChange(BigDecimal subtract, Map<BigDecimal, List<BigDecimal>> coins) {
        BigDecimal rest = new BigDecimal(subtract.toBigInteger());
        List<BigDecimal> keys = new ArrayList<>(coins.keySet());
        Collections.sort(keys);
        for (int i = keys.size() - 1; i >= 0; i--) {
            BigDecimal denomination = keys.get(i);
            if (rest.compareTo(denomination) >= 0 && !coins.get(denomination).isEmpty()) {
                for (BigDecimal coin : coins.get(denomination)) {
                    if (rest.subtract(coin).compareTo(BigDecimal.ZERO) >= 0) {
                        rest = rest.subtract(coin);
                    }
                }
            }
        }
        return rest.compareTo(BigDecimal.ZERO) == 0;
    }

    public  List<BigDecimal> getChange(BigDecimal subtract, Map<BigDecimal, List<BigDecimal>> coins) {
        List<BigDecimal> change = new ArrayList<>();
        BigDecimal rest = new BigDecimal(subtract.toBigInteger());
        for (BigDecimal denomination : coins.keySet()) {
            if(rest.compareTo(denomination) >= 0 && !coins.get(denomination).isEmpty()){
                addToChange(change, rest, coins.get(denomination));
            }
        }
        for (BigDecimal coin : change) {
            coins.get(coin).remove(coins.get(coin).size() - 1);
        }
        return change;
    }

    private void addToChange(List<BigDecimal> change, BigDecimal rest, List<BigDecimal> coins) {
        for (BigDecimal coin : coins) {
            if(rest.subtract(coin).compareTo(BigDecimal.ZERO) >= 0){
                rest = rest.subtract(coin);
                change.add(coin);
            }
        }
    }
}
