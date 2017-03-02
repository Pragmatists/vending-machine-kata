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
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Shelf;
import tdd.vendingMachine.domain.VendingMachineConfiguration;
import tdd.vendingMachine.dto.ProductImport;
import tdd.vendingMachine.util.TestUtils.TestUtils;
import tdd.vendingMachine.validation.VendingMachineValidator;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 * State representing a machine with a product selected without credit.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({NoCreditSelectedProductState.class, VendingMachineConfiguration.class,
    VendingMachineFactory.class, VendingMachine.class, VendingMachineImpl.class,})
@PowerMockIgnore(value = {"javax.management.*"})
public class NoCreditSelectedProductStateTest implements StateTest {

    private NoCreditSelectedProductState noCreditSelectedProductState;
    private Collection<ProductImport> productImportList;
    private int emptyShelfNumber;
    private int nonEmptyShelfNumber;
    private int otherNonEmptyShelfNumber;
    private Product COLA_199_025;

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
    public NoCreditSelectedProductState transformToAndValidateInitialState(VendingMachine vendingMachine) {
        VendingMachineValidator.validateToReadyState(vendingMachine);
        Assert.assertTrue(vendingMachine.provideCurrentState() instanceof ReadyState);
        ReadyState initialState = (ReadyState) vendingMachine.provideCurrentState();

        //transform to get desired state
        initialState.selectShelfNumber(nonEmptyShelfNumber);

        //validate initial state
        Assert.assertNotNull(vendingMachine.provideSelectedProduct());
        Assert.assertTrue(vendingMachine.provideCurrentState() instanceof NoCreditSelectedProductState);
        return (NoCreditSelectedProductState) vendingMachine.provideCurrentState();
    }

    @Before @Override
    public void setup() {
        COLA_199_025 = new Product(190, "COLA_199_025");
        productImportList = Arrays.asList(
            new ProductImport(COLA_199_025.getType(), COLA_199_025.getPrice(), 1),
            new ProductImport("CHIPS_025",130, 2),
            new ProductImport("CHOCOLATE_BAR", 150, 0));
        nonEmptyShelfNumber = 0;
        otherNonEmptyShelfNumber = 1;
        emptyShelfNumber = 2;
    }

    @After @Override
    public void tearDown() {
        COLA_199_025 = null;
        productImportList = null;
        noCreditSelectedProductState = null;
        nonEmptyShelfNumber = -1;
        emptyShelfNumber = -1;
        otherNonEmptyShelfNumber = -1;
    }

    @Test
    public void should_change_product_selection_valid_shelf_number() throws Exception {
        int productShelfCapacity = 10;
        int coinShelfCapacity = 10;
        int initialCoinsPerShelf = 4;
        int productShelfCount = productImportList.size();

        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, productShelfCount, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelfStubFromProductImports(productImportList, productShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelf = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialCoinsPerShelf);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelf, coinShelf);
        noCreditSelectedProductState = transformToAndValidateInitialState(vendingMachine);
        Product selectedBeforeAttempt = noCreditSelectedProductState.vendingMachine.provideSelectedProduct();

        noCreditSelectedProductState.selectShelfNumber(otherNonEmptyShelfNumber);

        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.PENDING.label));
        Assert.assertNotEquals(selectedBeforeAttempt, noCreditSelectedProductState.vendingMachine.provideSelectedProduct());
        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.provideCurrentState() instanceof NoCreditSelectedProductState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_not_change_product_selection_invalid_shelf_number() throws Exception {
        int productShelfCapacity = 10;
        int coinShelfCapacity = 10;
        int initialCoinsPerShelf = 4;
        int productShelfCount = productImportList.size();

        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, productShelfCount, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelfStubFromProductImports(productImportList, productShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelf = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialCoinsPerShelf);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelf, coinShelf);
        noCreditSelectedProductState = transformToAndValidateInitialState(vendingMachine);
        Product selectedBeforeAttempt = noCreditSelectedProductState.vendingMachine.provideSelectedProduct();

        int invalidShelfNumber = 42342;
        noCreditSelectedProductState.selectShelfNumber(invalidShelfNumber);

        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label));
        Assert.assertEquals(selectedBeforeAttempt, noCreditSelectedProductState.vendingMachine.provideSelectedProduct());
        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.provideCurrentState() instanceof NoCreditSelectedProductState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_not_change_product_selection_empty_shelf_number() throws Exception {
        int productShelfCapacity = 10;
        int coinShelfCapacity = 10;
        int initialCoinsPerShelf = 4;
        int productShelfCount = productImportList.size();

        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, productShelfCount, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelfStubFromProductImports(productImportList, productShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelf = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialCoinsPerShelf);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelf, coinShelf);
        noCreditSelectedProductState = transformToAndValidateInitialState(vendingMachine);
        Product selectedBeforeAttempt = noCreditSelectedProductState.vendingMachine.provideSelectedProduct();

        noCreditSelectedProductState.selectShelfNumber(emptyShelfNumber);

        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.UNABLE_TO_SELECT_EMPTY_SHELF.label));
        Assert.assertEquals(selectedBeforeAttempt, noCreditSelectedProductState.vendingMachine.provideSelectedProduct());
        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.provideCurrentState() instanceof NoCreditSelectedProductState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_skip_cash_inserted_cash_dispenser_full() throws Exception {
        int productShelfCapacity = 10;
        int coinShelfCapacity = 10;
        int productShelfCount = productImportList.size();

        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, productShelfCount, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelfStubFromProductImports(productImportList, productShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelf = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelf, coinShelf);
        noCreditSelectedProductState = transformToAndValidateInitialState(vendingMachine);
        Product selectedBeforeAttempt = noCreditSelectedProductState.vendingMachine.provideSelectedProduct();

        noCreditSelectedProductState.insertCoin(Coin.FIFTY_CENTS);

        Assert.assertEquals(0, noCreditSelectedProductState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(0, noCreditSelectedProductState.vendingMachine.provideCredit());
        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.CASH_NOT_ACCEPTED_DISPENSER_FULL.label));
        Assert.assertEquals(selectedBeforeAttempt, noCreditSelectedProductState.vendingMachine.provideSelectedProduct());
        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.provideCurrentState() instanceof NoCreditSelectedProductState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_return_cash_machine_cant_provide_change_inserted_coin_covers_selected_product_price() throws Exception {
        int productShelfCapacity = 10;
        int coinShelfCapacity = 10;
        int productShelfCount = productImportList.size();
        int initialCoinsDispenser= 0;

        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, productShelfCount, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelfStubFromProductImports(productImportList, productShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelf = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialCoinsDispenser);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelf, coinShelf);
        noCreditSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        noCreditSelectedProductState.insertCoin(Coin.FIVE);

        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.RETURN_TO_BUCKET_CREDIT.label));
        VendingMachineValidator.validateToReadyState(noCreditSelectedProductState.vendingMachine);
        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.provideCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_accept_cash_not_cover_selected_price_change_to_insufficientCreditState() throws Exception {
        int productShelfCapacity = 10;
        int coinShelfCapacity = 10;
        int productShelfCount = productImportList.size();
        int initialCoinsDispenser= 6;

        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, productShelfCount, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelfStubFromProductImports(productImportList, productShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelf = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialCoinsDispenser);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelf, coinShelf);
        noCreditSelectedProductState = transformToAndValidateInitialState(vendingMachine);
        int creditBefore = noCreditSelectedProductState.vendingMachine.provideCredit();
        int sizeCreditBefore = noCreditSelectedProductState.vendingMachine.getCreditStackSize();
        Product product = noCreditSelectedProductState.vendingMachine.provideSelectedProduct();

        Coin twentyCents = Coin.TWENTY_CENTS;
        noCreditSelectedProductState.insertCoin(twentyCents);

        Assert.assertEquals(sizeCreditBefore + 1, noCreditSelectedProductState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(creditBefore + twentyCents.denomination, noCreditSelectedProductState.vendingMachine.provideCredit());
        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.PENDING.label));
        Assert.assertNotNull(noCreditSelectedProductState.vendingMachine.provideSelectedProduct());
        Assert.assertEquals(product, noCreditSelectedProductState.vendingMachine.provideSelectedProduct());
        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.provideCurrentState() instanceof InsufficientCreditState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_sell_product_and_readyState_on_insert_coin_covers_price_return_change() throws Exception {
        int productShelfCapacity = 10;
        int coinShelfCapacity = 10;
        int productShelfCount = productImportList.size();
        int initialCoinsDispenser= 6;

        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, productShelfCount, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelfStubFromProductImports(productImportList, productShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelf = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialCoinsDispenser);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelf, coinShelf);
        noCreditSelectedProductState = transformToAndValidateInitialState(vendingMachine);

        Coin twentyCents = Coin.TWO;
        noCreditSelectedProductState.insertCoin(twentyCents);

        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.DISPENSED_TO_BUCKET.label));
        VendingMachineValidator.validateToReadyState(noCreditSelectedProductState.vendingMachine);
        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.provideCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_cancel_product_selection_move_to_readyState() throws Exception {
        int productShelfCapacity = 10;
        int coinShelfCapacity = 10;
        int productShelfCount = productImportList.size();
        int initialCoinsDispenser= 6;

        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, productShelfCount, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelfStubFromProductImports(productImportList, productShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelf = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialCoinsDispenser);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelf, coinShelf);
        noCreditSelectedProductState = transformToAndValidateInitialState(vendingMachine);


        noCreditSelectedProductState.cancel();

        VendingMachineValidator.validateToReadyState(noCreditSelectedProductState.vendingMachine);
        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.provideCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_send_machine_to_technical_error_state_fail_adding_coins() throws Exception {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        VendingMachine spied = PowerMockito.spy(new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(10, 5)));
        PowerMockito.doThrow(new RuntimeException("fail to error")).when(spied, "addCoinToCredit", fiftyCents);

        noCreditSelectedProductState = new NoCreditSelectedProductState((VendingMachineImpl) spied);

        noCreditSelectedProductState.insertCoin(fiftyCents);

        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.provideCurrentState() instanceof TechnicalErrorState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        Mockito.verify(spied, Mockito.times(1)).addCoinToCredit(fiftyCents);
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_send_machine_to_technical_error_state_fail_selecting_shelf() throws Exception {
        int shelfNumber = 0;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        VendingMachine spied = PowerMockito.spy(new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(10, 5)));
        PowerMockito.doThrow(new RuntimeException("fail to error")).when(spied, "selectProductGivenShelfNumber", shelfNumber);

        noCreditSelectedProductState = new NoCreditSelectedProductState((VendingMachineImpl) spied);

        noCreditSelectedProductState.selectShelfNumber(shelfNumber);

        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.provideCurrentState() instanceof TechnicalErrorState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        Mockito.verify(spied, Mockito.times(1)).selectProductGivenShelfNumber(shelfNumber);
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_send_machine_to_technical_error_state_fail_on_cancel() throws Exception {
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        VendingMachine spied = PowerMockito.spy(new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(10, 5)));
        PowerMockito.doThrow(new RuntimeException("fail to error")).when(spied, "undoProductSelection");

        noCreditSelectedProductState = new NoCreditSelectedProductState((VendingMachineImpl) spied);

        noCreditSelectedProductState.cancel();

        Assert.assertTrue(noCreditSelectedProductState.vendingMachine.provideCurrentState() instanceof TechnicalErrorState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        Mockito.verify(spied, Mockito.times(1)).undoProductSelection();
        verifyConfigMock(configMock, 3, 2, 2);
    }

}
