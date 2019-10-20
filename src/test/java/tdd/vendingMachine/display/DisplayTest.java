package tdd.vendingMachine.display;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.ProductType;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;
import tdd.vendingMachine.shelf.Shelf;

import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DisplayTest {
    @InjectMocks
    @Spy
    Display display = new Display();
    @Mock
    PrintStream displayOutput;

    @Test
    public void shouldPrintInvalidCoinMessage() throws Exception {
        //given
        //when
        display.showInvalidCoinFormatMessage();
        //then
        verify(display).print(Display.INVALID_COIN_MESSAGE);
    }

    @Test
    public void shouldPrintRemainingValueMessage() throws Exception {
        //given
        double remainingValue = 2.0;
        //when
        display.showRemainingValueForSelectedProductMessage(remainingValue);
        //then
        verify(display).print("Still need : 2.0 more. Hurry up!");
    }

    @Test
    public void shouldPrintInvalidShelfNumberMessage() throws Exception {
        //given
        //when
        display.showInvalidShelfNumberMessage();
        //then
        verify(display).print(Display.INCORRECT_SHELF_NUMBER_MESSAGE);
    }

    @Test
    public void shouldPrintAddProductMessage() throws Exception {
        //given
        int shelfNumber = 5;
        double price = ProductType.CHIPS.getPrice();
        Product product = new Product(ProductType.CHIPS);
        //when
        display.showInsertProductInformationMessage(shelfNumber, product);
        //then
        verify(display).print("Inserted product at shelf: 5. Product type is: CHIPS with own price: " + price);
    }

    @Test
    public void shouldPrintMessageWithMachinePrefix() throws Exception {
        //given
        String sampleMessage = "Some message";
        //when
        display.print(sampleMessage);
        //then
        verify(displayOutput).println(Display.MACHINE_DISPLAY_OUTPUT_PREFIX + sampleMessage);
    }

    @Test
    public void shouldPrintIncorrectShelfSelectedMessage() throws Exception {
        //given
        //when
        display.showIncorrectProductSelectMessage();
        //then
        verify(display).print(Display.INCORRECT_SHELF_SELECT_MESSAGE);
    }

    @Test
    public void shouldPrintSelectedProductMessage() throws Exception {
        //given
        double price = ProductType.CHIPS.getPrice();
        //when
        display.showProductSelectedMessage(new Product(ProductType.CHIPS));
        //then
        verify(display).print("Congratulations you choose product: CHIPS. Please insert " + price + " if you want to get this product.");
    }

    @Test
    public void shouldPrintReturnCoinMessage() throws Exception {
        //given
        double coinValue = 5.0;
        Coin coin = new Coin(coinValue);
        //when
        display.showReturnCoinMessage(coin);
        //then
        verify(display).print("Returned coin: " + coinValue);
    }

    @Test
    public void shouldPrintInvalidActionForMachineStateMessage() throws Exception {
        //given
        //when
        display.showInvalidActionForMachineStateMessage();
        //then
        verify(display).print(Display.ACTION_IS_NOT_ALLOWED_ON_THIS_MACHINE_STATE_MESSAGE);
    }

    @Test
    public void shouldPrintCoinAddedToCashBoxMessage() throws Exception {
        //given
        //when
        display.showCoinAddedToCashBoxMessage(new Coin(5.0));
        //then
        verify(display).print("You added coin to machine cash box with value: 5.0");
    }

    @Test
    public void shouldPrintFirstSelectProductMessage() throws Exception {
        //given
        //when
        display.showFirstSelectProductMessage();
        //then
        verify(display).print(Display.PLEASE_FIRST_SELECT_PRODUCT_MESSAGE);
    }

    @Test
    public void shouldPrintRequestWasCanceledMessage() throws Exception {
        //given
        //when
        display.showRequestCanceledMessage();
        //then
        verify(display).print(Display.REQUEST_WAS_CANCELED_MESSAGE);
    }

    @Test
    public void shouldPrintCantReturnChangeMessage() throws Exception {
        //given
        //when
        display.showCantReturnChangeMessage();
        //then
        verify(display).print(Display.CANT_RETURN_CHANGE_MESSAGE);
    }

    @Test
    public void shouldDisplayShelfDetailsInfoWhenShelfIsNotEmpty() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int num = 5;
        Shelf shelf = new Shelf(num);
        shelf.setProductsType(ProductType.CHIPS);
        shelf.add(new Product(ProductType.CHIPS));
        //when
        display.showShelfInformation(shelf);
        //then
        verify(display).print("Shelf 5 contains product type: CHIPS. Products on shelf: 1");
    }

    @Test
    public void shouldDisplayShelfIsEmptyWhenShelfIsEmpty() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        int num = 5;
        Shelf shelf = new Shelf(num);
        //when
        display.showShelfInformation(shelf);
        //then
        assertThat(shelf.isEmpty()).isTrue();
        verify(display).print("Shelf 5 is empty!");
    }

    @Test
    public void shouldDropProductMessage() throws Exception {
        //given
        Product product = new Product(ProductType.CHIPS);
        //when
        display.showDropProductMessage(product);
        //then
        verify(display).print("Machine drop product of type: CHIPS off price: " + ProductType.CHIPS.getPrice() + ".");
    }


}
