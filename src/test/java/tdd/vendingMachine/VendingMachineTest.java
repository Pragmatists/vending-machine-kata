package tdd.vendingMachine;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.util.Constants;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.*;

import static tdd.vendingMachine.util.Constants.ACCURACY;

@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachine.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class VendingMachineTest {

    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;
    VendingMachine vendingMachine;

    @Before
    public void setup() {
        COLA_199_025 = new Product(1.99, "COLA_199_025");
        CHIPS_025 = new Product(1.29, "CHIPS_025");
        CHOCOLATE_BAR = new Product(1.49, "CHOCOLATE_BAR");
        vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));
    }

    @After
    public void tearDown() {
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        vendingMachine = null;
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
        vendingMachine.displayProductPrice(5);
    }

    @Test
    public void should_display_product_price_for_valid_shelf_number() {
        vendingMachine.displayProductPrice(0);
        Assert.assertTrue(vendingMachine.getDisplay().getCurrentMessage().startsWith("Price"));
    }

    @Test
    public void should_add_coin_to_credit_stack() {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        double previousCredit = vendingMachine.getCredit();
        int previousSize = vendingMachine.getCreditStack().size();

        Assert.assertTrue(vendingMachine.addCoinToCredit(fiftyCents));

        Assert.assertEquals(previousSize + 1, vendingMachine.getCreditStack().size());
        Assert.assertEquals(previousCredit + fiftyCents.denomination, vendingMachine.getCredit(), ACCURACY);
        Assert.assertEquals(fiftyCents, vendingMachine.getCreditStack().peek());

    }


    @Test
    public void should_not_add_fiftyCents_coin_to_credit_stack_slot_full() {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        VendingMachine vendingMachineCashDispenserFull = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 10));

        double previousCredit = vendingMachineCashDispenserFull.getCredit();
        int previousSize = vendingMachineCashDispenserFull.getCreditStack().size();

        Assert.assertFalse(vendingMachineCashDispenserFull.addCoinToCredit(fiftyCents));

        Assert.assertEquals(previousSize, vendingMachineCashDispenserFull.getCreditStack().size());
        Assert.assertEquals(previousCredit, vendingMachineCashDispenserFull.getCredit(), ACCURACY);
    }

    @Test
    public void should_update_message_on_display() {
        String message = "message";

        vendingMachine.showMessageOnDisplay(message);

        Assert.assertEquals(message, vendingMachine.getDisplay().getCurrentMessage());
    }

    @Test
    public void should_return_all_credit_bucket() {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        Coin tenCents = Coin.TEN_CENTS;

        vendingMachine.addCoinToCredit(fiftyCents);
        vendingMachine.addCoinToCredit(tenCents);
        vendingMachine.addCoinToCredit(fiftyCents);
        vendingMachine.addCoinToCredit(tenCents);
        vendingMachine.addCoinToCredit(fiftyCents);

        Assert.assertFalse(vendingMachine.getCreditStack().isEmpty());

        vendingMachine.returnAllCreditToBucket();

        Assert.assertTrue(vendingMachine.getCreditStack().isEmpty());
        Assert.assertEquals(0, vendingMachine.getCredit(), ACCURACY);
        Assert.assertNotEquals(VendingMachine.MSG_NO_CREDIT_AVAILABLE, vendingMachine.getDisplay().getCurrentMessage());
    }

    @Test
    public void should_show_warning_message_on_return_all_credit_no_credit_available() {
        Assert.assertTrue(vendingMachine.getCreditStack().isEmpty());

        vendingMachine.returnAllCreditToBucket();

        Assert.assertTrue(vendingMachine.getCreditStack().isEmpty());
        Assert.assertEquals(0, vendingMachine.getCredit(), ACCURACY);
        Assert.assertEquals(VendingMachine.MSG_NO_CREDIT_AVAILABLE, vendingMachine.getDisplay().getCurrentMessage());
    }

    @Test
    public void should_validate_states_are_not_null() {
        Assert.assertNotNull(vendingMachine.getCurrentState());
        Assert.assertNotNull(vendingMachine.getNoCreditNoProductSelectedState());
        Assert.assertNotNull(vendingMachine.getHasCreditNoProductSelectedState());
        Assert.assertNotNull(vendingMachine.getNoCreditProductSelectedState());
        Assert.assertNotNull(vendingMachine.getHasCreditProductSelectedState());
        Assert.assertNotNull(vendingMachine.getSoldOutState());
    }

    @Test
    public void should_select_product_given_valid_shelfNumber() {
        Assert.assertNull(vendingMachine.getSelectedProduct());
        vendingMachine.selectProductGivenShelfNumber(0);
        Assert.assertNotNull(vendingMachine.getSelectedProduct());
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_select_product_given_invalid_shelfNumber() {
        Assert.assertNull(vendingMachine.getSelectedProduct());
        vendingMachine.selectProductGivenShelfNumber(995);
    }

    @Test
    public void should_count_cash_in_dispenser() {
        int coinsPerShelf = 8;

        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, coinsPerShelf));

        double totalAmountOnDispenser = Arrays.stream(Coin.values())
            .mapToDouble(coin -> coin.denomination * coinsPerShelf)
            .reduce(Constants.SUM_DOUBLE_IDENTITY, Constants.SUM_DOUBLE_BINARY_OPERATOR);

        Assert.assertEquals(totalAmountOnDispenser, vendingMachine.countCashInDispenser(), ACCURACY);

    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_provisioning_non_empty_credit_stack_to_full_cash_dispenser() {
        Coin tenCents = Coin.TEN_CENTS;

        VendingMachine spiedVendingMachine = PowerMockito.spy(new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 10)));
        Mockito.when(spiedVendingMachine.dispenserHasCoinSlotAvailable(tenCents)).thenReturn(true);//mock accepting coins to a full cash dispenser

        spiedVendingMachine.addCoinToCredit(tenCents);
        Assert.assertEquals(1, spiedVendingMachine.getCreditStack().size());

        Mockito.verify(spiedVendingMachine, Mockito.times(1)).dispenserHasCoinSlotAvailable(tenCents);

        spiedVendingMachine.provisionCreditStackToDispenser();
    }

    @Test
    public void should_provision_non_empty_credit_stack_to_cash_dispenser() {
        List<Coin> coins = Arrays.asList(Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TWO, Coin.ONE, Coin.FIFTY_CENTS, Coin.FIVE);

        double creditBeforeInserting = vendingMachine.getCredit();
        int stackSizeBeforeInserting = vendingMachine.getCreditStack().size();
        double totalInsertedCash = coins.stream()
            .mapToDouble(coin -> coin.denomination)
            .reduce(Constants.SUM_DOUBLE_IDENTITY, Constants.SUM_DOUBLE_BINARY_OPERATOR);
        double cashInDispenserBeforeInserting = vendingMachine.countCashInDispenser();

        Assert.assertTrue(totalInsertedCash > 0);
        coins.forEach(vendingMachine::addCoinToCredit);

        Assert.assertEquals(stackSizeBeforeInserting + coins.size(), vendingMachine.getCreditStack().size());
        Assert.assertEquals(creditBeforeInserting + totalInsertedCash, vendingMachine.getCredit(), ACCURACY);

        vendingMachine.provisionCreditStackToDispenser();

        Assert.assertTrue(vendingMachine.getCreditStack().isEmpty());
        Assert.assertEquals(0, vendingMachine.getCredit(), ACCURACY);
        Assert.assertEquals(cashInDispenserBeforeInserting + totalInsertedCash, vendingMachine.countCashInDispenser(), ACCURACY);
    }

    @Test
    public void should_provision_empty_credit_stack_to_cash_dispenser() {
        double creditBeforeInserting = vendingMachine.getCredit();
        int stackSizeBeforeInserting = vendingMachine.getCreditStack().size();
        double cashInDispenserBeforeInserting = vendingMachine.countCashInDispenser();

        Assert.assertTrue(vendingMachine.getCreditStack().isEmpty());
        Assert.assertEquals(0, vendingMachine.getCredit(), ACCURACY);

        vendingMachine.provisionCreditStackToDispenser();

        Assert.assertEquals(stackSizeBeforeInserting, vendingMachine.getCreditStack().size());
        Assert.assertTrue(vendingMachine.getCreditStack().isEmpty());
        Assert.assertEquals(creditBeforeInserting, vendingMachine.getCredit(), ACCURACY);
        Assert.assertEquals(cashInDispenserBeforeInserting, vendingMachine.countCashInDispenser(), ACCURACY);
    }

    @Test
    public void should_dispense_selected_product_to_bucket() {
        vendingMachine.selectProductGivenShelfNumber(0);
        vendingMachine.dispenseSelectedProductToBucket();
        Assert.assertTrue(vendingMachine.getDisplay().getCurrentMessage().contains("dispensed"));
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_dispense_selected_product_to_bucket_when_none_selected() {
        vendingMachine.dispenseSelectedProductToBucket();
    }

    @Test
    public void should_return_true_since_cash_dispenser_has_available_slots() {
        Assert.assertTrue(vendingMachine.dispenserHasCoinSlotAvailable(Coin.FIFTY_CENTS));
    }

    @Test
    public void should_return_false_on_every_denomination_since_cash_dispenser_does_not_have_available_slots() {
        VendingMachine vendingMachineDispenserFull = new VendingMachine(Collections.emptyMap(),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 10));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.FIFTY_CENTS));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.FIVE));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.TEN_CENTS));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.TWENTY_CENTS));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.TWO));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.ONE));
    }

    @Test
    public void should_undo_product_selection_on_unselected_product() {
        vendingMachine.undoProductSelection();
        Assert.assertNull(vendingMachine.getSelectedProduct());
    }

    @Test
    public void should_undo_product_selection_after_a_product_is_selected() {
        vendingMachine.selectProductGivenShelfNumber(0);
        Assert.assertNotNull(vendingMachine.getSelectedProduct());
        vendingMachine.undoProductSelection();
        Assert.assertNull(vendingMachine.getSelectedProduct());
    }

}
