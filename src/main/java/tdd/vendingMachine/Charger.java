package tdd.vendingMachine;

import java.util.EnumMap;
import java.util.Locale;

/**
 * Created by dzalunin on 2017-01-25.
 * <p>
 * This class is responsible for:
 * 1. storing collected coins
 * 2. supply new coins to cassette
 * 3. giving the change
 */
public class Charger {

    private EnumMap<Denomination, Long> cassette = new EnumMap<>(Denomination.class);

    public void supplyMoney(Denomination denomination, long quantity) {
        Preconditions.checkArgument(denomination != null, "denomination can't null");
        Preconditions.checkArgument(quantity > 0, "quantity should be greater 0");
        cassette.put(denomination, quantity);
    }

    public long count(Denomination denomination) {
        Preconditions.checkArgument(denomination != null, "denomination can't null");
        return cassette.get(denomination);
    }

    public EnumMap<Denomination, Integer> getChange(long toReturn) throws InsuffiecientMoenyForChange {
        //denomination in sorted order
        Denomination[] denominations = new Denomination[]{
                Denomination.FIVE,
                Denomination.TWO,
                Denomination.ONE,
                Denomination.FIFTY_CENTS,
                Denomination.TWENTY_CENTS,
                Denomination.TEN_CENTS,
        };
        EnumMap<Denomination, Integer> change = new EnumMap<Denomination, Integer>(Denomination.class);
        long remain = toReturn;
        for (int i = 0; i < denominations.length; i++) {
            Long count = cassette.get(denominations[i]);
            if (count == null || remain < denominations[i].value()) {
                continue;
            }

            long changeCoins = remain / denominations[i].value();
            if (changeCoins > count) {
                changeCoins = count;
            }

            remain = remain - changeCoins * denominations[i].value();
            change.put(denominations[i], (int) changeCoins);
            cassette.put(denominations[i], count - changeCoins);
        }
        if (remain != 0) {
            throw new InsuffiecientMoenyForChange("Insufficient moeny for make change for " + toReturn);
        }
        return change;
    }

}
