package tdd.vendingMachine.validation;

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
import tdd.vendingMachine.domain.*;
import tdd.vendingMachine.domain.exception.InvalidShelfSizeException;
import tdd.vendingMachine.dto.ProductImport;
import tdd.vendingMachine.util.TestUtils.TestUtils;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.Collections;
import java.util.Map;

/**
 * @author Agustin on 2/26/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachineValidator.class, VendingMachineConfiguration.class, VendingMachine.class, TestUtils.class,
    ShelfFactory.class, Product.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class VendingMachineValidatorTest {

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

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void should_fail_null_configuration() {
        boolean exceptionThrown = false;
        try {
            VendingMachineValidator.validateNewVendingMachineParameters(null, null, null);
        } catch (NullPointerException e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
    }

    @Test
    public void should_fail_null_product_shelves() {
        boolean exceptionThrown = false;
        VendingMachineConfiguration mockConfig = getConfigMock(1, 1, 1);
        try {
            VendingMachineValidator.validateNewVendingMachineParameters(mockConfig, null, null);
        } catch (NullPointerException e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
        verifyConfigMock(mockConfig, 0, 0, 0);
    }

    @Test
    public void should_fail_null_coin_shelves() {
        boolean exceptionThrown = false;
        VendingMachineConfiguration mockConfig = getConfigMock(1, 1, 1);

        try {
            VendingMachineValidator.validateNewVendingMachineParameters(mockConfig, TestUtils.buildShelvesWithItems(new Product(100, "p1"), 1), null);
        } catch (NullPointerException e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);

        verifyConfigMock(mockConfig, 0, 0, 0);
    }

    @Test
    public void should_fail_no_shelves_available_in_coin_dispenser() {
        boolean exceptionThrown = false;
        int coinShelfCapacity = 1;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 1, 1);
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelfStubFromProductImports(
            Collections.singleton(new ProductImport("p1", 100, 1)), 5);
        Map<Coin, Shelf<Coin>> coinShelves = Collections.emptyMap();
        try {
            VendingMachineValidator.validateNewVendingMachineParameters(mockConfig, productShelves, coinShelves);
        } catch (InvalidShelfSizeException invalidShelfException) {
            exceptionThrown = true;
            Assert.assertEquals(VendingMachineMessages.COIN_SHELF_SIZE_INCOMPATIBLE.label, invalidShelfException.getMessage());
            Assert.assertEquals(Coin.values().length, invalidShelfException.getMaximumSize());
            Assert.assertEquals(coinShelves.size(), invalidShelfException.getGivenSize());
        }
        Assert.assertTrue(exceptionThrown);

        verifyConfigMock(mockConfig, 0, 0, 0);
    }

    @Test
    public void should_fail_coin_dispenser_exceeds_capacity() throws Exception {
        boolean exceptionThrown = false;
        int configCoinShelfCapacity = 1;
        int actualShelfCapacity = 2;
        VendingMachineConfiguration mockConfigValidation = getConfigMock(configCoinShelfCapacity, 1, 1);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelfStubFromProductImports(Collections.singleton(new ProductImport("p1", 100, 1)), 5);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(actualShelfCapacity, 2);

        try {
            VendingMachineValidator.validateNewVendingMachineParameters(mockConfigValidation, productShelves, coinShelves);
        } catch (InvalidShelfSizeException invalidShelfException) {
            exceptionThrown = true;
            Assert.assertEquals(VendingMachineMessages.UNABLE_TO_CREATE_VENDING_MACHINE_EXCEEDED_COIN_SHELF_CAPACITY.label, invalidShelfException.getMessage());
            Assert.assertEquals(configCoinShelfCapacity, invalidShelfException.getMaximumSize());
            Assert.assertEquals(actualShelfCapacity, invalidShelfException.getGivenSize());
        }

        Assert.assertTrue(exceptionThrown);
        verifyConfigMock(mockConfigValidation, 2, 0, 0);
    }

    @Test
    public void should_fail_product_shelves_exceeds_config_limit() throws Exception {
        boolean exceptionThrown = false;
        int configCoinShelfCapacity = 2;
        int actualCoinShelfCapacity = 2;
        int currentProductShelfCount = 10;
        int configProductShelfCount = 1;
        VendingMachineConfiguration mockConfigValidation = getConfigMock(configCoinShelfCapacity, configProductShelfCount, 1);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(TestUtils.buildStubListOfProducts(currentProductShelfCount), 5, 10);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(actualCoinShelfCapacity, 2);

        try {
            VendingMachineValidator.validateNewVendingMachineParameters(mockConfigValidation, productShelves, coinShelves);
        } catch (InvalidShelfSizeException invalidShelfException) {
            exceptionThrown = true;
            Assert.assertEquals(VendingMachineMessages.PRODUCT_SHELF_SIZE_EXCEEDS_MAX.label, invalidShelfException.getMessage());
            Assert.assertEquals(configProductShelfCount, invalidShelfException.getMaximumSize());
            Assert.assertEquals(currentProductShelfCount, invalidShelfException.getGivenSize());
        }

        Assert.assertTrue(exceptionThrown);
        verifyConfigMock(mockConfigValidation, 1, 2, 0);
    }

    @Test
    public void should_fail_product_shelves_has_shelf_exceeding_capacity() throws Exception {
        boolean exceptionThrown = false;
        int configCoinShelfCapacity = 2;
        int actualCoinShelfCapacity = 2;
        int actualProductShelfCount = 10;
        int configProductShelfCount = 10;
        int configProductShelfCapacity = 1;
        int actualProductShelfCapacity = 10;
        VendingMachineConfiguration mockConfigValidation = getConfigMock(configCoinShelfCapacity, configProductShelfCount, configProductShelfCapacity);

        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(
            TestUtils.buildStubListOfProducts(actualProductShelfCount), 5, actualProductShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(actualCoinShelfCapacity, 2);

        try {
            VendingMachineValidator.validateNewVendingMachineParameters(mockConfigValidation, productShelves, coinShelves);
        } catch (InvalidShelfSizeException invalidShelfException) {
            exceptionThrown = true;
            Assert.assertEquals(VendingMachineMessages.UNABLE_TO_CREATE_VENDING_MACHINE_EXCEEDED_PRODUCT_SHELF_CAPACITY.label, invalidShelfException.getMessage());
            Assert.assertEquals(configProductShelfCapacity, invalidShelfException.getMaximumSize());
            Assert.assertEquals(actualProductShelfCapacity, invalidShelfException.getGivenSize());
        }

        Assert.assertTrue(exceptionThrown);
        verifyConfigMock(mockConfigValidation, 1, 1, 2);
    }

    @Test
    public void should_not_fail_valid_input() {
        int configProductShelfCapacity = 5;
        int configProductShelfCount = 3;
        int configCoinShelfCapacity = 10;

        VendingMachineConfiguration configMock = getConfigMock(configCoinShelfCapacity, configProductShelfCount, configProductShelfCapacity);
        Map<Integer, Shelf<Product>> productShelves = TestUtils.buildShelvesWithItems(TestUtils.buildStubListOfProducts(3), 2, configProductShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(configCoinShelfCapacity, 5);
        VendingMachineValidator.validateNewVendingMachineParameters(configMock, productShelves, coinShelves);

        verifyConfigMock(configMock, 1, 1, 1);
    }

    /**
     * Establishes the returning values for the respective methods on the vending machine mock
     * @param isSoldOut return value for isSoldOut
     * @param credit return value for getCredit
     * @param isStackEmpty return value for isStackEmpty
     * @param selectedProduct return value for getSelectedProduct
     * @return a mock of the vending machine with given return values for specific methods
     * @throws Exception
     */
    private VendingMachine provideMockReturningValuesForState(boolean isSoldOut, int credit, boolean isStackEmpty, Product selectedProduct)  throws Exception {
        VendingMachine vendingMachineMock  = PowerMockito.mock(VendingMachine.class);
        PowerMockito.when(vendingMachineMock, "isSoldOut").thenReturn(isSoldOut);
        PowerMockito.when(vendingMachineMock, "getCredit").thenReturn(credit);
        PowerMockito.when(vendingMachineMock, "isCreditStackEmpty").thenReturn(isStackEmpty);
        PowerMockito.when(vendingMachineMock, "getSelectedProduct").thenReturn(selectedProduct);
        return vendingMachineMock;
    }

    /**
     * Verifies calls to mock methods
     * @param vendingMachineMock the mock to validate
     * @param exceptionRequired if an exception was expected
     * @param exceptionThrown if an exception was thrown
     * @param amountTotalProducts calls to countTotalAmountProducts
     * @param amountCallsCredit calls to getCredit
     * @param amountCallsStackEmpty calls to isStackEmpty
     * @param amountCallsSelectedProduct calls to getSelectedProduct
     */
    private void verifyMockExecutionForState(VendingMachine vendingMachineMock, boolean exceptionRequired, boolean exceptionThrown,
                                             int amountTotalProducts, int amountCallsCredit, int amountCallsStackEmpty, int amountCallsSelectedProduct) {
        if (exceptionRequired) Assert.assertTrue(exceptionThrown);
        Mockito.verify(vendingMachineMock, Mockito.times(amountTotalProducts)).isSoldOut();
        Mockito.verify(vendingMachineMock, Mockito.times(amountCallsCredit)).getCredit();
        Mockito.verify(vendingMachineMock, Mockito.times(amountCallsStackEmpty)).isCreditStackEmpty();
        Mockito.verify(vendingMachineMock, Mockito.times(amountCallsSelectedProduct)).getSelectedProduct();
    }

    @Test
    public void should_fail_machine_is_sold_out() throws Exception {
        VendingMachine vendingMachineMock  = provideMockReturningValuesForState(true, 0, false, null);
        boolean exceptionThrown = false;
        try {
            VendingMachineValidator.validateToReadyStateMachine(vendingMachineMock);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        verifyMockExecutionForState(vendingMachineMock, true, exceptionThrown, 1, 1, 1, 1);
    }

    @Test
    public void should_fail_machine_has_credit() throws Exception {
        VendingMachine vendingMachineMock  = provideMockReturningValuesForState(false, 100, false, null);
        boolean exceptionThrown = false;
        try {
            VendingMachineValidator.validateToReadyStateMachine(vendingMachineMock);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        verifyMockExecutionForState(vendingMachineMock, true, exceptionThrown,1, 2, 1, 1);
    }

    @Test
    public void should_fail_machine_has_credit_stack() throws Exception {
        VendingMachine vendingMachineMock  = provideMockReturningValuesForState(false, 0, false, null);
        boolean exceptionThrown = false;
        try {
            VendingMachineValidator.validateToReadyStateMachine(vendingMachineMock);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        verifyMockExecutionForState(vendingMachineMock, true, exceptionThrown,1, 2, 2, 1);
    }

    @Test
    public void should_fail_machine_has_product_selected() throws Exception {
        VendingMachine vendingMachineMock  = provideMockReturningValuesForState(false, 0, true, Mockito.mock(Product.class));
        boolean exceptionThrown = false;
        try {
            VendingMachineValidator.validateToReadyStateMachine(vendingMachineMock);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        verifyMockExecutionForState(vendingMachineMock, true, exceptionThrown,1, 2, 2, 2);
    }

    @Test
    public void should_validate_machine_qualifies_to_ready() throws Exception {
        VendingMachine vendingMachineMock  = provideMockReturningValuesForState(false, 0, true, null);
        VendingMachineValidator.validateToReadyStateMachine(vendingMachineMock);
        verifyMockExecutionForState(vendingMachineMock, false, false, 1, 1, 1, 1);
    }
}
