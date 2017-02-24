package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.util.TestUtils.TestUtils;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.Arrays;
import java.util.Collection;


/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class InsufficientCreditStateTest implements StateTest {

    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;
    private InsufficientCreditState insufficientCreditState;


    @Override
    public InsufficientCreditState transformToInitialState(VendingMachine vendingMachine) {
        Assert.assertEquals(0, vendingMachine.getCredit()); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
        NoCreditNoProductSelectedState initialState = (NoCreditNoProductSelectedState) vendingMachine.getCurrentState();

        //modify to get desired state
        Coin tenCents = Coin.TEN_CENTS;
        initialState.insertCoin(tenCents);
        Assert.assertTrue(initialState.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);
        initialState.vendingMachine.getCurrentState().selectShelfNumber(1);

        //validate initial state
        Assert.assertEquals(tenCents.denomination, initialState.vendingMachine.getCredit());
        Assert.assertNotNull(initialState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(initialState.vendingMachine.getCurrentState() instanceof InsufficientCreditState);
        return (InsufficientCreditState) initialState.vendingMachine.getCurrentState();
    }

    @Before @Override
    public void setup() {
        COLA_199_025 = new Product(290, "COLA_199_025");
        CHIPS_025 = new Product(130, "CHIPS_025");
        CHOCOLATE_BAR = new Product(160, "CHOCOLATE_BAR");
        Collection<Product> products = Arrays.asList(COLA_199_025, CHIPS_025, CHOCOLATE_BAR);
        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(products, 3), TestUtils.buildCoinDispenserWithGivenItemsPerShelf(20, 5));
        insufficientCreditState = transformToInitialState(vendingMachine);
    }

    @After @Override
    public void tearDown() {
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        insufficientCreditState = null;
    }

    @Test
    public void should_return_all_credit_after_cancel_operation_and_change_state_to_NoCreditNoProductSelected() {
        insufficientCreditState.cancel();

        Assert.assertEquals(0, insufficientCreditState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(0, insufficientCreditState.vendingMachine.getCredit());
        Assert.assertTrue(insufficientCreditState.vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }

    @Test
    public void given_invalid_shelfNumber_should_provide_error_message_and_remain_same_state() {
        insufficientCreditState.selectShelfNumber(123);
        Assert.assertTrue(insufficientCreditState.vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label));
        Assert.assertTrue(insufficientCreditState.vendingMachine.getDisplayCurrentMessage().contains(VendingMachineMessages.PENDING.label));
        Assert.assertTrue(insufficientCreditState.vendingMachine.getCurrentState() instanceof InsufficientCreditState);
    }

    @Test
    public void given_valid_shelfNumber_that_price_is_equally_covered_with_current_credit_should_dispense_item() {
        int shelfNumberEnoughCash = 1;
        int shelfNumberInsufficientCash = 0;

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
        Assert.assertTrue(insufficientCreditState.vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }
}
