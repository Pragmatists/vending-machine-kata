package tdd.vendingMachine;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.*;

import static tdd.vendingMachine.util.Constants.ACCURACY;

public class VendingMachineTest {

    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;

    @Before
    public void setup(){
        COLA_199_025 = new Product(1.99, "COLA_199_025");
        CHIPS_025 = new Product(1.29, "CHIPS_025");
        CHOCOLATE_BAR = new Product(1.49, "CHOCOLATE_BAR");
    }

    @After
    public void tearDown(){
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_on_null_products_shelves_given() {
        new VendingMachine(null, Collections.emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_on_null_coins_shelves_given() {
        new VendingMachine(Collections.emptyMap(), null);
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_displaying_product_price_invalid_shelf_number() {
        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));
        vendingMachine.displayProductPrice(5);
    }

    @Test
    public void should_display_product_price_for_valid_shelf_number() {
        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));
        vendingMachine.displayProductPrice(0);
    }

    @Test
    public void should_add_coin_to_credit_stack() {
        Coin fiftyCents = Coin.FIFTY_CENTS;

        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));

        double previousCredit = vendingMachine.getCredit();
        int previousSize = vendingMachine.getCreditStack().size();

        vendingMachine.addCoinToCredit(fiftyCents);

        Assert.assertEquals(previousSize + 1, vendingMachine.getCreditStack().size());
        Assert.assertEquals(previousCredit + fiftyCents.denomination, vendingMachine.getCredit(), ACCURACY);
        Assert.assertEquals(fiftyCents, vendingMachine.getCreditStack().peek());

    }

    @Test
    public void should_update_message_on_display() {
        String message = "message";
        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));

        vendingMachine.showMessageOnDisplay(message);

        Assert.assertEquals(message, vendingMachine.getDisplay().getCurrentMessage());
    }

    @Test
    public void should_return_all_credit_bucket() {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        Coin tenCents = Coin.TEN_CENTS;

        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));

        vendingMachine.addCoinToCredit(fiftyCents);
        vendingMachine.addCoinToCredit(tenCents);
        vendingMachine.addCoinToCredit(fiftyCents);
        vendingMachine.addCoinToCredit(tenCents);
        vendingMachine.addCoinToCredit(fiftyCents);

        vendingMachine.returnAllCreditToBucket();

        Assert.assertTrue(vendingMachine.getCreditStack().isEmpty());

    }

}
