package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.util.CoinsHelper;

import static org.junit.Assert.*;

/**
 * @author Yevhen Sukhomud
 */
public class KingstonMemoryCardTest {

    private KingstonMemoryCard memory = new KingstonMemoryCard();

    @Before
    public void setUp() throws Exception {
        memory.clear();
    }

    @Test
    public void productIndex_shouldReturnProductIndex() throws Exception {
        // given
        int index = 1;
        Product product = new Product("gum", 2);
        // when
        memory.remember(product, index);
        // then
        assertEquals(index, memory.productIndex());
    }

    @Test
    public void price_shouldRememberPrice() throws Exception {
        // given
        int price = 2;
        Product product = new Product("gum", price);
        // when
        memory.remember(product, 1);
        // then
        assertEquals(price, memory.price(), 0);
    }

    @Test
    public void clear_shouldClearAllDataInMemory() throws Exception {
        // given
        memory.remember(new Product("gum", 2), 1);
        memory.remember(0.5);
        // when
        memory.clear();
        // then
        assertFalse(memory.hasSelectedProduct());
        assertFalse(memory.hasInsertedMoney());
        assertTrue(memory.insertedMoney().isEmpty());
        assertEquals(0, memory.price(), 0);
    }

    @Test
    public void rememberProduct_shouldStoreProductInMemory() throws Exception {
        // given
        Product product = new Product("gum", 2);
        // when
        memory.remember(product, 1);
        // then
        assertTrue(memory.hasSelectedProduct());
    }

    @Test
    public void rememberProductPrice_shouldStorePriceInMemory() throws Exception {
        // given
        int price = 2;
        Product product = new Product("gum", price);
        // when
        memory.remember(product, 1);
        // then
        assertEquals(price, memory.price(), 0);
    }

    @Test
    public void rememberMoney_shouldRememberRememberedMoney() throws Exception {
        // when
        memory.remember(0.1);
        // then
        assertTrue(memory.hasInsertedMoney());
    }

    @Test
    public void rememberMoney_shouldStoreAllRememberedMoney() throws Exception {
        // when
        memory.remember(0.1);
        memory.remember(0.1);
        memory.remember(0.2);
        // then
        assertEquals(CoinsHelper.listWithCoins(0.1, 0.1, 0.2), memory.insertedMoney());
    }

    @Test
    public void hasInsertedMoney_shouldReturnTrueWhenRemembered() throws Exception {
        // given
        double coin = 1;
        // when
        memory.remember(coin);
        // then
        assertTrue(memory.hasInsertedMoney());
    }

    @Test
    public void hasInsertedMoney_shouldReturnFalseWhenNotRemembered() throws Exception {
        // when
        // memory is clean
        // then
        assertFalse(memory.hasInsertedMoney());
    }

    @Test
    public void hasSelectedProduct_shouldReturnTrueWhenRemembered() throws Exception {
        // given
        Product product = new Product("gum", 2);
        // when
        memory.remember(product, 1);
        // then
        assertTrue(memory.hasSelectedProduct());
    }

    @Test
    public void hasSelectedProduct_shouldReturnFalseWhenNotRemembered() throws Exception {
        // when
        // memory is clean
        // then
        assertFalse(memory.hasSelectedProduct());
    }

}
