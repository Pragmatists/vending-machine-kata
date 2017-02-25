package tdd.vendingMachine;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.VendingMachineConfiguration;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Agustin on 2/25/2017.
 * @since 1.0
 */
public class VendingMachineFactoryTest {


    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void should_build_sold_out_vending_machine() {
        Product product = new Product(100, "product");
        VendingMachine soldOutVendingMachine = new VendingMachineFactory().buildSoldOutVendingMachineNoCash(product);
        Assert.assertEquals(0, soldOutVendingMachine.countCashInDispenser());
        Assert.assertEquals(0, soldOutVendingMachine.countTotalAmountProducts());
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_coin_shelves_null() {
        new VendingMachineFactory().customVendingMachineForTesting(Collections.emptyMap(), null);
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_product_shelves_null() {
        VendingMachineFactory vendingMachineFactory = new VendingMachineFactory();
        VendingMachineConfiguration vendingMachineConfiguration = vendingMachineFactory.getVendingMachineConfiguration();
        vendingMachineFactory.customVendingMachineForTesting(null, TestUtils.buildCoinDispenserWithGivenItemsPerShelf(vendingMachineConfiguration, 10));
    }

    @Test
    public void should_fail_invalid_amount_shelves() {
        int amountProducts = 5;
        int productItemCount = 3;
        int coinItemCount = 5;
        Collection<Product> productList = TestUtils.buildStubListOfProducts(amountProducts);
        new VendingMachineFactory().buildVendingMachineGivenProductsAndInitialShelfItemCounts(productList, productItemCount, coinItemCount);
    }
}
