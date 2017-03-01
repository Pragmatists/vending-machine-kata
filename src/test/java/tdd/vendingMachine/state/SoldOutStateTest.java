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
import tdd.vendingMachine.domain.VendingMachineConfiguration;
import tdd.vendingMachine.domain.exception.UnableToProvideBalanceException;
import tdd.vendingMachine.util.TestUtils.TestUtils;
import tdd.vendingMachine.view.VendingMachineMessages;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachine.class, SoldOutState.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class SoldOutStateTest implements StateTest {

    private Product COLA_025;
    private SoldOutState soldOutState;
    private VendingMachineFactory vendingMachineFactory;

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
    public SoldOutState transformToAndValidateInitialState(VendingMachine vendingMachine) {
        Assert.assertEquals(0, vendingMachine.getCredit()); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof SoldOutState);
        return (SoldOutState) vendingMachine.getCurrentState();
    }

    @Before @Override
    public void setup() {
        vendingMachineFactory = new VendingMachineFactory();
        COLA_025 = new Product(100, "COLA_025cl");
        soldOutState = transformToAndValidateInitialState(vendingMachineFactory.buildSoldOutVendingMachineNoCash(COLA_025));
    }

    @After @Override
    public void tearDown() {
        soldOutState = null;
        vendingMachineFactory = null;
        COLA_025 = null;
    }

    @Test
    public void should_remain_sold_out_after_inserting_coin_and_return_coin_to_pickup_shelf() {
        soldOutState.insertCoin(Coin.FIFTY_CENTS);

        Assert.assertEquals(0, soldOutState.vendingMachine.getCredit());
        Assert.assertTrue(soldOutState.vendingMachine.getCurrentState() instanceof SoldOutState);
    }

    @Test
    public void should_remain_sold_out_and_display_product_price_after_valid_selecting_valid_shelfNumber() {
        soldOutState.selectShelfNumber(0);

        Assert.assertTrue(soldOutState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.PRICE.label));//TODO: view todo list on googlespreadsheets
        Assert.assertEquals(0, soldOutState.vendingMachine.getCredit());
        Assert.assertTrue(soldOutState.vendingMachine.getCurrentState() instanceof SoldOutState);
    }

    @Test
    public void should_remain_sold_out_and_display_warning_after_selecting_invalid_shelfNumber() {
        soldOutState.selectShelfNumber(585);

        Assert.assertTrue(soldOutState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label));//TODO: view todo list on googlespreadsheets
        Assert.assertEquals(0, soldOutState.vendingMachine.getCredit());
        Assert.assertTrue(soldOutState.vendingMachine.getCurrentState() instanceof SoldOutState);
    }

    @Test
    public void should_perform_no_actions_on_cancel_operation() {
        soldOutState.cancel();
        Assert.assertEquals(0, soldOutState.vendingMachine.getCredit());
        Assert.assertNull(soldOutState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(soldOutState.vendingMachine.getCurrentState() instanceof SoldOutState);
    }

    @Test
    public void should_send_machine_to_technicalErrorState_on_selectShelf() throws Exception {
        int shelfNumber = 0;
        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);

        VendingMachine spied = PowerMockito.spy(new VendingMachineFactory().customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_025, 1),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(10, 5)));
        PowerMockito.doThrow(new RuntimeException("fail to error")).when(spied, "displayProductPrice", shelfNumber);

        soldOutState = new SoldOutState(spied);

        soldOutState.selectShelfNumber(shelfNumber);

        Assert.assertTrue(soldOutState.vendingMachine.getCurrentState() instanceof TechnicalErrorState);

        PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(1)).withNoArguments();
        Mockito.verify(spied, Mockito.times(1)).displayProductPrice(shelfNumber);
        verifyConfigMock(configMock, 1, 1, 1);
    }
}
