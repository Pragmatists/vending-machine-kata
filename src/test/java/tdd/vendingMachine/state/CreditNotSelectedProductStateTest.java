package tdd.vendingMachine.state;

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
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.VendingMachineFactory;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Shelf;
import tdd.vendingMachine.domain.VendingMachineConfiguration;
import tdd.vendingMachine.dto.ProductImport;
import tdd.vendingMachine.util.Constants;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachine.class, VendingMachineConfiguration.class,
    VendingMachineFactory.class, CreditNotSelectedProductState.class, State.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class CreditNotSelectedProductStateTest implements StateTest {

    private Product COLA_199_025;
    CreditNotSelectedProductState creditNotSelectedProductState;

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

    @Override
    public CreditNotSelectedProductState transformToAndValidateInitialState(VendingMachine vendingMachine) {
        Assert.assertEquals(0, vendingMachine.getCredit()); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof ReadyState);
        ReadyState initialState = (ReadyState) vendingMachine.getCurrentState();

        //transform to desired state
        initialState.insertCoin(Coin.FIFTY_CENTS);

        //validate initial state
        Assert.assertEquals(Coin.FIFTY_CENTS.denomination, initialState.vendingMachine.getCredit());
        Assert.assertTrue(initialState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);
        return (CreditNotSelectedProductState) initialState.vendingMachine.getCurrentState();
    }

    @Before @Override
    public void setup(){
        COLA_199_025 = new Product(190, "COLA_199_025");
    }

    @After @Override
    public void tearDown(){
        COLA_199_025 = null;
        creditNotSelectedProductState = null;
    }

    @Test
    public void should_insert_credit_remain_same_state() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5));
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        Coin tenCents = Coin.TEN_CENTS;
        int previousStackCreditSize = creditNotSelectedProductState.vendingMachine.getCreditStackSize();
        int previousCredit = creditNotSelectedProductState.vendingMachine.getCredit();

        creditNotSelectedProductState.insertCoin(tenCents);

        Assert.assertEquals(previousStackCreditSize + 1, creditNotSelectedProductState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination + previousCredit, creditNotSelectedProductState.vendingMachine.getCredit());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_skip_insert_coins_after_dispensers_capacity_reached() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5));
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        Coin tenCents = Coin.TEN_CENTS;
        int previousStackCreditSize = creditNotSelectedProductState.vendingMachine.getCreditStackSize();
        int previousCredit = creditNotSelectedProductState.vendingMachine.getCredit();
        int insertsToFillCoinShelf = 5;

        //fill ten cents dispenser shelf
        for (int i = 0; i < insertsToFillCoinShelf; i++) {
            creditNotSelectedProductState.insertCoin(tenCents);
        }

        Assert.assertEquals(previousStackCreditSize + insertsToFillCoinShelf, creditNotSelectedProductState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination * insertsToFillCoinShelf + previousCredit, creditNotSelectedProductState.vendingMachine.getCredit());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);

        //insert coins will no take any effect
        creditNotSelectedProductState.insertCoin(tenCents);
        creditNotSelectedProductState.insertCoin(tenCents);
        creditNotSelectedProductState.insertCoin(tenCents);
        creditNotSelectedProductState.insertCoin(tenCents);
        creditNotSelectedProductState.insertCoin(tenCents);

        Assert.assertEquals(previousStackCreditSize + insertsToFillCoinShelf, creditNotSelectedProductState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination * insertsToFillCoinShelf + previousCredit, creditNotSelectedProductState.vendingMachine.getCredit());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);

    }

    @Test
    public void should_select_valid_shelfNumber_and_change_state_to_InsufficientCreditState() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5));
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        creditNotSelectedProductState.selectShelfNumber(0);
        Assert.assertNotNull(creditNotSelectedProductState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof InsufficientCreditState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_select_valid_shelfNumber_and_successfully_sell_product_and_change_state_to_ReadyState() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 2);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelves, coinShelves);
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        creditNotSelectedProductState.insertCoin(Coin.ONE);
        creditNotSelectedProductState.insertCoin(Coin.TWENTY_CENTS);
        creditNotSelectedProductState.insertCoin(Coin.FIFTY_CENTS);
        creditNotSelectedProductState.selectShelfNumber(0);

        Assert.assertNull(creditNotSelectedProductState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCreditStackSize() == 0);
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCredit() == 0);
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_select_valid_shelfNumber_and_successfully_sell_product_and_change_state_to_SoldOutState() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelves, coinShelves);
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        creditNotSelectedProductState.insertCoin(Coin.ONE);
        creditNotSelectedProductState.insertCoin(Coin.TWENTY_CENTS);
        creditNotSelectedProductState.insertCoin(Coin.FIFTY_CENTS);
        creditNotSelectedProductState.selectShelfNumber(0);

        Assert.assertNull(creditNotSelectedProductState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCreditStackSize() == 0);
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCredit() == 0);
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof SoldOutState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_select_invalid_shelfNumber_and_remain_state_to_CreditNoSelectedProductState() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5));
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        creditNotSelectedProductState.selectShelfNumber(5454);
        Assert.assertNull(creditNotSelectedProductState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_fail_when_selecting_empty_shelf() throws Exception {
        int initialCoinsOnShelf = 10;
        int emptyShelfId = 0;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelfStubFromProductImports(
            Arrays.asList(new ProductImport("p1", 100, 0), new ProductImport("p2", 100, 1)), 10);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelves, coinShelves);
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        creditNotSelectedProductState.selectShelfNumber(emptyShelfId);
        Assert.assertNull(creditNotSelectedProductState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_not_add_credit_return_all_inserted_credit_after_cancel_and_change_to_noCreditState() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5));
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        creditNotSelectedProductState.cancel();

        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, creditNotSelectedProductState.vendingMachine.getCredit());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_add_credit_return_all_inserted_credit_after_cancel_and_change_to_noCreditState() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5));
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        List<Coin> coinsToInsert = Arrays.asList(Coin.FIFTY_CENTS, Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TWO, Coin.ONE, Coin.FIVE);

        int previousStackCreditSize = creditNotSelectedProductState.vendingMachine.getCreditStackSize();
        int previousCredit = creditNotSelectedProductState.vendingMachine.getCredit();

        coinsToInsert.forEach(creditNotSelectedProductState::insertCoin);

        Assert.assertEquals(previousStackCreditSize + coinsToInsert.size(), creditNotSelectedProductState.vendingMachine.getCreditStackSize());
        int total = coinsToInsert.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
        Assert.assertEquals(total + previousCredit, creditNotSelectedProductState.vendingMachine.getCredit());

        creditNotSelectedProductState.cancel();

        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, creditNotSelectedProductState.vendingMachine.getCredit());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_select_valid_shelfNumber_and_not_sell_product_unable_to_provide_change() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 0);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelves, coinShelves);
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        creditNotSelectedProductState.insertCoin(Coin.FIVE);
        creditNotSelectedProductState.selectShelfNumber(0);

        Assert.assertNull(creditNotSelectedProductState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCreditStackSize() == 0);
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCredit() == 0);
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_select_valid_shelfNumber_and_sell_product_exact_amount_given_stateToReady() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 2);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelves,
            coinShelves);
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        creditNotSelectedProductState.insertCoin(Coin.ONE);
        creditNotSelectedProductState.insertCoin(Coin.TWENTY_CENTS);
        creditNotSelectedProductState.insertCoin(Coin.TWENTY_CENTS);
        creditNotSelectedProductState.selectShelfNumber(0);

        Assert.assertNull(creditNotSelectedProductState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCreditStackSize() == 0);
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCredit() == 0);
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_select_valid_shelfNumber_and_sell_product_exact_amount_given_state_to_soldOut() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelves,
            coinShelves);
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        creditNotSelectedProductState.insertCoin(Coin.ONE);
        creditNotSelectedProductState.insertCoin(Coin.TWENTY_CENTS);
        creditNotSelectedProductState.insertCoin(Coin.TWENTY_CENTS);
        creditNotSelectedProductState.selectShelfNumber(0);

        Assert.assertNull(creditNotSelectedProductState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCreditStackSize() == 0);
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCredit() == 0);
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof SoldOutState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_send_to_technical_error_state() throws Exception {
        int initialCoinsOnShelf = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(COLA_199_025, 1);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(initialCoinsOnShelf, 5);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelves,
            coinShelves);
        creditNotSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        creditNotSelectedProductState.insertCoin(Coin.ONE);
        creditNotSelectedProductState.insertCoin(Coin.TWENTY_CENTS);
        creditNotSelectedProductState.insertCoin(Coin.TWENTY_CENTS);

        CreditNotSelectedProductState spied = PowerMockito.spy(creditNotSelectedProductState);
        PowerMockito.doThrow(new UnsupportedOperationException("invalid state")).when(spied).attemptSell();

        spied.selectShelfNumber(0);

        Assert.assertTrue(spied.vendingMachine.getCurrentState() instanceof TechnicalErrorState);
        Mockito.verify(spied, Mockito.times(1)).attemptSell();
    }
}
