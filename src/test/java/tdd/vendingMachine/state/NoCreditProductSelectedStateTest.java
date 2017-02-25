package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.VendingMachineFactory;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.Arrays;
import java.util.Collection;


/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class NoCreditProductSelectedStateTest implements StateTest {

    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;
    private NoCreditSelectedProductState noCreditSelectedProductState;
    private VendingMachineFactory vendinMachineFactory;

    @Override
    public NoCreditSelectedProductState transformToInitialState(VendingMachine vendingMachine) {
        Assert.assertEquals(0, vendingMachine.getCredit()); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof ReadyState);
        ReadyState initialState = (ReadyState) vendingMachine.getCurrentState();

        //transform to get desired state
        initialState.selectShelfNumber(0);

        //validate initial state
        Assert.assertNotNull(initialState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(initialState.vendingMachine.getCurrentState() instanceof NoCreditSelectedProductState);
        return (NoCreditSelectedProductState) initialState.vendingMachine.getCurrentState();
    }

    @Before @Override
    public void setup() {
        COLA_199_025 = new Product(199, "COLA_199_025");
        CHIPS_025 = new Product(129, "CHIPS_025");
        CHOCOLATE_BAR = new Product(149, "CHOCOLATE_BAR");
        vendinMachineFactory = new VendingMachineFactory();
        Collection<Product> productList = Arrays.asList(COLA_199_025, CHIPS_025, CHOCOLATE_BAR);
        VendingMachine vendingMachine = vendinMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(productList, 3),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(vendinMachineFactory.getVendingMachineConfiguration(), 5));
        noCreditSelectedProductState = transformToInitialState(vendingMachine);
    }

    @After @Override
    public void tearDown() {
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        noCreditSelectedProductState = null;
        vendinMachineFactory = null;
    }
}
