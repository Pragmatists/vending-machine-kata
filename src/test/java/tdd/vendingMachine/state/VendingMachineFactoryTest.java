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
import tdd.vendingMachine.domain.CoinDispenserFactory;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.Shelf;
import tdd.vendingMachine.domain.VendingMachineConfiguration;
import tdd.vendingMachine.dto.ProductImport;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachine.class, VendingMachineConfiguration.class, VendingMachineFactory.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class VendingMachineFactoryTest {

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
    public void should_build_sold_out_vending_machine() throws Exception {
        Product product = new Product(100, "product");

        VendingMachineConfiguration configMock = getConfigMock(10, 10, 10);
        PowerMockito.spy(VendingMachineFactory.class);
        PowerMockito.when(VendingMachineFactory.getConfig()).thenReturn(configMock);

        VendingMachine soldOutVendingMachine = VendingMachineFactory.buildSoldOutVendingMachineNoCash(product);
        Assert.assertTrue(soldOutVendingMachine.isSoldOut());

        PowerMockito.verifyStatic(Mockito.times(2));
        VendingMachineFactory.getConfig();
        verifyConfigMock(configMock, 2, 1, 2);
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_coin_shelves_null() {
        VendingMachineFactory.customVendingMachineForTesting(Collections.emptyMap(), null);
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_product_shelves_null() {
        VendingMachineFactory.customVendingMachineForTesting(null, TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(10, 10));
    }

    @Test
    public void should_build_shelves() {
        VendingMachine vendingMachine = VendingMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(TestUtils.buildStubListOfProducts(2), 1, 3),
            TestUtils.buildStubCoinDispenserWithGivenItemsPerShelf(10, 3)
        );
        Assert.assertNotNull(vendingMachine);
    }

    @Test
    public void should_fail_invalid_amount_shelves() {
        int amountProducts = 5;
        int productItemCount = 3;
        int coinItemCount = 5;
        Collection<Product> productList = TestUtils.buildStubListOfProducts(amountProducts);
        VendingMachineFactory.buildVendingMachineGivenProductsAndInitialShelfItemCounts(productList, productItemCount, coinItemCount);
    }

    @Test
    public void should_build_products_from_products_import_collection() throws Exception {
        int productShelfCount = 10;
        int productShelfCapacity = 10;
        VendingMachineConfiguration configMock = getConfigMock(10, productShelfCount, productShelfCapacity);

        PowerMockito.spy(VendingMachineFactory.class);
        PowerMockito.when(VendingMachineFactory.getConfig()).thenReturn(configMock);

        ProductImport productImport1 = new ProductImport("p1", 100, 10);
        ProductImport productImport2 = new ProductImport("p2", 200, 10);
        Collection<ProductImport> productImports = Arrays.asList(productImport1, productImport2);
        Map<Integer, Shelf<Product>> productShelves = VendingMachineFactory.buildProductShelfFromCashImports(productImports);

        Assert.assertNotNull(productImports);
        Assert.assertTrue(productShelves.size() <= productShelfCount );
        Assert.assertEquals(productImport1.getItemCount(), productShelves.get(0).getItemCount());
        Assert.assertEquals(productImport1.getType(), productShelves.get(0).getType().getType());
        Assert.assertEquals(productImport1.getPrice(), productShelves.get(0).getType().getPrice());
        Assert.assertEquals(productImport2.getItemCount(), productShelves.get(1).getItemCount());
        Assert.assertEquals(productImport2.getType(), productShelves.get(1).getType().getType());
        Assert.assertEquals(productImport2.getPrice(), productShelves.get(1).getType().getPrice());

        productShelves.values().forEach(productShelf -> Assert.assertEquals(productShelfCapacity, productShelf.capacity));

        PowerMockito.verifyStatic(Mockito.times(1));
        VendingMachineFactory.getConfig();

        verifyConfigMock(configMock, 0, 0, 1);
    }
}
