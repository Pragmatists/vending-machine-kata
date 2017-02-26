package tdd.vendingMachine.state;

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
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.VendingMachineFactory;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Shelf;
import tdd.vendingMachine.domain.VendingMachineConfiguration;
import tdd.vendingMachine.domain.exception.UnableToProvideBalanceException;
import tdd.vendingMachine.util.TestUtils.TestUtils;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.Map;

/**
 * @author Agustin on 2/26/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TechnicalErrorState.class, VendingMachineConfiguration.class, VendingMachineFactory.class, VendingMachine.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class TechnicalErrorStateTest implements StateTest {

    private TechnicalErrorState technicalErrorState;
    private VendingMachineConfiguration configMock;

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
    public TechnicalErrorState transformToAndValidateInitialState(VendingMachine vendingMachine) {
        vendingMachine.setCurrentState(vendingMachine.getTechnicalErrorState());
        return (TechnicalErrorState) vendingMachine.getCurrentState();
    }

    @Before @Override
    public void setup() {
        int shelfCapacityCoins = 10;
        int shelfCapacityProducts = 10;
        int productShelfCount = 10;

        configMock = getConfigMock(shelfCapacityCoins, productShelfCount, shelfCapacityProducts);
        try {
            PowerMockito.whenNew(VendingMachineConfiguration.class).withNoArguments().thenReturn(configMock);
        }catch (Exception e) {
            Assert.fail();
        }


        Map<Integer, Shelf<Product>> productShelf = TestUtils.buildShelvesWithItems(TestUtils.buildStubListOfProducts(3), 3, shelfCapacityProducts);
        Map<Coin, Shelf<Coin>> coinShelf = TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(shelfCapacityCoins, 5);
        VendingMachine vendingMachine = new VendingMachineFactory().customVendingMachineForTesting(productShelf, coinShelf);
        technicalErrorState = transformToAndValidateInitialState(vendingMachine);
    }

    @After @Override
    public void tearDown() {
        technicalErrorState = null;
        try {
            PowerMockito.verifyNew(VendingMachineConfiguration.class, Mockito.times(2)).withNoArguments();
        } catch (Exception e) {
            Assert.fail();
        }
        verifyConfigMock(configMock, 3, 2, 2);
        configMock = null;
    }

    @Test
    public void on_cancel_do_nothing() {
        Assert.assertTrue(StringUtils.isEmpty(technicalErrorState.vendingMachine.getDisplayCurrentMessage()));
        technicalErrorState.cancel();
        Assert.assertTrue(technicalErrorState.vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.TECHNICAL_ERROR.label));
    }

    @Test
    public void on_insertCoin_do_nothing() {
        Assert.assertTrue(StringUtils.isEmpty(technicalErrorState.vendingMachine.getDisplayCurrentMessage()));
        technicalErrorState.insertCoin(Coin.FIFTY_CENTS);
        Assert.assertTrue(technicalErrorState.vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.TECHNICAL_ERROR.label));
    }

    @Test
    public void on_selectShelf_do_nothing() {
        Assert.assertTrue(StringUtils.isEmpty(technicalErrorState.vendingMachine.getDisplayCurrentMessage()));
        technicalErrorState.selectShelfNumber(0);
        Assert.assertTrue(technicalErrorState.vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.TECHNICAL_ERROR.label));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_fail_on_attempt_to_sell() throws UnableToProvideBalanceException {
        technicalErrorState.attemptSell();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_fail_on_attempt_to_rollback() throws UnableToProvideBalanceException {
        technicalErrorState.returnCreditStackToCashPickupBucketAndSetToReadyState("", 0);
    }
}
