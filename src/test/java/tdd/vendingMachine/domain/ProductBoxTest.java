package tdd.vendingMachine.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ProductBoxTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void should_return_zero_tray_count_for_empty_box() {
        ProductBox box = new ProductBox();

        assertEquals(0, box.getAvailableTrays());
    }

    @Test
    public void should_return_count_of_installed_trays() {
        ProductBox box = new ProductBox();
        box.addTray(Products.COCA_COLA_0_33);
        box.addTray(Products.COCA_COLA_0_33);
        box.addTray(Products.COCA_COLA_0_33);
        box.addTray(Products.COCA_COLA_0_33);

        assertEquals(4, box.getAvailableTrays());
    }

    @Test
    public void should_return_selected_tray() {
        ProductBox box = new ProductBox();
        box.addTray(Products.COCA_COLA_0_33);

        assertEquals(Tray.class, box.getTray(0));
        assertEquals(Products.COCA_COLA_0_33, box.getTray(0).getProduct());
    }

    @Test
    public void should_throw_exception_on_illegal_tray_selected() {
        ProductBox box = new ProductBox();
        box.addTray(Products.COCA_COLA_0_33);

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid tray selected");

        box.getTray(2);
    }
}
