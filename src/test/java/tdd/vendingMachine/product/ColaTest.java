package tdd.vendingMachine.product;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by okraskat on 06.02.16.
 */
public class ColaTest {

    @Test
    public void shouldCost2() throws Exception {
        //when
        BigDecimal price = new Cola().getPrice();
        //then
        assertNotNull(price);
        assertEquals(new BigDecimal("2.0"), price);
    }

    @Test
    public void shouldReturnNameCola() throws Exception {
        //when
        String name = new Cola().getName();
        //then
        assertEquals("Cola", name);
    }

}
