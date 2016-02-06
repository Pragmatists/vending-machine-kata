package tdd.vendingMachine;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by okraskat on 06.02.16.
 */
public class ShelveTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenProductsAreNull() throws Exception {
        new Shelve<>(null);
    }

    @Test
    public void shouldReturnProducts() throws Exception {
        //given
        List<Product> products = new ArrayList<>();
        Shelve<Product> shelve = new Shelve<>(products);
        //when
        List<Product> returnedProducts = shelve.getProducts();
        //then
        assertEquals(products, returnedProducts);
    }
}
