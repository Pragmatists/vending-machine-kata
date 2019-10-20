package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.display.Display;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tdd.vendingMachine.MachineConfiguration.aMachine;
import static tdd.vendingMachine.product.ProductType.*;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineIntegrationTest {

    VendingMachine vendingMachine;

    String printedMessage = "";

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(mock(OutputStream.class)) {
            @Override
            public void println(String value) {
                printedMessage = printedMessage.concat(value + "\n");
            }
        });
        vendingMachine = new VendingMachine();
    }

    @Test
    public void shouldDisplayChangeWithPrecision() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        aMachine(vendingMachine)
            .withProduct(new Product(CHIPS))
            .onShelf(shelfNumber)
            .withSelectedProduct(shelfNumber)
            .withCurrentRequestCoins(1.0, 0.5, 0.1, 0.1, 0.1, 0.1, 0.1)
            .configure();
        //when
        vendingMachine.insertCoinForCurrentRequest(new Coin(0.5));
        //then
        assertThat(printedMessage).doesNotContain("0.8999999999999999");
        assertThat(printedMessage).contains("1.5");
        assertThat(printedMessage).contains("1.0");
        assertThat(printedMessage).contains("0.9");
        assertThat(printedMessage).contains("0.8");
        assertThat(printedMessage).contains("0.7");
        assertThat(printedMessage).contains("0.6");
        assertThat(printedMessage).contains("0.5");
        assertThat(printedMessage).contains("Machine drop product of type: CHIPS");
    }


    //    1. Vending machine contains products.
//    3. Products are on shelves.
//    2. Products can be of different types (i.e. Cola drink 0.25l, chocolate bar, mineral water 0.33l and so on).
    @Test
    public void shouldVendingMachineContainsProducts() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        insertProductOnShelf(shelfNumber, new Product(CHIPS));
        //when
        vendingMachine.displayMachineShelfsInformation();
        //then
        assertThat(printedMessage).contains("Shelf 5 contains product type: CHIPS. Products on shelf: 1");
    }

    //    4. One shelve can contain only one type of product (but multiple products).
    @Test(expected = CannotChangeShelfProductsTypeException.class)
    public void shouldThrowErrorWhenWeWantToPutDifferentProductOnOneShelf() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        Product chips = new Product(CHIPS);
        Product cola = new Product(COLA);
        insertProductOnShelf(shelfNumber, chips);
        //when
        insertProductOnShelf(shelfNumber, cola);
        //then
    }

    //    5. Each product type has its own price.
    @Test()
    public void shouldProductTypesHasPrices() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        //when
        //then
        assertThat(CHIPS.getPrice()).isEqualTo(2.5);
        assertThat(CHOCOLATE_BAR.getPrice()).isEqualTo(3.0);
        assertThat(WATER.getPrice()).isEqualTo(1.2);
        assertThat(NUTS.getPrice()).isEqualTo(3.5);
        assertThat(COLA.getPrice()).isEqualTo(2.0);
    }

    //    6. Machine has a display.
    @Test()
    public void eachProductTypeShouldHasPrice() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        //when
        vendingMachine.displayMachineShelfsInformation();
        //then
        assertThat(printedMessage).contains(Display.MACHINE_DISPLAY_OUTPUT_PREFIX);
    }

    //    7. If we select shelve number, display should show product price.
    @Test()
    public void machineShouldDisplayProductPriceWhenProductWasSelected() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        aMachine(vendingMachine)
            .withProduct(new Product(CHIPS))
            .onShelf(shelfNumber)
            .withSelectedProduct(shelfNumber)
            .configure();
        //when
        //then
        assertThat(printedMessage).contains("CHIPS. Please insert " + CHIPS.getPrice());
    }

    //    8. You can buy products by putting money into machine. Machine accepts denominations 5, 2, 1, 0.5, 0.2, 0.1.
    @Test()
    public void shouldDisplayInvalidCoinMessageWhenCoinIsInvalid() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int invalidCoinValue = 10;
        int shelfNumber = 5;
        //when
        aMachine(vendingMachine)
            .withProduct(new Product(CHIPS))
            .onShelf(shelfNumber)
            .withSelectedProduct(shelfNumber)
            .withCurrentRequestCoins(invalidCoinValue)
            .configure();
        //then
        assertThat(printedMessage).contains(Display.INVALID_COIN_MESSAGE);
        assertThat(printedMessage).contains("Returned coin: 10.0");
    }

    //    8. You can buy products by putting money into machine. Machine accepts denominations 5, 2, 1, 0.5, 0.2, 0.1.
    @Test()
    public void shouldNotDisplayInvalidCoinFormatForSupportedCoinsFormat() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        double[] validCoinsValues = {5.0, 2.0, 1.0, 0.5, 0.2, 0.1};
        //when
        aMachine(vendingMachine)
            .withProduct(aProductWithPrice(100000.0))
            .onShelf(shelfNumber)
            .withSelectedProduct(shelfNumber)
            .withCurrentRequestCoins(validCoinsValues)
            .configure();
        //then
        assertThat(printedMessage).doesNotContain(Display.INVALID_COIN_MESSAGE);
    }

    //    9. After inserting a coin, display shows amount that must be added to cover product price.
    @Test()
    public void shouldDisplayRemainingCoinsValueWhenInsertingNewCoin() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        aMachine(vendingMachine)
            .withProduct(aProductWithPrice(2.5))
            .onShelf(shelfNumber)
            .withSelectedProduct(shelfNumber)
            .configure();
        //when
        vendingMachine.insertCoinForCurrentRequest(new Coin(2.0));
        //then
        assertThat(printedMessage).contains("Still need : 0.5 more.");
    }

    //    10.After selecting a shelve and inserting enough money we will get the product and the change (but machine has to have money to be able to return the change).
    @Test()
    public void shouldDropProductWhenMachineHasMoneyAndCanWithdrawChange() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        aMachine(vendingMachine)
            .withProduct(aProductWithPrice(0.5))
            .withCashBoxCoins(0.5)
            .onShelf(shelfNumber)
            .withSelectedProduct(shelfNumber)
            .configure();
        //when
        vendingMachine.insertCoinForCurrentRequest(new Coin(1.0));
        //then
        assertThat(printedMessage).contains("Machine drop product");
    }

    //11. After selecting a shelve and inserting insufficient money to buy a product, user has to press "Cancel" to get their money back.
    @Test()
    public void shouldDropInsertedCoinsWhenRequestWasCanceled() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        aMachine(vendingMachine)
            .withProduct(aProductWithPrice(100))
            .withCurrentRequestCoins(0.5, 1.0)
            .onShelf(shelfNumber)
            .withSelectedProduct(shelfNumber)
            .configure();
        //when
        vendingMachine.cancelRequest();
        //then
        assertThat(printedMessage).contains(Display.REQUEST_WAS_CANCELED_MESSAGE);
        assertThat(printedMessage).contains("Returned coin: 0.5");
        assertThat(printedMessage).contains("Returned coin: 1.0");
    }

    //    12. If machine does not have enough money to give the change it must show a warning message and return the money user has put, and it should not give the product.
    @Test()
    public void shouldReturnInsertedCoinsWhenMachineCantReturnChange() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        aMachine(vendingMachine)
            .withProduct(aProductWithPrice(0.8))
            .withCashBoxCoins(0.5, 0.1)
            .onShelf(shelfNumber)
            .withSelectedProduct(shelfNumber)
            .configure();
        //when
        vendingMachine.insertCoinForCurrentRequest(new Coin(1.0));
        //then
        assertThat(printedMessage).contains(Display.CANT_RETURN_CHANGE_MESSAGE);
        assertThat(printedMessage).contains(Display.REQUEST_WAS_CANCELED_MESSAGE);
        assertThat(printedMessage).contains("Returned coin: 1.0");
    }

    //13. Machine can return change using only money that was put into it (or by someone at start or by people who bought goods). Machine cannot create it's own money!
    @Test()
    public void shouldReturnChangeUsingCashBoxPocketAndCurrentRequestPocket() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int shelfNumber = 5;
        aMachine(vendingMachine)
            .withProduct(aProductWithPrice(0.8))
            .withCashBoxCoins(0.2)
            .withCurrentRequestCoins(0.1)
            .onShelf(shelfNumber)
            .withSelectedProduct(shelfNumber)
            .configure();
        //when
        vendingMachine.insertCoinForCurrentRequest(new Coin(1.0));
        //then
        assertThat(printedMessage).contains("Machine drop product");
        assertThat(printedMessage).contains("Returned coin: 0.1");
        assertThat(printedMessage).contains("Returned coin: 0.2");
    }

    private void insertProductOnShelf(int shelfNumber, Product product) throws CannotChangeShelfProductsTypeException {
        aMachine(vendingMachine)
            .withProduct(product)
            .onShelf(shelfNumber)
            .configure();
    }

    private Product aProductWithPrice(double price) {
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(price);
        when(product.getType()).thenReturn(CHIPS);
        return product;
    }

}
