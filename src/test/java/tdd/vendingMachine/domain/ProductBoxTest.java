package tdd.vendingMachine.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class ProductBoxTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void should_return_zero_tray_count_for_empty_box() {
        ProductBox box = new ProductBox();

        assertEquals(Integer.valueOf(0), box.getAvailableTrays());
    }

    @Test
    public void should_return_count_of_installed_trays() {
        ProductBox box = new ProductBox();
        box.addTray(new Tray(Products.COCA_COLA_0_33, 1));
        box.addTray(new Tray(Products.COCA_COLA_0_33, 1));
        box.addTray(new Tray(Products.COCA_COLA_0_33, 1));
        box.addTray(new Tray(Products.COCA_COLA_0_33, 1));

        assertEquals(Integer.valueOf(4), box.getAvailableTrays());
    }

    @Test
    public void should_return_selected_tray() {
        ProductBox box = new ProductBox();
        box.addTray(new Tray(Products.COCA_COLA_0_33, 1));

        assertEquals(Tray.class, box.getTray(0).getClass());
        assertEquals(Products.COCA_COLA_0_33, box.getTray(0).getProduct());
    }

    @Test
    public void should_throw_exception_on_illegal_tray_selected() {
        ProductBox box = new ProductBox();
        box.addTray(new Tray(Products.COCA_COLA_0_33, 1));

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid tray selected");

        box.getTray(2);
    }
}
