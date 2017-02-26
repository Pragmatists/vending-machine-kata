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
import tdd.vendingMachine.util.TestUtils.TestUtils;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachine.class, VendingMachineConfiguration.class, VendingMachineFactory.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class ReadyStateTest implements StateTest {


    private Product COLA_199_025;
    private Product CHOCOLATE_BAR;
    private ReadyState readyState;

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
    public ReadyState transformToAndValidateInitialState(VendingMachine vendingMachine) {
        Assert.assertEquals(0, vendingMachine.getCredit()); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof ReadyState);
        return (ReadyState) vendingMachine.getCurrentState();
    }

    @Before @Override
    public void setup(){
        COLA_199_025 = new Product(190, "COLA_199_025");
        CHOCOLATE_BAR = new Product(160, "CHOCOLATE_BAR");
    }

    @After @Override
    public void tearDown(){
        COLA_199_025 = null;
        CHOCOLATE_BAR = null;
        readyState = null;
    }

    @Test
    public void should_change_state_after_inserting_coin_on_machine_with_dispenser_available() throws Exception {
        int initialCoinsOnShelf = 5;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialCoinsOnShelf));
        readyState = transformToAndValidateInitialState(vendingMachine);

        Coin tenCents = Coin.TEN_CENTS;

        readyState.insertCoin(tenCents);

        Assert.assertEquals(tenCents.denomination, readyState.vendingMachine.getCredit());
        Assert.assertTrue(readyState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 3, 2, 2);
    }

    @Test
    public void should_not_change_state_and_skip_coin_insertions_when_coin_dispenser_full() throws Exception {
        Coin tenCents = Coin.TEN_CENTS;

        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);

        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(CHOCOLATE_BAR, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity));
        readyState = transformToAndValidateInitialState(vendingMachine);

        readyState.insertCoin(tenCents);

        Assert.assertEquals(0, readyState.vendingMachine.getCredit());
        Assert.assertEquals(0, readyState.vendingMachine.getCreditStackSize());
        Assert.assertTrue(readyState.vendingMachine.getCurrentState() instanceof ReadyState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 3, 2, 2);
    }

    @Test
    public void should_change_state_after_selecting_valid_shelfNumber_and_display_pending_balance_for_item() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(CHOCOLATE_BAR, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity));
        readyState = transformToAndValidateInitialState(vendingMachine);

        readyState.selectShelfNumber(0);
        Assert.assertTrue(readyState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.PENDING.label));
        Assert.assertNotNull(readyState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(readyState.vendingMachine.getCurrentState() instanceof NoCreditSelectedProductState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 3, 2, 2);
    }

    @Test
    public void should_not_change_state_after_selecting_invalid_shelfNumber() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(CHOCOLATE_BAR, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity));
        readyState = transformToAndValidateInitialState(vendingMachine);

        readyState.selectShelfNumber(5515);
        transformToAndValidateInitialState(readyState.vendingMachine);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 3, 2, 2);
    }

    @Test
    public void should_not_change_state_after_selecting_empty_shelfNumber() throws Exception {
        int productShelfCapacity = 10;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, productShelfCapacity);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);
        Collection<ProductImport> products = Arrays.asList(new ProductImport(CHOCOLATE_BAR.getType(), CHOCOLATE_BAR.getPrice(), 1),
                                                            new ProductImport(COLA_199_025.getType(), COLA_199_025.getPrice(), 0));

        Map<Integer, Shelf<Product>> productImportStub = TestUtils.buildShelfStubFromProductImports(products, productShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productImportStub, coinShelves);
        readyState = transformToAndValidateInitialState(vendingMachine);
        int emptyShelfId = 1;

        readyState.selectShelfNumber(emptyShelfId);
        transformToAndValidateInitialState(readyState.vendingMachine);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 3, 2, 2);
    }

    @Test
    public void should_not_change_state_after_cancel() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration mockConfig = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(mockConfig);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(CHOCOLATE_BAR, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity));
        readyState = transformToAndValidateInitialState(vendingMachine);

        readyState.cancel();
        transformToAndValidateInitialState(readyState.vendingMachine);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        verifyConfigMock(mockConfig, 3, 2, 2);
    }
}
