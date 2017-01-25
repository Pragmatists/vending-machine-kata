package tdd.vendingMachine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.EnumMap;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


/**
 * Created by dzalunin on 2017-01-25.
 */
@RunWith(Parameterized.class)
public class ChargerTest {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {Denomination.FIFTY_CENTS, -10},
                {Denomination.FIFTY_CENTS, 0},
                {null, 0}
        };
    }

    private int quantity;
    private Denomination denomination;

    public ChargerTest(Denomination denomination, int quantity) {
        this.quantity = quantity;
        this.denomination = denomination;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSupplyMoneyNegativeQuantity() throws Exception {
        Charger charger = new Charger();
        charger.supplyMoney(denomination, quantity);
    }

    @Test
    public void testSupplyMoneySuccess() throws Exception {
        Charger charger = new Charger();
        charger.supplyMoney(Denomination.FIFTY_CENTS, 3);

        long actual = charger.count(Denomination.FIFTY_CENTS);
        assertThat(actual).isEqualTo(3);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCountPassNullException() throws Exception {
        Charger charger = new Charger();
        charger.count(null);
    }

}