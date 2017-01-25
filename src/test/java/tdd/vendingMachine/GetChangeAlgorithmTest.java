package tdd.vendingMachine;

import org.junit.Test;

import java.util.EnumMap;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by dzalunin on 2017-01-25.
 */
public class GetChangeAlgorithmTest {

    @Test
    public void testGetChangeSuccess() throws Exception {
        Charger charger = new Charger();
        charger.supplyMoney(Denomination.FIVE, 1);
        charger.supplyMoney(Denomination.TWO, 1);
        charger.supplyMoney(Denomination.ONE, 1);
        charger.supplyMoney(Denomination.FIFTY_CENTS, 7);
        charger.supplyMoney(Denomination.TWENTY_CENTS, 2);
        charger.supplyMoney(Denomination.TEN_CENTS, 4);

        EnumMap<Denomination, Integer> change = charger.getChange(440);

        EnumMap<Denomination, Integer> expected = new EnumMap<Denomination, Integer>(Denomination.class);
        expected.put(Denomination.TWO, 1);
        expected.put(Denomination.ONE, 1);
        expected.put(Denomination.FIFTY_CENTS, 2);
        expected.put(Denomination.TWENTY_CENTS, 2);
        assertThat(change).isEqualTo(expected);
    }

    @Test(expected = InsuffiecientMoenyForChange.class)
    public void testGetChangeInsuffiecientMoneyException() throws Exception {
        Charger charger = new Charger();
        charger.supplyMoney(Denomination.FIVE, 1);
        charger.supplyMoney(Denomination.TWO, 1);
        charger.supplyMoney(Denomination.ONE, 1);
        charger.getChange(20);
    }

}
