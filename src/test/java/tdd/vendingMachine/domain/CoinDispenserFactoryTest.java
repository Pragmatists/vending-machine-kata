package tdd.vendingMachine.domain;

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
import tdd.vendingMachine.dto.CashImport;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CoinDispenserFactory.class, VendingMachineConfiguration.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class CoinDispenserFactoryTest {

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
     * @param mock mock configuration to verify
     * @param coinShelfInvocations expected invocations for coinShelfCapacity
     * @param productShelfCountInvocations expected invocations for productShelfCount
     * @param productShelfCapacityInvocations expected invocations for productShelfCapacity
     */
    private void verifyConfigMock(VendingMachineConfiguration mock, int coinShelfInvocations, int productShelfCountInvocations, int productShelfCapacityInvocations) {
        Mockito.verify(mock, Mockito.times(coinShelfInvocations)).getCoinShelfCapacity();
        Mockito.verify(mock, Mockito.times(productShelfCountInvocations)).getProductShelfCount();
        Mockito.verify(mock, Mockito.times(productShelfCapacityInvocations)).getProductShelfCapacity();
    }

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void should_build_empty_cashDispenser_with_shelf_capacity_1() {
        final int expectedCapacity = 1;
        int expectedSize = Coin.values().length;
        VendingMachineConfiguration configMock = getConfigMock(1, 0, 0);

        PowerMockito.spy(CoinDispenserFactory.class);
        PowerMockito.when(CoinDispenserFactory.getConfig()).thenReturn(configMock);

        Map<Coin, Shelf<Coin>> cashDispenser = CoinDispenserFactory.buildShelfWithGivenCoinItemCount(0);
        Assert.assertEquals(expectedSize, cashDispenser.size());
        cashDispenser.values().forEach(coinShelf -> {
            Assert.assertEquals(expectedCapacity, coinShelf.capacity);
            Assert.assertEquals(0, coinShelf.getItemCount());
        });

        PowerMockito.verifyStatic(Mockito.times(1));
        CoinDispenserFactory.getConfig();
        verifyConfigMock(configMock, 1, 0, 0);
    }

    @Test
    public void should_build_full_cashDispenser_with_shelf_capacity_5() throws Exception {
        final int expectedCapacity = 5;
        final int expectedSize = Coin.values().length;
        final int expectedAmount = 5;
        Collection<CashImport> stubCashImportsFull = TestUtils.getStubCashImportsFull(expectedAmount);
        VendingMachineConfiguration configMock = getConfigMock(expectedCapacity, 0, 0);

        PowerMockito.spy(CoinDispenserFactory.class);
        PowerMockito.when(CoinDispenserFactory.getConfig()).thenReturn(configMock);

        Map<Coin, Shelf<Coin>> cashDispenser = CoinDispenserFactory.buildShelf(stubCashImportsFull);
        Assert.assertEquals(expectedSize, cashDispenser.size());
        cashDispenser.values().forEach(coinShelf -> {
            Assert.assertEquals(expectedCapacity, coinShelf.capacity);
            Assert.assertEquals(expectedAmount, coinShelf.getItemCount());
            Assert.assertTrue(coinShelf.countFreeSlots() == 0);
        });

        PowerMockito.verifyStatic(Mockito.times(1));
        CoinDispenserFactory.getConfig();
        verifyConfigMock(configMock, 1, 0, 0);
    }

    @Test
    public void should_discard_items_since_shelf_got_full()  throws Exception {
        final int expectedSize = Coin.values().length;
        final int expectedCapacity = 4;
        final int givenAmount = 7;
        final int expectedDiscardedItems = givenAmount - expectedCapacity;
        Coin fiftyCents = Coin.FIFTY_CENTS;
        CashImport stubCashImport = new CashImport(fiftyCents.label, givenAmount);

        VendingMachineConfiguration configMock = getConfigMock(4, 0, 0);

        PowerMockito.spy(CoinDispenserFactory.class);
        PowerMockito.when(CoinDispenserFactory.getConfig()).thenReturn(configMock);

        Map<Coin, Shelf<Coin>> cashDispenser = CoinDispenserFactory.buildShelf(stubCashImport);
        Assert.assertEquals(expectedSize, cashDispenser.size());
        Assert.assertEquals(expectedCapacity, cashDispenser.get(fiftyCents).getItemCount());
        Assert.assertEquals(expectedDiscardedItems, stubCashImport.getAmount() - cashDispenser.get(fiftyCents).getItemCount());
        Assert.assertEquals(0, cashDispenser.get(fiftyCents).countFreeSlots());

        PowerMockito.verifyStatic(Mockito.times(1));
        CoinDispenserFactory.getConfig();
        verifyConfigMock(configMock, 1, 0, 0);
    }

    @Test
    public void should_build_non_full_shelf_since_import_amount_less_than_capacity()  throws Exception {
        final int expectedSize = Coin.values().length;
        final int expectedCapacity = 4;
        final int givenAmount = 3;
        final int expectedDiscardedItems = 0;
        Coin fiftyCents = Coin.FIFTY_CENTS;
        CashImport stubCashImport = new CashImport(fiftyCents.label, givenAmount);
        VendingMachineConfiguration configMock = getConfigMock(expectedCapacity, 0, 0);

        PowerMockito.spy(CoinDispenserFactory.class);
        PowerMockito.when(CoinDispenserFactory.getConfig()).thenReturn(configMock);

        Map<Coin, Shelf<Coin>> cashDispenser = CoinDispenserFactory.buildShelf(stubCashImport);

        Assert.assertEquals(expectedSize, cashDispenser.size());
        Assert.assertEquals(stubCashImport.getAmount(), cashDispenser.get(fiftyCents).getItemCount());
        Assert.assertEquals(expectedDiscardedItems, stubCashImport.getAmount() - cashDispenser.get(fiftyCents).getItemCount());
        Assert.assertEquals(expectedCapacity - stubCashImport.getAmount(), cashDispenser.get(fiftyCents).countFreeSlots());

        PowerMockito.verifyStatic(Mockito.times(1));
        CoinDispenserFactory.getConfig();
        verifyConfigMock(configMock, 1, 0, 0);
    }

    @Test
    public void should_build_coin_dispenser_with_amount_of_coins_given()  throws Exception {
        int expectedCapacity = 8;
        int initialCount = 7;
        VendingMachineConfiguration configMock = getConfigMock(expectedCapacity, 0, 0);
        PowerMockito.spy(CoinDispenserFactory.class);
        PowerMockito.when(CoinDispenserFactory.getConfig()).thenReturn(configMock);

        Map<Coin, Shelf<Coin>> coinShelfMap = CoinDispenserFactory.buildShelfWithGivenCoinItemCount(initialCount);

        Assert.assertEquals(Coin.values().length, coinShelfMap.size());
        coinShelfMap.values().forEach(coinShelf -> {
            Assert.assertEquals(expectedCapacity, coinShelf.capacity);
            Assert.assertEquals(initialCount, coinShelf.getItemCount());
        });

        PowerMockito.verifyStatic(Mockito.times(1));
        CoinDispenserFactory.getConfig();
        verifyConfigMock(configMock, 1, 0, 0);
    }

    @Test
    public void should_discard_excess_of_cash_imports_coin_shelf_discarded_must_be_full()  throws Exception {
        int expectedCapacity = 8;
        int firstImportAmount = 3;
        int secondImportAmount = 6;
        int expectedDiscardedItems = 1;
        VendingMachineConfiguration configMock = getConfigMock(expectedCapacity, 0, 0);
        PowerMockito.spy(CoinDispenserFactory.class);
        PowerMockito.when(CoinDispenserFactory.getConfig()).thenReturn(configMock);
        Coin fiftyCents = Coin.FIFTY_CENTS;

        List<CashImport> cashImportsCollectionStub = Arrays.asList(new CashImport(fiftyCents.label, firstImportAmount),
            new CashImport(fiftyCents.label, secondImportAmount));
        Map<Coin, Shelf<Coin>> coinShelf = CoinDispenserFactory.buildShelf(cashImportsCollectionStub);
        coinShelf.values().forEach(shelf -> {
            Assert.assertEquals(expectedCapacity, shelf.capacity);
            if (shelf.getType() == fiftyCents) { //the only loaded coin shelf
                Assert.assertEquals(expectedCapacity, shelf.getItemCount());
                Assert.assertFalse(shelf.isEmpty());
                Assert.assertEquals(expectedDiscardedItems, (firstImportAmount + secondImportAmount) - shelf.capacity);
            } else {//other shelves must empty
                Assert.assertEquals(0, shelf.getItemCount());
                Assert.assertTrue(shelf.isEmpty());
            }
        });

        PowerMockito.verifyStatic(Mockito.times(1));
        CoinDispenserFactory.getConfig();
        verifyConfigMock(configMock, 1, 0, 0);
    }
}
