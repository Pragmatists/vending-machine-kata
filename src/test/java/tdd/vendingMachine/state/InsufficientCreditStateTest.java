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
import tdd.vendingMachine.util.TestUtils.TestUtils;
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
@PrepareForTest({InsufficientCreditState.class, VendingMachineConfiguration.class, VendingMachineFactory.class, VendingMachine.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class InsufficientCreditStateTest implements StateTest {

    private InsufficientCreditState insufficientCreditState;
    private List<Product> products;

    @Override
    public InsufficientCreditState transformToInitialState(VendingMachine vendingMachine) {
        Assert.assertEquals(0, vendingMachine.getCredit()); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof ReadyState);
        ReadyState initialState = (ReadyState) vendingMachine.getCurrentState();

        //modify to get desired state
        Coin tenCents = Coin.TEN_CENTS;
        initialState.insertCoin(tenCents);
        Assert.assertTrue(initialState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);
        initialState.vendingMachine.getCurrentState().selectShelfNumber(1);

        //validate initial state
        Assert.assertEquals(tenCents.denomination, initialState.vendingMachine.getCredit());
        Assert.assertNotNull(initialState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(initialState.vendingMachine.getCurrentState() instanceof InsufficientCreditState);
        return (InsufficientCreditState) initialState.vendingMachine.getCurrentState();
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
    public void should_return_all_credit_after_cancel_operation_and_change_state_to_NoCreditNoProductSelected() throws Exception {
        VendingMachineConfiguration mockConfig = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(products, 3);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildCoinDispenserWithGivenItemsPerShelf(mockConfig, 5);
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
        insufficientCreditState = transformToInitialState(vendingMachine);

        insufficientCreditState.cancel();

        Assert.assertEquals(0, insufficientCreditState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(0, insufficientCreditState.vendingMachine.getCredit());
        Assert.assertTrue(insufficientCreditState.vendingMachine.getCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 2, 2, 1);
    }

   @Test
    public void given_invalid_shelfNumber_should_provide_error_message_and_remain_same_state() throws Exception {
       VendingMachineConfiguration mockConfig = getConfigMock(10, 10, 10);
       PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

       VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
       Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(products, 3);
       Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildCoinDispenserWithGivenItemsPerShelf(mockConfig, 5);
       VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
       insufficientCreditState = transformToInitialState(vendingMachine);

        insufficientCreditState.selectShelfNumber(123);
        Assert.assertTrue(insufficientCreditState.vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label));
        Assert.assertTrue(insufficientCreditState.vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.PENDING.label));
        Assert.assertTrue(insufficientCreditState.vendingMachine.getCurrentState() instanceof InsufficientCreditState);

       PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
       verifyConfigMock(mockConfig, 2, 2, 1);
    }

    @Test
    public void given_valid_shelfNumber_that_price_is_equally_covered_with_current_credit_should_dispense_item() throws Exception {
        int shelfNumberEnoughCash = 1;
        int shelfNumberInsufficientCash = 0;
        VendingMachineConfiguration mockConfig = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(products, 3);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildCoinDispenserWithGivenItemsPerShelf(mockConfig, 5);
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
        insufficientCreditState = transformToInitialState(vendingMachine);

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
        Assert.assertTrue(insufficientCreditState.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, insufficientCreditState.vendingMachine.getCredit());
        Assert.assertTrue(insufficientCreditState.vendingMachine.getCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 2, 2, 1);
    }

    @Test
    public void given_valid_shelfNumber_that_price_is_over_covered_with_current_credit_should_dispense_item_and_provide_change() throws Exception {
        int shelfNumberEnoughCash = 1;
        int shelfNumberInsufficientCash = 0;
        VendingMachineConfiguration mockConfig = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(products, 3);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildCoinDispenserWithGivenItemsPerShelf(mockConfig, 5);
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
        insufficientCreditState = transformToInitialState(vendingMachine);

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
        Assert.assertTrue(insufficientCreditState.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, insufficientCreditState.vendingMachine.getCredit());
        Assert.assertTrue(insufficientCreditState.vendingMachine.getCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 2, 2, 1);
    }

    @Test
    public void should_give_all_credit_back_since_is_not_possible_to_give_change_when_amount_reached() throws Exception {
        int shelfNumberEnoughCash = 1;
        int shelfNumberInsufficientCash = 0;
        VendingMachineConfiguration mockConfig = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
        Collection<Product> myProducts = Arrays.asList(new Product(180, "p1"), new Product(190, "p2"));
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(myProducts, 3);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildCoinDispenserWithGivenItemsPerShelf(mockConfig, 0);
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
        insufficientCreditState = transformToInitialState(vendingMachine);

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
        Assert.assertTrue(insufficientCreditState.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, insufficientCreditState.vendingMachine.getCredit());
        Assert.assertTrue(insufficientCreditState.vendingMachine.getCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 2, 2, 1);
    }



    @Test
    public void should_give_all_credit_back_since_is_not_possible_to_give_change_when_product_selection_changes_to_cover_priced_product() throws Exception {
        int shelfNumberEnoughCash = 1;
        int shelfNumberInsufficientCash = 0;
        VendingMachineConfiguration mockConfig = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
        Collection<Product> myProducts = Arrays.asList(new Product(180, "p1"), new Product(600, "p2"));
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(myProducts, 3);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildCoinDispenserWithGivenItemsPerShelf(mockConfig, 0);
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(productShelves, coinShelves);
        insufficientCreditState = transformToInitialState(vendingMachine);

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
        Assert.assertEquals(0, insufficientCreditState.vendingMachine.getCredit());
        Assert.assertTrue(insufficientCreditState.vendingMachine.getCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 2, 2, 1);
    }
}
