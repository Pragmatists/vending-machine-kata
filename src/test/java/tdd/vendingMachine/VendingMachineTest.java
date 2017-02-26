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
import tdd.vendingMachine.domain.Shelf;
import tdd.vendingMachine.domain.VendingMachineConfiguration;
import tdd.vendingMachine.domain.exception.*;
import tdd.vendingMachine.state.SoldOutState;
import tdd.vendingMachine.state.State;
import tdd.vendingMachine.util.Constants;
import tdd.vendingMachine.util.TestUtils.TestUtils;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachine.class, VendingMachineConfiguration.class,
    VendingMachineFactory.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class VendingMachineTest {

    private Product COLA_199_025;

    @Before
    public void setup() throws Exception {
        COLA_199_025 = new Product(190, "COLA_199_025");
    }

    @After
    public void tearDown() {
        COLA_199_025 = null;
    }

    /**
     * Creates a mock for the class VendingMachineConfiguration
     * @param coinShelfCapacity what to return when coinShelfCapacity is requested
     * @param productShelfCount what to return when productShelfCount is requested
     * @param productShelfCapacity what to return when productShelfCapacity is requested
     * @return new mock with the desired behaviour
     */
    private VendingMachineConfiguration getConfigMock(int coinShelfCapacity, int productShelfCount, int productShelfCapacity) {
        VendingMachineConfiguration vendingMachineConfigurationMock = Mockito.mock(VendingMachineConfiguration.class);
        Mockito.when(vendingMachineConfigurationMock.getCoinShelfCapacity()).thenReturn(coinShelfCapacity);
        Mockito.when(vendingMachineConfigurationMock.getProductShelfCount()).thenReturn(productShelfCount);
        Mockito.when(vendingMachineConfigurationMock.getProductShelfCapacity()).thenReturn(productShelfCapacity);
        return vendingMachineConfigurationMock;
    }

    /**
     * Verifies the calls to the methods on the mock for the VendingMachineConfiguration object
     * @param mockConfig the object to verify
     * @param coinShelfInvocations expected invocations for coinShelfCapacity
     * @param productShelfCountInvocations expected invocations for productShelfCount
     * @param productShelfCapacityInvocations expected invocations for productShelfCapacity
     */
    private void verifyConfigMock(VendingMachineConfiguration mockConfig, int coinShelfInvocations, int productShelfCountInvocations, int productShelfCapacityInvocations) {
        Mockito.verify(mockConfig, Mockito.times(coinShelfInvocations)).getCoinShelfCapacity();
        Mockito.verify(mockConfig, Mockito.times(productShelfCountInvocations)).getProductShelfCount();
        Mockito.verify(mockConfig, Mockito.times(productShelfCapacityInvocations)).getProductShelfCapacity();
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_on_null_products_shelves_given() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        new VendingMachine(null, Collections.emptyMap());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_on_null_coins_shelves_given() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        new VendingMachine(Collections.emptyMap(), null);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_displaying_product_price_invalid_shelf_number() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 5);

        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        new VendingMachine(productShelves, coinShelves).displayProductPrice(5);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_display_product_price_for_valid_shelf_number() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 5);

        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        vendingMachine.displayProductPrice(0);
        Assert.assertTrue(vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.PRICE.label));

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_add_coin_to_credit_stack() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItem(COLA_199_025, 1, coinShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 5);

        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);
        Coin fiftyCents = Coin.FIFTY_CENTS;
        int previousCredit = vendingMachine.getCredit();
        int previousSize = vendingMachine.getCreditStackSize();

        vendingMachine.addCoinToCredit(fiftyCents);

        Assert.assertEquals(previousSize + 1, vendingMachine.getCreditStackSize());
        Assert.assertEquals(previousCredit + fiftyCents.denomination, vendingMachine.getCredit());
        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test(expected = CashDispenserFullException.class)
    public void should_not_add_fiftyCents_coin_to_credit_stack_slot_full() throws Exception {
        Coin fiftyCents = Coin.FIFTY_CENTS;

        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 10);
        VendingMachine vendingMachineCashDispenserFull = new VendingMachine(productShelves, coinShelves);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
        vendingMachineCashDispenserFull.addCoinToCredit(fiftyCents);
    }

    @Test(expected = CashDispenserFullException.class)
    public void should_only_accept_coins_until_capacity_for_coin_shelf_is_reached() throws Exception {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        int capacity = 10;
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachineCashDispenserFull = new VendingMachine(productShelves, coinShelves);

        int insertsBeforeFull = capacity - initialShelfCount;
        int previousCredit = vendingMachineCashDispenserFull.getCredit();
        int previousSize = vendingMachineCashDispenserFull.getCreditStackSize();

        for (int i = 0; i < insertsBeforeFull; i++) {
            vendingMachineCashDispenserFull.addCoinToCredit(fiftyCents);
        }
        Assert.assertEquals(previousSize + insertsBeforeFull, vendingMachineCashDispenserFull.getCreditStackSize());
        Assert.assertEquals(previousCredit + insertsBeforeFull * fiftyCents.denomination, vendingMachineCashDispenserFull.getCredit());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);

        //after this point FIFTY_CENT shelf can't receive any more coins
        vendingMachineCashDispenserFull.addCoinToCredit(fiftyCents);
    }

    @Test
    public void should_have_empty_credit_stack() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        Assert.assertTrue(vendingMachine.isCreditStackEmpty());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_return_empty_since_no_messages_have_been_sent() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        Assert.assertTrue(StringUtils.isEmpty(vendingMachine.getDisplayCurrentMessage()));

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_update_message_on_display() throws Exception {
        String message = "label";
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        vendingMachine.showMessageOnDisplay(message);

        Assert.assertEquals(message, vendingMachine.getDisplayCurrentMessage());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_return_all_credit_bucket() throws Exception {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        Coin tenCents = Coin.TEN_CENTS;
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

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

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_show_warning_message_on_return_all_credit_no_credit_available() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        Assert.assertTrue(vendingMachine.isCreditStackEmpty());

        vendingMachine.returnAllCreditToBucket();

        Assert.assertTrue(vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, vendingMachine.getCredit());
        Assert.assertEquals(VendingMachineMessages.NO_CREDIT_AVAILABLE.label, vendingMachine.getDisplayCurrentMessage());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_validate_states_are_not_null() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        Assert.assertNotNull(vendingMachine.getCurrentState());
        Assert.assertNotNull(vendingMachine.getReadyState());
        Assert.assertNotNull(vendingMachine.getCreditNotSelectedProductState());
        Assert.assertNotNull(vendingMachine.getNoCreditSelectedProductState());
        Assert.assertNotNull(vendingMachine.getInsufficientCreditState());
        Assert.assertNotNull(vendingMachine.getSoldOutState());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_select_product_given_valid_shelfNumber() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        Assert.assertNull(vendingMachine.getSelectedProduct());
        vendingMachine.selectProductGivenShelfNumber(0);
        Assert.assertNotNull(vendingMachine.getSelectedProduct());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_select_product_given_invalid_shelfNumber() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        Assert.assertNull(vendingMachine.getSelectedProduct());
        vendingMachine.selectProductGivenShelfNumber(995);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test(expected = ShelfEmptyNotAvailableForSelectionException.class)
    public void should_fail_select_product_given_empty_shelfNumber() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        List<Product> products = Collections.singletonList(COLA_199_025);
        VendingMachine soldOutVendingMachine = new VendingMachineFactory().buildSoldOutVendingMachineNoCash(products);
        Assert.assertNull(soldOutVendingMachine.getSelectedProduct());
        soldOutVendingMachine.selectProductGivenShelfNumber(0);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_count_cash_in_dispenser() throws Exception {
        int coinsPerShelf = 8;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinsPerShelf));

        int totalAmountOnDispenser = Arrays.stream(Coin.values())
            .mapToInt(coin -> coin.denomination * coinsPerShelf)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);

        Assert.assertEquals(totalAmountOnDispenser, vendingMachine.countCashInDispenser());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test(expected = NotEnoughSlotsAvailableDispenserException.class)
    public void should_fail_provisioning_non_empty_credit_stack_to_full_cash_dispenser() throws Exception {
        Coin tenCents = Coin.TEN_CENTS;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine spiedVendingMachine = PowerMockito.spy(new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 10)));
        Mockito.when(spiedVendingMachine.dispenserHasCoinSlotAvailable(tenCents)).thenReturn(true);//mock accepting coins to a full cash dispenser

        spiedVendingMachine.addCoinToCredit(tenCents);
        Assert.assertEquals(1, spiedVendingMachine.getCreditStackSize());

        Mockito.verify(spiedVendingMachine, Mockito.times(1)).dispenserHasCoinSlotAvailable(tenCents);
        spiedVendingMachine.provisionCreditStackCashToDispenser();

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_provision_non_empty_credit_stack_cash_to_cash_dispenser() throws Exception {
        List<Coin> coins = Arrays.asList(Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TWO, Coin.ONE, Coin.FIFTY_CENTS, Coin.FIVE);
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        int creditBeforeInserting = vendingMachine.getCredit();
        int stackSizeBeforeInserting = vendingMachine.getCreditStackSize();
        int totalInsertedCash = coins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
        int cashInDispenserBeforeInserting = vendingMachine.countCashInDispenser();

        Assert.assertTrue(totalInsertedCash > 0);
        for(Coin coin: coins) {
            vendingMachine.addCoinToCredit(coin);
        }

        Assert.assertEquals(stackSizeBeforeInserting + coins.size(), vendingMachine.getCreditStackSize());
        Assert.assertEquals(creditBeforeInserting + totalInsertedCash, vendingMachine.getCredit());

        vendingMachine.provisionCreditStackCashToDispenser();

        Assert.assertFalse(vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(cashInDispenserBeforeInserting + totalInsertedCash, vendingMachine.countCashInDispenser());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_provision_empty_credit_stack_to_cash_dispenser() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        int creditBeforeInserting = vendingMachine.getCredit();
        int stackSizeBeforeInserting = vendingMachine.getCreditStackSize();
        int cashInDispenserBeforeInserting = vendingMachine.countCashInDispenser();

        Assert.assertTrue(vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, vendingMachine.getCredit());

        vendingMachine.provisionCreditStackCashToDispenser();

        Assert.assertEquals(stackSizeBeforeInserting, vendingMachine.getCreditStackSize());
        Assert.assertTrue(vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(creditBeforeInserting, vendingMachine.getCredit());
        Assert.assertEquals(cashInDispenserBeforeInserting, vendingMachine.countCashInDispenser());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_count_the_amount_products_in_given_valid_shelfNumber() throws Exception {
        int expectedProductsOnShelf = 1;
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        Assert.assertEquals(expectedProductsOnShelf, vendingMachine.countProductsOnShelf(0));

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_count_the_amount_products_invalid_shelfNumber() throws Exception {
        int invalidShelfNumber = 12313212;
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);
        vendingMachine.countProductsOnShelf(invalidShelfNumber);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_count_the_amount_products_in_vending_machine() throws Exception {
        int amountOfProductsPerType = 2;
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        List<Product> products = Arrays.asList(new Product(100, "PRODUCT1"), new Product(200, "PRODUCT2"));
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(products, amountOfProductsPerType, 10);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine myVendingMachine = new VendingMachine(productShelves, coinShelves);
        int expectedAmountProducts = amountOfProductsPerType * products.size();
        Assert.assertEquals(expectedAmountProducts, myVendingMachine.countTotalAmountProducts());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_dispense_selected_product_to_bucket() throws Exception {
        int shelfNumber = 0;
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        int totalProductsBefore = vendingMachine.countTotalAmountProducts();
        int productsOnShelfBefore = vendingMachine.countProductsOnShelf(shelfNumber);

        vendingMachine.selectProductGivenShelfNumber(shelfNumber);
        vendingMachine.dispenseSelectedProductToBucketAndClearCreditStack();

        Assert.assertNotNull(vendingMachine.getSelectedProduct());
        Assert.assertEquals(totalProductsBefore - 1, vendingMachine.countTotalAmountProducts());
        Assert.assertEquals(productsOnShelfBefore - 1, vendingMachine.countProductsOnShelf(shelfNumber));
        Assert.assertTrue(vendingMachine.getDisplayCurrentMessage().endsWith(VendingMachineMessages.DISPENSED_TO_BUCKET.label));

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_dispense_selected_product_to_bucket_when_none_selected() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        vendingMachine.dispenseSelectedProductToBucketAndClearCreditStack();

        Assert.assertNull(vendingMachine.getSelectedProduct());
        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_return_true_since_cash_dispenser_has_available_slots() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        Assert.assertTrue(vendingMachine.dispenserHasCoinSlotAvailable(Coin.FIFTY_CENTS));

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_return_false_on_every_denomination_since_cash_dispenser_does_not_have_available_slots() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine vendingMachineDispenserFull = new VendingMachine(Collections.emptyMap(),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 10));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.FIFTY_CENTS));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.FIVE));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.TEN_CENTS));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.TWENTY_CENTS));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.TWO));
        Assert.assertFalse(vendingMachineDispenserFull.dispenserHasCoinSlotAvailable(Coin.ONE));

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);

    }

    @Test
    public void should_undo_product_selection_on_unselected_product() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        vendingMachine.undoProductSelection();
        Assert.assertNull(vendingMachine.getSelectedProduct());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_undo_product_selection_after_a_product_is_selected() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        vendingMachine.selectProductGivenShelfNumber(0);
        Assert.assertNotNull(vendingMachine.getSelectedProduct());
        vendingMachine.undoProductSelection();
        Assert.assertNull(vendingMachine.getSelectedProduct());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_give_pending_balance_as_selected_product_price_minus_total_credit() throws Exception {
        Product expectedSelectedProduct = COLA_199_025;
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        vendingMachine.selectProductGivenShelfNumber(0);

        Assert.assertNotNull(vendingMachine.getSelectedProduct());
        Assert.assertEquals(expectedSelectedProduct, vendingMachine.getSelectedProduct());

        Assert.assertEquals(expectedSelectedProduct.getPrice(), vendingMachine.calculatePendingBalance());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_calculating_pending_balance_since_no_product_was_selected() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        Assert.assertNull(vendingMachine.getSelectedProduct());
        vendingMachine.calculatePendingBalance();

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_return_true_can_give_change_from_dispenser() throws Exception {
        Product cheap_product = new Product((880), "cheap_product");
        List<Coin> tenInCoins = Arrays.asList(Coin.FIVE, Coin.FIVE);
        int totalCoins = tenInCoins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);

        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine myVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(cheap_product, 2),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 1));
        int totalCashBeforeOperation = myVendingMachine.countCashInDispenser();

        for(Coin coin: tenInCoins){
            myVendingMachine.addCoinToCredit(coin);
        }

        Assert.assertEquals(totalCoins, myVendingMachine.getCredit());
        Assert.assertEquals(tenInCoins.size(), myVendingMachine.getCreditStackSize());

        myVendingMachine.selectProductGivenShelfNumber(0);
        myVendingMachine.displayProductPrice(0);

        int changeRequested = cheap_product.getPrice() - totalCoins;
        Assert.assertTrue(myVendingMachine.canGiveChangeFromCashDispenser(changeRequested));
        Assert.assertEquals(totalCashBeforeOperation, myVendingMachine.countCashInDispenser());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_return_false_can_not_give_change_from_dispenser() throws Exception {
        Product unPurchasableProduct = new Product((882), "cheap_product"); //this product cant be purchased since no coin of one cent exists.
        List<Coin> tenInCoins = Arrays.asList(Coin.FIVE, Coin.FIVE);
        int totalCoins = tenInCoins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);

        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine myVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(unPurchasableProduct, 2),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 1));
        int totalCashBeforeOperation = myVendingMachine.countCashInDispenser();

        for(Coin coin: tenInCoins){
            myVendingMachine.addCoinToCredit(coin);
        }

        Assert.assertEquals(totalCoins, myVendingMachine.getCredit());
        Assert.assertEquals(tenInCoins.size(), myVendingMachine.getCreditStackSize());

        myVendingMachine.selectProductGivenShelfNumber(0);
        myVendingMachine.displayProductPrice(0);

        int changeRequested = unPurchasableProduct.getPrice() - totalCoins;
        Assert.assertFalse(myVendingMachine.canGiveChangeFromCashDispenser(changeRequested));
        Assert.assertEquals(totalCashBeforeOperation, myVendingMachine.countCashInDispenser());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_return_false_cash_dispenser_cant_build_the_amount_requested() throws Exception {
        Product cheap_product = new Product((810), "cheap_product");
        List<Coin> nineFiftyInCoins = Arrays.asList(Coin.FIVE, Coin.ONE, Coin.ONE, Coin.ONE, Coin.ONE, Coin.FIFTY_CENTS);
        int totalCoins = nineFiftyInCoins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);

        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine myVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(cheap_product, 2),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 1));
        int totalCashBeforeOperation = myVendingMachine.countCashInDispenser();

        for(Coin coin: nineFiftyInCoins){
            myVendingMachine.addCoinToCredit(coin);
        }

        Assert.assertEquals(totalCoins, myVendingMachine.getCredit());
        Assert.assertEquals(nineFiftyInCoins.size(), myVendingMachine.getCreditStackSize());

        myVendingMachine.selectProductGivenShelfNumber(0);
        myVendingMachine.displayProductPrice(0);

        int changeRequested = cheap_product.getPrice() - totalCoins;
        Assert.assertFalse(myVendingMachine.canGiveChangeFromCashDispenser(changeRequested));
        Assert.assertEquals(totalCashBeforeOperation, myVendingMachine.countCashInDispenser());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_return_change_stack_with_pending_balance() throws Exception {
        Product cheap_product = new Product((880), "cheap_product");
        List<Coin> tenInCoins = Arrays.asList(Coin.FIVE, Coin.FIVE);
        int totalCoins = tenInCoins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
        int desiredAmountOfProducts = 2;

        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine myVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(cheap_product, desiredAmountOfProducts),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 1));
        int totalCashBeforeOperation = myVendingMachine.countCashInDispenser();
        int shelfNumberToSelect = 0;
        int totalAmountOfProductsOnVendingMachinePriorDispense = myVendingMachine.countTotalAmountProducts();
        int totalAmountProductsInShelfBeforeDispense = myVendingMachine.countProductsOnShelf(shelfNumberToSelect);

        for(Coin coin: tenInCoins){
            myVendingMachine.addCoinToCredit(coin);
        }

        Assert.assertEquals(totalCoins, myVendingMachine.getCredit());
        Assert.assertEquals(tenInCoins.size(), myVendingMachine.getCreditStackSize());

        myVendingMachine.selectProductGivenShelfNumber(shelfNumberToSelect);
        myVendingMachine.displayProductPrice(shelfNumberToSelect);
        myVendingMachine.provisionCreditStackCashToDispenser();
        myVendingMachine.dispenseCurrentBalance();
        myVendingMachine.dispenseSelectedProductToBucketAndClearCreditStack();

        Assert.assertNotNull(myVendingMachine.getSelectedProduct());
        Assert.assertEquals(totalAmountProductsInShelfBeforeDispense - 1, myVendingMachine.countProductsOnShelf(shelfNumberToSelect)); //Decrement of products by one in shelf
        Assert.assertEquals(totalAmountOfProductsOnVendingMachinePriorDispense - 1, myVendingMachine.countTotalAmountProducts()); //Decrement of products by one in total
        Assert.assertEquals(totalCashBeforeOperation + cheap_product.getPrice(), myVendingMachine.countCashInDispenser()); //Increase of cash the product received

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);

    }

    @Test(expected = UnableToProvideBalanceException.class)
    public void should_fail_return_balance_not_possible_with_given_dispenser_coin_cash() throws Exception {
        Product cheap_product = new Product((810), "cheap_product");
        List<Coin> nineFiftyInCoins = Arrays.asList(Coin.FIVE, Coin.ONE, Coin.ONE, Coin.ONE, Coin.ONE, Coin.FIFTY_CENTS);
        int totalCoins = nineFiftyInCoins.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
        int desiredAmountOfProducts = 2;

        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine myVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(cheap_product, desiredAmountOfProducts),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 1));
        int shelfNumberToSelect = 0;

        for(Coin coin: nineFiftyInCoins){
            myVendingMachine.addCoinToCredit(coin);
        }

        Assert.assertEquals(totalCoins, myVendingMachine.getCredit());
        Assert.assertEquals(nineFiftyInCoins.size(), myVendingMachine.getCreditStackSize());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);

        myVendingMachine.selectProductGivenShelfNumber(shelfNumberToSelect);
        myVendingMachine.displayProductPrice(shelfNumberToSelect);
        myVendingMachine.provisionCreditStackCashToDispenser();
        myVendingMachine.dispenseCurrentBalance();
        myVendingMachine.dispenseSelectedProductToBucketAndClearCreditStack();
    }

    @Test
    public void should_update_state() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        State soldOutState = vendingMachine.getSoldOutState();
        Assert.assertFalse(vendingMachine.getCurrentState() instanceof SoldOutState);
        vendingMachine.setCurrentState(soldOutState);
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof SoldOutState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_fail_since_no_coin_shelves_are_provided() throws Exception {
        int coinShelfCapacity = 6;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        boolean thrownException = false;
        Map<Integer, Shelf<Product>> productShelves = Collections.emptyMap();
        Map<Coin, Shelf<Coin>> coinShelves = Collections.emptyMap();
        try {
            new VendingMachine(productShelves, coinShelves);
        } catch (InvalidShelfSizeException invalidShelfSizeException) {
            thrownException = true;
            Assert.assertEquals(0, invalidShelfSizeException.getGivenSize());
            Assert.assertEquals(coinShelfCapacity, invalidShelfSizeException.getMaximumSize());
        }
        Assert.assertTrue(thrownException);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 0, 0, 0);
    }

    @Test
    public void should_fail_since_product_shelves_exceed_machine_max_count() throws Exception {
        int initialShelfCount = 7;
        int productShelfCount = 0;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, productShelfCount, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        boolean thrownException = false;

        try {
            new VendingMachine(productShelves, coinShelves);
        }catch (InvalidShelfSizeException ime) {
            thrownException = true;
            Assert.assertEquals(productShelfCount, ime.getMaximumSize());
            Assert.assertEquals(productShelves.size(), ime.getGivenSize());
        }

        Assert.assertTrue(thrownException);
        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 2, 0);
    }

    @Test
    public void should_fail_creation_since_given_product_shelves_exceed_maximum_capacity() throws Exception {
        int initialShelfCount = 7;
        int productShelfCapacity = 1;
        int expectedProductShelfCapacity = 10;
        int coinShelfCapacity = 7;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 1, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);

        boolean thrownException = false;
        try {
            new VendingMachine(productShelves, coinShelves);
        }catch (InvalidShelfSizeException ime) {
            thrownException = true;
            Assert.assertEquals(productShelfCapacity, ime.getMaximumSize());
            Assert.assertEquals(expectedProductShelfCapacity, ime.getGivenSize());
        }

        Assert.assertTrue(thrownException);
        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 2);
    }

    @Test
    public void should_display_pending_balance_on_product_selected_when_inserting_coin() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        vendingMachine.addCoinToCredit(Coin.TEN_CENTS);

        Assert.assertNull(vendingMachine.getSelectedProduct());
        Assert.assertTrue(vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.CASH_ACCEPTED_NEW_CREDIT.label));

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_display_credit_if_product_not_selected_when_inserting_coin() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        vendingMachine.selectProductGivenShelfNumber(0);
        vendingMachine.addCoinToCredit(Coin.TEN_CENTS);

        Assert.assertNotNull(vendingMachine.getSelectedProduct());
        Assert.assertTrue(vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.PENDING.label));

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_provision_cash_stack_to_dispenser_and_then_return_it_back() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        int cashBeforeOperation = vendingMachine.countCashInDispenser();

        List<Coin> moneyToInsert = Arrays.asList(Coin.FIVE, Coin.FIVE, Coin.FIVE);
        for (Coin coin: moneyToInsert) {
            vendingMachine.addCoinToCredit(coin);
        }

        vendingMachine.provisionCreditStackCashToDispenser();
        vendingMachine.returnCreditStackToBucketUpdatingCashDispenser();

        Assert.assertEquals(0, vendingMachine.getCredit());
        Assert.assertTrue(vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(cashBeforeOperation, vendingMachine.countCashInDispenser());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }

    @Test
    public void should_provision_empty_stack_to_dispenser_and_then_call_return_method() throws Exception {
        int initialShelfCount = 7;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialShelfCount);
        VendingMachine vendingMachine = new VendingMachine(productShelves, coinShelves);

        int cashBeforeOperation = vendingMachine.countCashInDispenser();

        List<Coin> moneyToInsert = Collections.emptyList();
        for (Coin coin: moneyToInsert) {
            vendingMachine.addCoinToCredit(coin);
        }
        Assert.assertTrue(vendingMachine.isCreditStackEmpty());

        vendingMachine.provisionCreditStackCashToDispenser();
        vendingMachine.returnCreditStackToBucketUpdatingCashDispenser();

        Assert.assertEquals(0, vendingMachine.getCredit());
        Assert.assertTrue(vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(cashBeforeOperation, vendingMachine.countCashInDispenser());

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        verifyConfigMock(mockConfig, 1, 1, 1);
    }
}
