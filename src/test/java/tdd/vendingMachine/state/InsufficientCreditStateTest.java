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
import java.util.List;
import java.util.Map;


/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({InsufficientCreditState.class, VendingMachineConfiguration.class,
    VendingMachineFactory.class, VendingMachine.class, VendingMachineImpl.class, State.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class InsufficientCreditStateTest implements StateTest {

    private InsufficientCreditState insufficientCreditState;
    private List<Product> products;

    @Override
    public InsufficientCreditState transformToAndValidateInitialState(VendingMachine vendingMachine) {
        VendingMachineValidator.validateToReadyState(vendingMachine);
        Assert.assertTrue(vendingMachine.provideCurrentState() instanceof ReadyState);
        ReadyState initialState = (ReadyState) vendingMachine.provideCurrentState();

        //modify to get desired state
        Coin tenCents = Coin.TEN_CENTS;
        initialState.insertCoin(tenCents);
        Assert.assertTrue(vendingMachine.provideCurrentState() instanceof CreditNotSelectedProductState);
        vendingMachine.provideCurrentState().selectShelfNumber(1);

        //validate initial state
        Assert.assertEquals(tenCents.denomination, vendingMachine.provideCredit());
        Assert.assertNotNull(vendingMachine.provideSelectedProduct());
        Assert.assertTrue(vendingMachine.provideCurrentState() instanceof InsufficientCreditState);
        return (InsufficientCreditState) vendingMachine.provideCurrentState();
    }

    @Before @Override
    public void setup() {
        products = Arrays.asList(new Product(290, "COLA_199_025"), new Product(130, "CHIPS_025"), new Product(160, "CHOCOLATE_BAR"));
    }

    @After @Override
    public void tearDown() {
        products = null;
        insufficientCreditState = null;
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



    @Test
    public void should_give_all_credit_back_since_is_not_possible_to_give_change_when_product_selection_changes_to_cover_priced_product() throws Exception {
        int shelfNumberEnoughCash = 1;
        int shelfNumberInsufficientCash = 0;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
        Collection<Product> myProducts = Arrays.asList(new Product(180, "p1"), new Product(600, "p2"));
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(myProducts, 3, 10);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 0);
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
        insufficientCreditState = transformToAndValidateInitialState(vendingMachine);

        int productsBeforeSell = insufficientCreditState.vendingMachine.countTotalAmountProducts();
        int productsBeforeSellOnShelfEnoughCash = insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberEnoughCash);
        int productsBeforeSellOnShelfInsufficientCash = insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberInsufficientCash);

        insufficientCreditState.insertCoin(Coin.FIVE);
        insufficientCreditState.selectShelfNumber(shelfNumberInsufficientCash);

        //inventory validation
        Assert.assertEquals(productsBeforeSellOnShelfEnoughCash, insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberEnoughCash));
        Assert.assertEquals(productsBeforeSellOnShelfInsufficientCash, insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberInsufficientCash));
        Assert.assertEquals(productsBeforeSell, insufficientCreditState.vendingMachine.countTotalAmountProducts());

        //cash validation
        Assert.assertTrue(insufficientCreditState.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, insufficientCreditState.vendingMachine.provideCredit());

        //state validation
        VendingMachineValidator.validateToReadyState(insufficientCreditState.vendingMachine);
        Assert.assertTrue(insufficientCreditState.vendingMachine.provideCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 3, 2, 2);
    }

    @Test
    public void should_return_all_credit_after_cancel_operation_and_change_state_to_NoCreditNoProductSelected() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(products, 3, 10);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 5);
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
        insufficientCreditState = transformToAndValidateInitialState(vendingMachine);

        insufficientCreditState.cancel();

        VendingMachineValidator.validateToReadyState(insufficientCreditState.vendingMachine);
        Assert.assertTrue(insufficientCreditState.vendingMachine.provideCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 3, 2, 2);
    }

   @Test
    public void given_invalid_shelfNumber_should_provide_error_message_and_remain_same_state() throws Exception {
       int coinShelfCapacity = 10;
       VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
       PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

       VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
       Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(products, 3, 10);
       Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 5);
       VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
       insufficientCreditState = transformToAndValidateInitialState(vendingMachine);

        insufficientCreditState.selectShelfNumber(123);
        Assert.assertTrue(insufficientCreditState.vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label));
        Assert.assertTrue(insufficientCreditState.vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.PENDING.label));
        Assert.assertTrue(insufficientCreditState.vendingMachine.provideCurrentState() instanceof InsufficientCreditState);

       PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
       verifyConfigMock(mockConfig, 3, 2, 2);
    }

    @Test
    public void given_valid_shelfNumber_that_price_is_equally_covered_with_current_credit_should_dispense_item() throws Exception {
        int shelfNumberEnoughCash = 1;
        int shelfNumberInsufficientCash = 0;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(products, 3, 10);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 5);
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
        insufficientCreditState = transformToAndValidateInitialState(vendingMachine);

        int productsBeforeSell = insufficientCreditState.vendingMachine.countTotalAmountProducts();
        int productsBeforeSellOnShelfEnoughCash = insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberEnoughCash);
        int productsBeforeSellOnShelfInsufficientCash = insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberInsufficientCash);

        insufficientCreditState.selectShelfNumber(shelfNumberInsufficientCash);
        insufficientCreditState.insertCoin(Coin.TWENTY_CENTS);
        insufficientCreditState.insertCoin(Coin.ONE);
        insufficientCreditState.selectShelfNumber(shelfNumberEnoughCash);

        //inventory validation
        Assert.assertEquals(productsBeforeSellOnShelfEnoughCash - 1, insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberEnoughCash));
        Assert.assertEquals(productsBeforeSellOnShelfInsufficientCash, insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberInsufficientCash));
        Assert.assertEquals(productsBeforeSell - 1, insufficientCreditState.vendingMachine.countTotalAmountProducts());

        //cash validation
        VendingMachineValidator.validateToReadyState(insufficientCreditState.vendingMachine);
        Assert.assertTrue(insufficientCreditState.vendingMachine.provideCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 3, 2, 2);
    }

    @Test
    public void given_valid_shelfNumber_that_price_is_over_covered_with_current_credit_should_dispense_item_and_provide_change() throws Exception {
        int shelfNumberEnoughCash = 1;
        int shelfNumberInsufficientCash = 0;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(products, 3, 10);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 5);
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
        insufficientCreditState = transformToAndValidateInitialState(vendingMachine);

        int productsBeforeSell = insufficientCreditState.vendingMachine.countTotalAmountProducts();
        int productsBeforeSellOnShelfEnoughCash = insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberEnoughCash);
        int productsBeforeSellOnShelfInsufficientCash = insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberInsufficientCash);

        insufficientCreditState.selectShelfNumber(shelfNumberInsufficientCash);
        insufficientCreditState.insertCoin(Coin.TWENTY_CENTS);
        insufficientCreditState.insertCoin(Coin.ONE);
        insufficientCreditState.insertCoin(Coin.TEN_CENTS);
        insufficientCreditState.selectShelfNumber(shelfNumberEnoughCash);

        //inventory validation
        Assert.assertEquals(productsBeforeSellOnShelfEnoughCash - 1, insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberEnoughCash));
        Assert.assertEquals(productsBeforeSellOnShelfInsufficientCash, insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberInsufficientCash));
        Assert.assertEquals(productsBeforeSell - 1, insufficientCreditState.vendingMachine.countTotalAmountProducts());

        //cash validation
        VendingMachineValidator.validateToReadyState(insufficientCreditState.vendingMachine);
        Assert.assertTrue(insufficientCreditState.vendingMachine.provideCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 3, 2, 2);
    }

    @Test
    public void should_give_all_credit_back_since_is_not_possible_to_give_change_when_amount_reached() throws Exception {
        int shelfNumberEnoughCash = 1;
        int shelfNumberInsufficientCash = 0;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
        Collection<Product> myProducts = Arrays.asList(new Product(180, "p1"), new Product(190, "p2"));
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(myProducts, 3, 10);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, 0);
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
        insufficientCreditState = transformToAndValidateInitialState(vendingMachine);

        int productsBeforeSell = insufficientCreditState.vendingMachine.countTotalAmountProducts();
        int productsBeforeSellOnShelfEnoughCash = insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberEnoughCash);
        int productsBeforeSellOnShelfInsufficientCash = insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberInsufficientCash);

        insufficientCreditState.selectShelfNumber(shelfNumberInsufficientCash);
        insufficientCreditState.insertCoin(Coin.FIVE);

        //inventory validation
        Assert.assertEquals(productsBeforeSellOnShelfEnoughCash, insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberEnoughCash));
        Assert.assertEquals(productsBeforeSellOnShelfInsufficientCash, insufficientCreditState.vendingMachine.countProductsOnShelf(shelfNumberInsufficientCash));
        Assert.assertEquals(productsBeforeSell, insufficientCreditState.vendingMachine.countTotalAmountProducts());

        //cash validation
        VendingMachineValidator.validateToReadyState(insufficientCreditState.vendingMachine);
        Assert.assertTrue(insufficientCreditState.vendingMachine.provideCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 3, 2, 2);
    }

    @Test
    public void should_skip_inserting_coin_on_full_dispenser() throws Exception {
        int coinShelfCapacity = 5;
        int productShelfCount = 5;
        int productShelfCapacity = 10;
        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, productShelfCount, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        int actualProductShelfCapacity = 5;
        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelfStubFromProductImports(
            Arrays.asList(new ProductImport("p1", 100, 0), new ProductImport("p1", 100, 1)), actualProductShelfCapacity);
        Map<Coin, Shelf<Coin>> fullShelfDispenser = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity - 1);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelf, fullShelfDispenser);
        insufficientCreditState = transformToAndValidateInitialState(vendingMachine);
        int stackBeforeInserting = vendingMachine.getCreditStackSize();
        int creditBeforeAttempt = vendingMachine.provideCredit();

        insufficientCreditState.insertCoin(Coin.TEN_CENTS);

        Assert.assertEquals(stackBeforeInserting, vendingMachine.getCreditStackSize());
        Assert.assertEquals(creditBeforeAttempt, vendingMachine.provideCredit());
        Assert.assertTrue(vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.CASH_NOT_ACCEPTED_DISPENSER_FULL.label));

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_not_change_selected_product_when_new_selected_shelf_empty() throws Exception {
        int coinShelfCapacity = 5;
        int productShelfCount = 5;
        int productShelfCapacity = 10;
        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, productShelfCount, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        int emptyShelfId = 0;

        int actualProductShelfCapacity = 5;
        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelfStubFromProductImports(
            Arrays.asList(new ProductImport("p1", 100, 0), new ProductImport("p2", 100, 1)), actualProductShelfCapacity);
        Map<Coin, Shelf<Coin>> fullShelfDispenser = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity - 1);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelf, fullShelfDispenser);
        insufficientCreditState = transformToAndValidateInitialState(vendingMachine);
        Product productBeforeAttempt = vendingMachine.provideSelectedProduct();

        insufficientCreditState.selectShelfNumber(emptyShelfId);

        Assert.assertEquals(productBeforeAttempt, vendingMachine.provideSelectedProduct());
        Assert.assertTrue(vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.UNABLE_TO_SELECT_EMPTY_SHELF.label));

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_send_to_technical_error_state_on_select_shelf() throws Exception {
        int nonEmptyShelfId = 1;
        int coinShelfCapacity = 5;
        int productShelfCount = 5;
        int productShelfCapacity = 10;
        int actualProductShelfCapacity = 5;
        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, productShelfCount, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelfStubFromProductImports(
            Arrays.asList(new ProductImport("p1", 100, 0), new ProductImport("p2", 100, 1)), actualProductShelfCapacity);
        Map<Coin, Shelf<Coin>> fullShelfDispenser = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity - 1);
        VendingMachine spied = PowerMockito.spy(new VendingMachineFactory().customVendingMachineForTesting(productShelf, fullShelfDispenser));
        PowerMockito.doThrow(new UnsupportedOperationException("not valid state for sell")).when(spied, "selectProductGivenShelfNumber", nonEmptyShelfId);

        insufficientCreditState = new InsufficientCreditState((VendingMachineImpl) spied);

        insufficientCreditState.selectShelfNumber(nonEmptyShelfId);

        Assert.assertTrue(insufficientCreditState.vendingMachine.provideCurrentState() instanceof TechnicalErrorState);
        Mockito.verify(spied, Mockito.times(1)).selectProductGivenShelfNumber(nonEmptyShelfId);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_send_machine_to_technical_error_state_fail_adding_coins() throws Exception {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        VendingMachine spied = PowerMockito.spy(new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(products.get(0), 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(10, 5)));
        PowerMockito.doThrow(new RuntimeException("fail to error")).when(spied, "addCoinToCredit", fiftyCents);

        insufficientCreditState = new InsufficientCreditState((VendingMachineImpl) spied);

        insufficientCreditState.insertCoin(fiftyCents);

        Assert.assertTrue(insufficientCreditState.vendingMachine.provideCurrentState() instanceof TechnicalErrorState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        Mockito.verify(spied, Mockito.times(1)).addCoinToCredit(fiftyCents);
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_send_machine_to_technical_error_state_fail_selecting_shelf() throws Exception {
        int shelfNumber = 0;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        VendingMachine spied = PowerMockito.spy(new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(products.get(0), 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(10, 5)));
        PowerMockito.doThrow(new RuntimeException("fail to error")).when(spied, "selectProductGivenShelfNumber", shelfNumber);

        insufficientCreditState = new InsufficientCreditState((VendingMachineImpl) spied);

        insufficientCreditState.selectShelfNumber(shelfNumber);

        Assert.assertTrue(insufficientCreditState.vendingMachine.provideCurrentState() instanceof TechnicalErrorState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        Mockito.verify(spied, Mockito.times(1)).selectProductGivenShelfNumber(shelfNumber);
        verifyConfigMock(configMock, 3, 2, 2);
    }

    @Test
    public void should_send_machine_to_technical_error_state_fail_on_cancel() throws Exception {
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        VendingMachine spied = PowerMockito.spy(new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(products.get(0), 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(10, 5)));
        PowerMockito.doThrow(new RuntimeException("fail to error")).when(spied, "returnAllCreditToBucket");

        insufficientCreditState = new InsufficientCreditState((VendingMachineImpl) spied);

        insufficientCreditState.cancel();

        Assert.assertTrue(insufficientCreditState.vendingMachine.provideCurrentState() instanceof TechnicalErrorState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        Mockito.verify(spied, Mockito.times(1)).returnAllCreditToBucket();
        verifyConfigMock(configMock, 3, 2, 2);
    }
}
