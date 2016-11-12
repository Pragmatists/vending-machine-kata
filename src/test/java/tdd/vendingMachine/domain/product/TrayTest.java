package tdd.vendingMachine.domain.product;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import tdd.vendingMachine.domain.product.Products;
import tdd.vendingMachine.domain.product.Tray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrayTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void should_return_product_count() {
        Tray tray = new Tray(Products.COCA_COLA_0_33, 10);

        assertEquals(10, tray.getProductCount());
        assertFalse(tray.isEmpty());
    }

    @Test
    public void should_return_0_for_count_and_true_for_is_empty_when_tray_is_empty() {
        Tray tray = new Tray(Products.COCA_COLA_0_33, 0);

        assertEquals(0, tray.getProductCount());
        assertTrue(tray.isEmpty());
    }

    @Test
    public void should_remove_product_from_tray_when_dispensed() {
        Tray tray = new Tray(Products.COCA_COLA_0_33, 1);

        assertEquals(1, tray.getProductCount());
        assertFalse(tray.isEmpty());

        tray.removeProduct(1);

        assertEquals(0, tray.getProductCount());
        assertTrue(tray.isEmpty());
    }

    @Test
    public void should_throw_exception_on_attempt_to_remove_more_products_than_available() {
        Tray tray = new Tray(Products.COCA_COLA_0_33, 1);

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Not enough products on tray");

        tray.removeProduct(2);
    }
}
