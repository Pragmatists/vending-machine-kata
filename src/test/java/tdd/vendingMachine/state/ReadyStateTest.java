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
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Shelf;
import tdd.vendingMachine.domain.VendingMachineConfiguration;
import tdd.vendingMachine.dto.ProductImport;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.util.TestUtils.TestUtils;
import tdd.vendingMachine.validation.VendingMachineValidator;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachine.class, VendingMachineImpl.class, VendingMachineConfiguration.class, VendingMachineFactory.class})
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
        VendingMachineValidator.validateToReadyState(vendingMachine);
        Assert.assertTrue(vendingMachine.provideCurrentState() instanceof ReadyState);
        return (ReadyState) vendingMachine.provideCurrentState();
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
        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.spy(VendingMachineFactory.class);
        PowerMockito.when(VendingMachineFactory.getConfig()).thenReturn(configMock);

        VendingMachine vendingMachine = VendingMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialCoinsOnShelf));
        readyState = transformToAndValidateInitialState(vendingMachine);

        Coin tenCents = Coin.TEN_CENTS;

        readyState.insertCoin(tenCents);

        Assert.assertEquals(tenCents.denomination, readyState.vendingMachine.provideCredit());
        Assert.assertTrue(readyState.vendingMachine.provideCurrentState() instanceof CreditNotSelectedProductState);

        PowerMockito.verifyStatic(Mockito.times(1));
        VendingMachineFactory.getConfig();
        verifyConfigMock(configMock, 1, 1, 1);
    }

    @Test
    public void should_not_change_state_and_skip_coin_insertions_when_coin_dispenser_full() throws Exception {
        Coin tenCents = Coin.TEN_CENTS;

        int coinShelfCapacity = 10;
        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.spy(VendingMachineFactory.class);
        PowerMockito.when(VendingMachineFactory.getConfig()).thenReturn(configMock);

        VendingMachine vendingMachine = VendingMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(CHOCOLATE_BAR, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity));
        readyState = transformToAndValidateInitialState(vendingMachine);

        readyState.insertCoin(tenCents);

        VendingMachineValidator.validateToReadyState(readyState.vendingMachine);
        Assert.assertTrue(readyState.vendingMachine.provideCurrentState() instanceof ReadyState);

        PowerMockito.verifyStatic(Mockito.times(1));
        VendingMachineFactory.getConfig();
        verifyConfigMock(configMock, 1, 1, 1);
    }

    @Test
    public void should_change_state_after_selecting_valid_shelfNumber_and_display_pending_balance_for_item() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.spy(VendingMachineFactory.class);
        PowerMockito.when(VendingMachineFactory.getConfig()).thenReturn(configMock);
        VendingMachine vendingMachine = VendingMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(CHOCOLATE_BAR, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity));
        readyState = transformToAndValidateInitialState(vendingMachine);

        readyState.selectShelfNumber(0);
        Assert.assertTrue(readyState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.PENDING.label));
        Assert.assertNotNull(readyState.vendingMachine.provideSelectedProduct());
        Assert.assertTrue(readyState.vendingMachine.provideCurrentState() instanceof NoCreditSelectedProductState);

        PowerMockito.verifyStatic(Mockito.times(1));
        VendingMachineFactory.getConfig();
        verifyConfigMock(configMock, 1, 1, 1);
    }

    @Test
    public void should_not_change_state_after_selecting_invalid_shelfNumber() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.spy(VendingMachineFactory.class);
        PowerMockito.when(VendingMachineFactory.getConfig()).thenReturn(configMock);
        VendingMachine vendingMachine = VendingMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(CHOCOLATE_BAR, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity));
        readyState = transformToAndValidateInitialState(vendingMachine);

        readyState.selectShelfNumber(5515);
        transformToAndValidateInitialState(readyState.vendingMachine);

        PowerMockito.verifyStatic(Mockito.times(1));
        VendingMachineFactory.getConfig();
        verifyConfigMock(configMock, 1, 1, 1);
    }

    @Test
    public void should_not_change_state_after_selecting_empty_shelfNumber() throws Exception {
        int productShelfCapacity = 10;
        int coinShelfCapacity = 10;
        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, 10, productShelfCapacity);
        PowerMockito.spy(VendingMachineFactory.class);
        PowerMockito.when(VendingMachineFactory.getConfig()).thenReturn(configMock);
        Collection<ProductImport> products = Arrays.asList(new ProductImport(CHOCOLATE_BAR.getType(), CHOCOLATE_BAR.getPrice(), 1),
                                                            new ProductImport(COLA_199_025.getType(), COLA_199_025.getPrice(), 0));

        Map<Integer, Shelf<Product>> productImportStub = TestUtils.buildShelfStubFromProductImports(products, productShelfCapacity);
        Map<Coin, Shelf<Coin>> coinShelves = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity);
        VendingMachine vendingMachine = VendingMachineFactory.customVendingMachineForTesting(productImportStub, coinShelves);
        readyState = transformToAndValidateInitialState(vendingMachine);
        int emptyShelfId = 1;

        readyState.selectShelfNumber(emptyShelfId);
        transformToAndValidateInitialState(readyState.vendingMachine);

        PowerMockito.verifyStatic(Mockito.times(1));
        VendingMachineFactory.getConfig();
        verifyConfigMock(configMock, 1, 1, 1);
    }

    @Test
    public void should_not_change_state_after_cancel() throws Exception {
        int coinShelfCapacity = 10;
        VendingMachineConfiguration configMock = getConfigMock(coinShelfCapacity, 10, 10);
        PowerMockito.spy(VendingMachineFactory.class);
        PowerMockito.when(VendingMachineFactory.getConfig()).thenReturn(configMock);
        VendingMachine vendingMachine = VendingMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(CHOCOLATE_BAR, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity));
        readyState = transformToAndValidateInitialState(vendingMachine);

        readyState.cancel();
        transformToAndValidateInitialState(readyState.vendingMachine);

        PowerMockito.verifyStatic(Mockito.times(1));
        VendingMachineFactory.getConfig();
        verifyConfigMock(configMock, 1, 1, 1);
    }

    @Test
    public void should_send_machine_to_technicalErrorState_on_insertCoin() throws Exception {
        Coin fiftyCents = Coin.FIFTY_CENTS;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.spy(VendingMachineFactory.class);
        PowerMockito.when(VendingMachineFactory.getConfig()).thenReturn(configMock);

        VendingMachine spied = PowerMockito.spy(VendingMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(10, 5)));
        PowerMockito.doThrow(new RuntimeException("fail to error")).when(spied, "addCoinToCredit", fiftyCents);

        readyState = new ReadyState(spied);

        readyState.insertCoin(fiftyCents);

        Assert.assertTrue(readyState.vendingMachine.provideCurrentState() instanceof TechnicalErrorState);

        PowerMockito.verifyStatic(Mockito.times(1));
        VendingMachineFactory.getConfig();
        Mockito.verify(spied, Mockito.times(1)).addCoinToCredit(fiftyCents);
        verifyConfigMock(configMock, 1, 1, 1);
    }

    @Test
    public void should_send_machine_to_technicalErrorState_on_selectShelf() throws Exception {
        int shelfNumber = 0;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.spy(VendingMachineFactory.class);
        PowerMockito.when(VendingMachineFactory.getConfig()).thenReturn(configMock);

        VendingMachine spied = PowerMockito.spy(VendingMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(10, 5)));
        PowerMockito.doThrow(new RuntimeException("fail to error")).when(spied, "selectProductGivenShelfNumber", shelfNumber);

        readyState = new ReadyState(spied);

        readyState.selectShelfNumber(shelfNumber);

        Assert.assertTrue(readyState.vendingMachine.provideCurrentState() instanceof TechnicalErrorState);

        PowerMockito.verifyStatic(Mockito.times(1));
        VendingMachineFactory.getConfig();
        Mockito.verify(spied, Mockito.times(1)).selectProductGivenShelfNumber(shelfNumber);
        verifyConfigMock(configMock, 1, 1, 1);
    }
}
