package tdd.vendingMachine;

import org.apache.commons.lang3.StringUtils;
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
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.state.SoldOutState;
import tdd.vendingMachine.state.State;
import tdd.vendingMachine.util.Constants;
import tdd.vendingMachine.util.TestUtils.TestUtils;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachine.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class VendingMachineTest {

    private Product COLA_199_025;
    VendingMachine vendingMachine;

    @Before
    public void setup() {
        COLA_199_025 = new Product(199, "COLA_199_025");
        vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));
    }

    @After
    public void tearDown() {
        COLA_199_025 = null;
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
        Assert.assertTrue(vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.PRICE.label));
    }

    @Test
    public void should_add_coin_to_credit_stack() {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        int previousCredit = vendingMachine.getCredit();
        int previousSize = vendingMachine.getCreditStackSize();

        Assert.assertTrue(vendingMachine.addCoinToCredit(fiftyCents));

        Assert.assertEquals(previousSize + 1, vendingMachine.getCreditStackSize());
        Assert.assertEquals(previousCredit + fiftyCents.denomination, vendingMachine.getCredit());
    }

    @Test
    public void should_not_add_fiftyCents_coin_to_credit_stack_slot_full() {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        VendingMachine vendingMachineCashDispenserFull = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 10));

        int previousCredit = vendingMachineCashDispenserFull.getCredit();
        int previousSize = vendingMachineCashDispenserFull.getCreditStackSize();

        Assert.assertFalse(vendingMachineCashDispenserFull.addCoinToCredit(fiftyCents));

        Assert.assertEquals(previousSize, vendingMachineCashDispenserFull.getCreditStackSize());
        Assert.assertEquals(previousCredit, vendingMachineCashDispenserFull.getCredit());
    }

    @Test
    public void should_only_accept_coins_until_capacity_for_coin_shelf_is_reached() {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        int capacity = 10;
        int initialShelfCount = 7;
        VendingMachine vendingMachineCashDispenserFull = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(capacity, initialShelfCount));

        int insertsBeforeFull = capacity - initialShelfCount;
        int previousCredit = vendingMachineCashDispenserFull.getCredit();
        int previousSize = vendingMachineCashDispenserFull.getCreditStackSize();

        for (int i = 0; i < insertsBeforeFull; i++) {
            Assert.assertTrue(vendingMachineCashDispenserFull.addCoinToCredit(fiftyCents));
        }
        //after this point FIFTY_CENT shelf can't receive any more coins

        Assert.assertFalse(vendingMachineCashDispenserFull.addCoinToCredit(fiftyCents));
        Assert.assertFalse(vendingMachineCashDispenserFull.addCoinToCredit(fiftyCents));
        Assert.assertFalse(vendingMachineCashDispenserFull.addCoinToCredit(fiftyCents));

        Assert.assertEquals(previousSize + insertsBeforeFull, vendingMachineCashDispenserFull.getCreditStackSize());
        Assert.assertEquals(previousCredit + insertsBeforeFull * fiftyCents.denomination, vendingMachineCashDispenserFull.getCredit());
    }

    @Test
    public void should_have_empty_credit_stack() {
        Assert.assertTrue(vendingMachine.isCreditStackEmpty());
    }

    @Test
    public void should_return_empty_since_no_messages_have_been_sent() {
        Assert.assertTrue(StringUtils.isEmpty(vendingMachine.getDisplayCurrentMessage()));
    }

    @Test
    public void should_update_message_on_display() {
        String message = "label";

        vendingMachine.showMessageOnDisplay(message);

        Assert.assertEquals(message, vendingMachine.getDisplayCurrentMessage());
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

        Assert.assertFalse(vendingMachine.isCreditStackEmpty());

        vendingMachine.returnAllCreditToBucket();

        Assert.assertTrue(vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, vendingMachine.getCredit());
        Assert.assertNotEquals(VendingMachineMessages.NO_CREDIT_AVAILABLE, vendingMachine.getDisplayCurrentMessage());
    }

    @Test
    public void should_show_warning_message_on_return_all_credit_no_credit_available() {
        Assert.assertTrue(vendingMachine.isCreditStackEmpty());

        vendingMachine.returnAllCreditToBucket();

        Assert.assertTrue(vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, vendingMachine.getCredit());
        Assert.assertEquals(VendingMachineMessages.NO_CREDIT_AVAILABLE.label, vendingMachine.getDisplayCurrentMessage());
    }

    @Test
    public void should_validate_states_are_not_null() {
        Assert.assertNotNull(vendingMachine.getCurrentState());
        Assert.assertNotNull(vendingMachine.getNoCreditNoProductSelectedState());
        Assert.assertNotNull(vendingMachine.getHasCreditNoProductSelectedState());
        Assert.assertNotNull(vendingMachine.getNoCreditProductSelectedState());
        Assert.assertNotNull(vendingMachine.getInsufficientCreditState());
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

        int totalAmountOnDispenser = Arrays.stream(Coin.values())
            .mapToInt(coin -> coin.denomination * coinsPerShelf)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);

        Assert.assertEquals(totalAmountOnDispenser, vendingMachine.countCashInDispenser());

    }

    @Test(expected = InputMismatchException.class)
    public void should_fail_provisioning_non_empty_credit_stack_to_full_cash_dispenser() {
        Coin tenCents = Coin.TEN_CENTS;

        VendingMachine spiedVendingMachine = PowerMockito.spy(new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 10)));
        Mockito.when(spiedVendingMachine.dispenserHasCoinSlotAvailable(tenCents)).thenReturn(true);//mock accepting coins to a full cash dispenser

        spiedVendingMachine.addCoinToCredit(tenCents);
        Assert.assertEquals(1, spiedVendingMachine.getCreditStackSize());

        Mockito.verify(spiedVendingMachine, Mockito.times(1)).dispenserHasCoinSlotAvailable(tenCents);
        spiedVendingMachine.provisionCreditStackToDispenser();
    }

    @Test
    public void should_provision_non_empty_credit_stack_to_cash_dispenser() {
        List<Coin> coins = Arrays.asList(Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TWO, Coin.ONE, Coin.FIFTY_CENTS, Coin.FIVE);

        int creditBeforeInserting = vendingMachine.getCredit();
        int stackSizeBeforeInserting = vendingMachine.getCreditStackSize();
        int totalInsertedCash = coins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
        int cashInDispenserBeforeInserting = vendingMachine.countCashInDispenser();

        Assert.assertTrue(totalInsertedCash > 0);
        coins.forEach(vendingMachine::addCoinToCredit);

        Assert.assertEquals(stackSizeBeforeInserting + coins.size(), vendingMachine.getCreditStackSize());
        Assert.assertEquals(creditBeforeInserting + totalInsertedCash, vendingMachine.getCredit());

        vendingMachine.provisionCreditStackToDispenser();

        Assert.assertTrue(vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(cashInDispenserBeforeInserting + totalInsertedCash, vendingMachine.countCashInDispenser());
    }

    @Test
    public void should_provision_empty_credit_stack_to_cash_dispenser() {
        int creditBeforeInserting = vendingMachine.getCredit();
        int stackSizeBeforeInserting = vendingMachine.getCreditStackSize();
        int cashInDispenserBeforeInserting = vendingMachine.countCashInDispenser();

        Assert.assertTrue(vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, vendingMachine.getCredit());

        vendingMachine.provisionCreditStackToDispenser();

        Assert.assertEquals(stackSizeBeforeInserting, vendingMachine.getCreditStackSize());
        Assert.assertTrue(vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(creditBeforeInserting, vendingMachine.getCredit());
        Assert.assertEquals(cashInDispenserBeforeInserting, vendingMachine.countCashInDispenser());
    }

    @Test
    public void should_count_the_amount_products_in_given_valid_shelfNumber() {
        int expectedProductsOnShelf = 1;
        Assert.assertEquals(expectedProductsOnShelf, vendingMachine.countProductsOnShelf(0));
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_count_the_amount_products_invalid_shelfNumber() {
        int invalidShelfNumber = 12313212;
        vendingMachine.countProductsOnShelf(invalidShelfNumber);
    }

    @Test
    public void should_count_the_amount_products_in_vending_machine() {
        int amountOfProductsPerType = 2;
        List<Product> products = Arrays.asList(new Product(100, "PRODUCT1"), new Product(200, "PRODUCT2"));
        VendingMachine myVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(
            products, amountOfProductsPerType), Collections.emptyMap());
        int expectedAmountProducts = amountOfProductsPerType * products.size();
        Assert.assertEquals(expectedAmountProducts, myVendingMachine.countTotalAmountProducts());
    }

    @Test
    public void should_dispense_selected_product_to_bucket() {
        int shelfNumber = 0;
        int totalProductsBefore = vendingMachine.countTotalAmountProducts();
        int productsOnShelfBefore = vendingMachine.countProductsOnShelf(shelfNumber);

        vendingMachine.selectProductGivenShelfNumber(shelfNumber);
        vendingMachine.dispenseSelectedProductToBucket();

        Assert.assertEquals(totalProductsBefore - 1, vendingMachine.countTotalAmountProducts());
        Assert.assertEquals(productsOnShelfBefore - 1, vendingMachine.countProductsOnShelf(shelfNumber));
        Assert.assertTrue(vendingMachine.getDisplayCurrentMessage()
            .endsWith(VendingMachineMessages.DISPENSED_TO_BUCKET.label));
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

    @Test
    public void should_give_pending_balance_as_selected_product_price_minus_total_credit() {
        Product expectedSelectedProduct = COLA_199_025;

        vendingMachine.selectProductGivenShelfNumber(0);

        Assert.assertNotNull(vendingMachine.getSelectedProduct());
        Assert.assertEquals(expectedSelectedProduct, vendingMachine.getSelectedProduct());

        Assert.assertEquals(expectedSelectedProduct.getPrice(), vendingMachine.calculatePendingBalance());
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_calculating_pending_balance_since_no_product_was_selected() {
        Assert.assertNull(vendingMachine.getSelectedProduct());
        vendingMachine.calculatePendingBalance();
    }

    @Test
    public void should_return_true_can_give_change_from_dispenser() {
        Product cheap_product = new Product((880), "cheap_product");
        List<Coin> tenInCoins = Arrays.asList(Coin.FIVE, Coin.FIVE);
        int totalCoins = tenInCoins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
        VendingMachine myVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(cheap_product, 2),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 1));
        int totalCashBeforeOperation = myVendingMachine.countCashInDispenser();

        tenInCoins.forEach(myVendingMachine::addCoinToCredit);
        Assert.assertEquals(totalCoins, myVendingMachine.getCredit());
        Assert.assertEquals(tenInCoins.size(), myVendingMachine.getCreditStackSize());

        myVendingMachine.selectProductGivenShelfNumber(0);
        myVendingMachine.displayProductPrice(0);

        int changeRequested = cheap_product.getPrice() - totalCoins;
        Assert.assertTrue(myVendingMachine.canGiveChangeFromCashDispenser(changeRequested));
        Assert.assertEquals(totalCashBeforeOperation, myVendingMachine.countCashInDispenser());
    }

    @Test
    public void should_return_false_can_not_give_change_from_dispenser() {
        Product unPurchasableProduct = new Product((882), "cheap_product"); //this product cant be purchased since no coin of one cent exists.
        List<Coin> tenInCoins = Arrays.asList(Coin.FIVE, Coin.FIVE);
        int totalCoins = tenInCoins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
        VendingMachine myVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(unPurchasableProduct, 2),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 1));
        int totalCashBeforeOperation = myVendingMachine.countCashInDispenser();

        tenInCoins.forEach(myVendingMachine::addCoinToCredit);
        Assert.assertEquals(totalCoins, myVendingMachine.getCredit());
        Assert.assertEquals(tenInCoins.size(), myVendingMachine.getCreditStackSize());

        myVendingMachine.selectProductGivenShelfNumber(0);
        myVendingMachine.displayProductPrice(0);

        int changeRequested = unPurchasableProduct.getPrice() - totalCoins;
        Assert.assertFalse(myVendingMachine.canGiveChangeFromCashDispenser(changeRequested));
        Assert.assertEquals(totalCashBeforeOperation, myVendingMachine.countCashInDispenser());
    }

    @Test
    public void should_return_false_cash_dispenser_cant_build_the_amount_requested() {
        Product cheap_product = new Product((810), "cheap_product");
        List<Coin> nineFiftyInCoins = Arrays.asList(Coin.FIVE, Coin.ONE, Coin.ONE, Coin.ONE, Coin.ONE, Coin.FIFTY_CENTS);
        int totalCoins = nineFiftyInCoins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);

        VendingMachine myVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(cheap_product, 2),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 1));
        int totalCashBeforeOperation = myVendingMachine.countCashInDispenser();

        nineFiftyInCoins.forEach(myVendingMachine::addCoinToCredit);
        Assert.assertEquals(totalCoins, myVendingMachine.getCredit());
        Assert.assertEquals(nineFiftyInCoins.size(), myVendingMachine.getCreditStackSize());

        myVendingMachine.selectProductGivenShelfNumber(0);
        myVendingMachine.displayProductPrice(0);

        int changeRequested = cheap_product.getPrice() - totalCoins;
        Assert.assertFalse(myVendingMachine.canGiveChangeFromCashDispenser(changeRequested));
        Assert.assertEquals(totalCashBeforeOperation, myVendingMachine.countCashInDispenser());
    }

    @Test
    public void should_return_change_stack_with_pending_balance() {
        Product cheap_product = new Product((880), "cheap_product");
        List<Coin> tenInCoins = Arrays.asList(Coin.FIVE, Coin.FIVE);
        int totalCoins = tenInCoins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
        int desiredAmountOfProducts = 2;
        VendingMachine myVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(cheap_product, desiredAmountOfProducts),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 1));
        int totalCashBeforeOperation = myVendingMachine.countCashInDispenser();
        int shelfNumberToSelect = 0;
        int totalAmountOfProductsOnVendingMachinePriorDispense = myVendingMachine.countTotalAmountProducts();
        int totalAmountProductsInShelfBeforeDispense = myVendingMachine.countProductsOnShelf(shelfNumberToSelect);

        tenInCoins.forEach(myVendingMachine::addCoinToCredit);
        Assert.assertEquals(totalCoins, myVendingMachine.getCredit());
        Assert.assertEquals(tenInCoins.size(), myVendingMachine.getCreditStackSize());

        myVendingMachine.selectProductGivenShelfNumber(shelfNumberToSelect);
        myVendingMachine.displayProductPrice(shelfNumberToSelect);
        myVendingMachine.provisionCreditStackToDispenser();
        myVendingMachine.dispenseCurrentBalance();
        myVendingMachine.dispenseSelectedProductToBucket();

        Assert.assertEquals(totalAmountProductsInShelfBeforeDispense - 1, myVendingMachine.countProductsOnShelf(shelfNumberToSelect)); //Decrement of products by one in shelf
        Assert.assertEquals(totalAmountOfProductsOnVendingMachinePriorDispense - 1, myVendingMachine.countTotalAmountProducts()); //Decrement of products by one in total
        Assert.assertEquals(totalCashBeforeOperation + cheap_product.getPrice(), myVendingMachine.countCashInDispenser()); //Increase of cash the product received
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_fail_return_balance_not_possible_with_given_dispenser_coin_cash() {
        Product cheap_product = new Product((810), "cheap_product");
        List<Coin> nineFiftyInCoins = Arrays.asList(Coin.FIVE, Coin.ONE, Coin.ONE, Coin.ONE, Coin.ONE, Coin.FIFTY_CENTS);
        int totalCoins = nineFiftyInCoins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
        int desiredAmountOfProducts = 2;
        VendingMachine myVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(cheap_product, desiredAmountOfProducts),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 1));
        int shelfNumberToSelect = 0;

        nineFiftyInCoins.forEach(myVendingMachine::addCoinToCredit);
        Assert.assertEquals(totalCoins, myVendingMachine.getCredit());
        Assert.assertEquals(nineFiftyInCoins.size(), myVendingMachine.getCreditStackSize());

        myVendingMachine.selectProductGivenShelfNumber(shelfNumberToSelect);
        myVendingMachine.displayProductPrice(shelfNumberToSelect);
        myVendingMachine.provisionCreditStackToDispenser();
        myVendingMachine.dispenseCurrentBalance();
        myVendingMachine.dispenseSelectedProductToBucket();
    }

    @Test
    public void should_update_state() {
        State soldOutState = vendingMachine.getSoldOutState();
        Assert.assertFalse(vendingMachine.getCurrentState() instanceof SoldOutState);
        vendingMachine.setCurrentState(soldOutState);
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof SoldOutState);
    }
}
