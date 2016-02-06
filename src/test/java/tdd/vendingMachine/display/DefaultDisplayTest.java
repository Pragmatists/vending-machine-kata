package tdd.vendingMachine.display;

import org.junit.Test;
import tdd.vendingMachine.Product;
import tdd.vendingMachine.errors.ShelveNotFound;
import tdd.vendingMachine.shelve.DefaultShelve;
import tdd.vendingMachine.shelve.Shelve;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by okraskat on 06.02.16.
 */
public class DefaultDisplayTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenShelvesAreNull() throws Exception {
        //when
        new DefaultDisplay(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenShelveNumberIsNull() throws Exception {
        //given
        DefaultDisplay defaultDisplay = new DefaultDisplay(new HashMap<>());
        //when
        defaultDisplay.getProductPriceByShelveNumber(null);
    }

    @Test(expected = ShelveNotFound.class)
    public void shouldThrowExceptionWhenShelveNotFound() throws Exception {
        //given
        DefaultDisplay defaultDisplay = new DefaultDisplay(new HashMap<>());
        //when
        defaultDisplay.getProductPriceByShelveNumber(1);
    }

    @Test
    public void shouldReturnProductPriceByShelveNumber() throws Exception {
        //given
        Map<Integer, Shelve> shelves = new HashMap<>();
        BigDecimal price = new BigDecimal("10.0");
        shelves.put(1, new DefaultShelve<>(new ArrayList<Product>(), price));
        DefaultDisplay defaultDisplay = new DefaultDisplay(shelves);
        //when
        BigDecimal returnedPrice = defaultDisplay.getProductPriceByShelveNumber(1);
        //then
        assertNotNull(returnedPrice);
        assertEquals(price, returnedPrice);
    }
}
