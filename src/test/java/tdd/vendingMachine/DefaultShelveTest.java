package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.shelve.DefaultShelve;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by okraskat on 06.02.16.
 */
public class DefaultShelveTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenProductsAreNull() throws Exception {
        new DefaultShelve<>(null, new BigDecimal("0.0"));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenProductPriceIsNull() throws Exception {
        new DefaultShelve<>(new ArrayList<>(), null);
    }

    @Test
    public void shouldReturnProducts() throws Exception {
        //given
        List<Product> products = new ArrayList<>();
        DefaultShelve<Product> shelve = new DefaultShelve<>(products, new BigDecimal("0.0"));
        //when
        List<Product> returnedProducts = shelve.getProducts();
        //then
        assertEquals(products, returnedProducts);
    }

    @Test
    public void shouldReturnProductPrice() throws Exception {
        //given
        BigDecimal price = new BigDecimal("0.1");
        DefaultShelve shelve = new DefaultShelve<>(new ArrayList<>(), price);
        //when
        BigDecimal returnedPrice = shelve.getProductPrice();
        //then
        assertEquals(price, returnedPrice);
    }
}
