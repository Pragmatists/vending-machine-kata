package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import tdd.vendingMachine.cash.coin.Coin;
import tdd.vendingMachine.cash.register.ICashBox;
import tdd.vendingMachine.display.IDisplay;
import tdd.vendingMachine.product.Product;
import tdd.vendingMachine.product.ProductType;
import tdd.vendingMachine.request.Request;
import tdd.vendingMachine.shelf.CannotChangeShelfProductsTypeException;
import tdd.vendingMachine.shelf.IShelf;
import tdd.vendingMachine.shelf.Shelf;
import tdd.vendingMachine.shelf.Shelfs;

import java.util.Arrays;
import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineTest {
    @Spy
    @InjectMocks
    VendingMachine vendingMachine;
    @Mock
    ICashBox cashBox;
    @Mock
    IDisplay display;
    @Mock
    Shelfs shelfs;
    @Mock
    IShelf mockedShelf;
    private Product sampleProduct = new Product(ProductType.CHIPS);

    @Before
    public void setUp() throws Exception {
        vendingMachine.currentRequest = new Request(5, sampleProduct);
        when(mockedShelf.pop()).thenReturn(sampleProduct);
        when(mockedShelf.getNumber()).thenReturn(5);
    }

    @Test
    public void shouldCreateRequestIfChosenShelfIsNotEmpty() throws Exception {
        //given
        int validShelfNumber = 5;
        when(shelfs.get(validShelfNumber)).thenReturn(mockedShelf);
        when(mockedShelf.isEmpty()).thenReturn(false);
        when(mockedShelf.getNumber()).thenReturn(validShelfNumber);
        when(mockedShelf.pop()).thenReturn(sampleProduct);
        vendingMachine.state = VendingMachineState.WAITING_FOR_SELECT_PRODUCT;
        //when
        vendingMachine.selectProduct(validShelfNumber);
        //then
        assertThat(vendingMachine.currentRequest.getShelfNumber()).isEqualTo(validShelfNumber);
        assertThat(vendingMachine.currentRequest.getProduct()).isSameAs(sampleProduct);
    }

    @Test
    public void shouldDisplayInformationWhenShelfDoesNotExist() throws Exception {
        //given
        int invalidShelf = 5;
        when(shelfs.get(invalidShelf)).thenReturn(null);
        when(mockedShelf.isEmpty()).thenReturn(false);
        vendingMachine.state = VendingMachineState.WAITING_FOR_SELECT_PRODUCT;
        //when
        vendingMachine.selectProduct(invalidShelf);
        //then
        verify(display).showIncorrectProductSelectMessage();
    }

    @Test
    public void shouldDisplayInformationWhenShelfIsEmpty() throws Exception {
        //given
        int invalidShelf = 5;
        when(shelfs.get(invalidShelf)).thenReturn(mockedShelf);
        when(mockedShelf.isEmpty()).thenReturn(true);
        //when
        vendingMachine.selectProduct(invalidShelf);
        //then
        verify(display).showIncorrectProductSelectMessage();
    }

    @Test
    public void shouldDisplayIncorrectProductSelectMessageIfShelfChoiceIsCorrect() throws Exception {
        //given
        int validShelfNumber = 5;
        when(shelfs.get(validShelfNumber)).thenReturn(mockedShelf);
        Product product = new Product(ProductType.CHIPS);
        when(mockedShelf.pop()).thenReturn(product);
        when(mockedShelf.isEmpty()).thenReturn(true);
        vendingMachine.state = VendingMachineState.WAITING_FOR_SELECT_PRODUCT;
        //when
        vendingMachine.selectProduct(validShelfNumber);
        //then
        verify(display).showIncorrectProductSelectMessage();
    }

    @Test
    public void shouldChangeStateToProductSelectedIfShelfNumberIsCorrect() throws Exception {
        //given
        int validShelfNumber = 5;
        when(shelfs.get(validShelfNumber)).thenReturn(mockedShelf);
        when(mockedShelf.isEmpty()).thenReturn(false);
        //when
        vendingMachine.selectProduct(validShelfNumber);
        //then
        assertThat(vendingMachine.state).isEqualTo(VendingMachineState.PRODUCT_SELECTED);
    }

    @Test
    public void shouldNotChangeStateToProductSelectedIfShelfNumberIsNotExist() throws Exception {
        //given
        int incorrectShelfNumber = 251;
        assertThat(vendingMachine.state).isEqualTo(VendingMachineState.WAITING_FOR_SELECT_PRODUCT);
        vendingMachine.state = VendingMachineState.WAITING_FOR_SELECT_PRODUCT;
        //when
        vendingMachine.selectProduct(incorrectShelfNumber);
        //then
        assertThat(vendingMachine.state).isEqualTo(VendingMachineState.WAITING_FOR_SELECT_PRODUCT);
    }

    @Test
    public void shouldInsertCoin() throws Exception {
        //given
        Coin coin = new Coin(5.0);
        when(cashBox.isValidCoin(coin)).thenReturn(true);
        doNothing().when(vendingMachine).tryToFinalizeRequest();
        vendingMachine.state = VendingMachineState.PRODUCT_SELECTED;
        //when
        vendingMachine.insertCoinForCurrentRequest(coin);
        //then
        verify(cashBox).addToCurrentRequestPocket(coin);
    }

    @Test
    public void shouldNotInsertCoinIfTheValueIsNotProper() throws Exception {
        //given
        Coin coin = new Coin(0.05);
        when(cashBox.isValidCoin(coin)).thenReturn(false);
        //when
        vendingMachine.insertCoinForCurrentRequest(coin);
        //then
        verify(cashBox, never()).addToCurrentRequestPocket(coin);
    }

    @Test
    public void shouldDisplayMessageAboutTheCoinIsInvalid() throws Exception {
        //given
        Coin coin = new Coin(0.05);
        when(cashBox.isValidCoin(coin)).thenReturn(false);
        vendingMachine.state = VendingMachineState.PRODUCT_SELECTED;
        //when
        vendingMachine.insertCoinForCurrentRequest(coin);
        //then
        verify(display).showInvalidCoinFormatMessage();
    }

    @Test
    public void shouldDisplayMessageAboutReamingValueToFInalizeRequest() throws Exception {
        //given
        when(vendingMachine.getCurrentRequestPrice()).thenReturn(5.0);
        when(cashBox.getInsertedCoinsValueForCurrentRequest()).thenReturn(4.0);
        //when
        vendingMachine.tryToFinalizeRequest();
        //then
        verify(display).showRemainingValueForSelectedProductMessage(1.0);
    }

    @Test
    public void shouldReturnProduct() throws Exception {
        //given
        when(vendingMachine.getCurrentRequestPrice()).thenReturn(5.0);
        when(cashBox.getInsertedCoinsValueForCurrentRequest()).thenReturn(6.0);
        when(cashBox.isAbleToReturnChangeFor(1.0)).thenReturn(true);
        //when
        vendingMachine.tryToFinalizeRequest();
        //then
        verify(vendingMachine).returnProduct();
    }

    @Test
    public void shouldReturnRestOfMoney() throws Exception {
        //given
        when(vendingMachine.getCurrentRequestPrice()).thenReturn(5.0);
        when(cashBox.getInsertedCoinsValueForCurrentRequest()).thenReturn(6.0);
        when(cashBox.isAbleToReturnChangeFor(any())).thenReturn(true);
        //when
        vendingMachine.tryToFinalizeRequest();
        //then
        verify(vendingMachine).returnRestOfMoney(1.0);
    }

    @Test
    public void shouldSetNullAsCurrentRequest() throws Exception {
        //given
        when(cashBox.getInsertedCoinsValueForCurrentRequest()).thenReturn(6.0);
        when(vendingMachine.getCurrentRequestPrice()).thenReturn(5.0);
        when(cashBox.isAbleToReturnChangeFor(1.0)).thenReturn(true);
        //when
        vendingMachine.tryToFinalizeRequest();
        //then
        assertThat(vendingMachine.currentRequest).isNull();
    }

    @Test
    public void afterFinalizeRequestCurrentStateShouldBeSetToFree() throws Exception {
        //given
        when(vendingMachine.getCurrentRequestPrice()).thenReturn(5.0);
        when(cashBox.getInsertedCoinsValueForCurrentRequest()).thenReturn(6.0);
        //when
        vendingMachine.tryToFinalizeRequest();
        //then
        assertThat(vendingMachine.state).isEqualTo(VendingMachineState.WAITING_FOR_SELECT_PRODUCT);
    }

    @Test
    public void shouldReturnInvalidCoin() throws Exception {
        //given
        Coin coin = new Coin(0.05);
        when(cashBox.isValidCoin(coin)).thenReturn(false);
        //when
        vendingMachine.insertCoinForCurrentRequest(coin);
        //then
        verify(display).showReturnCoinMessage(coin);
    }

    @Test
    public void shouldInsertProduct() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        Product product = new Product(ProductType.CHIPS);
        Integer shelfNumber = 5;
        when(shelfs.get(shelfNumber)).thenReturn(mockedShelf);
        vendingMachine.state = VendingMachineState.SET_UP_MACHINE;
        //when
        vendingMachine.insertProduct(shelfNumber, product);
        //then
        verify(mockedShelf).push(product);
    }

    @Test
    public void shouldNotInsertProductOnOtherThanSetupMachineState() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        Product product = new Product(ProductType.CHIPS);
        Integer shelfNumber = 5;
        when(shelfs.get(shelfNumber)).thenReturn(mockedShelf);
        vendingMachine.state = VendingMachineState.PRODUCT_SELECTED;
        //when
        vendingMachine.insertProduct(shelfNumber, product);
        //then
        verify(mockedShelf, never()).push(product);
    }

    @Test(expected = CannotChangeShelfProductsTypeException.class)
    public void shouldNotInsertProductWhenShelfTypeIsMismatch() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        vendingMachine.shelfs = new Shelfs();
        Product chips = new Product(ProductType.CHIPS);
        Product cola = new Product(ProductType.COLA);
        Integer shelfNumber = 5;
        vendingMachine.state = VendingMachineState.SET_UP_MACHINE;
        vendingMachine.insertProduct(shelfNumber, cola);
        //when
        vendingMachine.insertProduct(shelfNumber, chips);
        //then
    }

    @Test()
    public void shouldReturnInsertedCoins() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        Coin firstCoin = new Coin(5.0);
        Coin secondCoin = new Coin(2.0);
        Stack<Coin> coins = new Stack();
        coins.push(firstCoin);
        coins.push(secondCoin);
        when(cashBox.getCurrentRequestPocket()).thenReturn(coins);
        when(cashBox.isValidCoin(any(Coin.class))).thenReturn(true);
        vendingMachine.state = VendingMachineState.PRODUCT_SELECTED;
        //when
        vendingMachine.cancelRequest();
        //then
        verify(display).showReturnCoinMessage(firstCoin);
        verify(display).showReturnCoinMessage(secondCoin);
    }

    @Test
    public void shouldDisplayReturnCoinMessage() throws Exception {
        Coin coin = new Coin(5.0);
        //when
        vendingMachine.returnCoin(coin);
        //then
        verify(display).showReturnCoinMessage(coin);
    }

    @Test
    public void shouldReturnCoinIfMachineIsOnWaitingForSelectProduct() throws Exception {
        Coin coin = new Coin(5.0);
        vendingMachine.state = VendingMachineState.WAITING_FOR_SELECT_PRODUCT;
        //when
        vendingMachine.insertCoinForCurrentRequest(coin);
        //then
        verify(display).showReturnCoinMessage(coin);
    }

    @Test
    public void shouldNotReturnCoinIfMachineIsOnSelectedProductState() throws Exception {
        Coin coin = new Coin(5.0);
        vendingMachine.state = VendingMachineState.PRODUCT_SELECTED;
        when(cashBox.isValidCoin(coin)).thenReturn(true);
        //when
        vendingMachine.insertCoinForCurrentRequest(coin);
        //then
        verify(vendingMachine, times(0)).returnCoin(coin);
    }

    @Test
    public void shouldInsertCoinToCashBox() throws Exception {
        //given
        vendingMachine.state = VendingMachineState.SET_UP_MACHINE;
        Coin coin = new Coin(5.0);
        //when
        vendingMachine.insertCoinToCashBox(coin);
        //then
        verify(cashBox).addToCashBoxPocket(coin);
    }

    @Test
    public void shouldNotInsertCoinToCashBoxWhenStateIsDifferentThanSetUpMachine() throws Exception {
        //given
        vendingMachine.state = VendingMachineState.PRODUCT_SELECTED;
        Coin coin = new Coin(5.0);
        //when
        vendingMachine.insertCoinToCashBox(coin);
        //then
        verify(cashBox, never()).addToCashBoxPocket(coin);
        verify(display).showInvalidActionForMachineStateMessage();
    }

    @Test
    public void shouldTurnOnSetUpMachineState() throws Exception {
        //given
        vendingMachine.state = null;
        //when
        vendingMachine.turnOnMachineSetUpState();
        //then
        assertThat(vendingMachine.state).isEqualTo(VendingMachineState.SET_UP_MACHINE);
    }

    @Test
    public void shouldTurnOfSetUpMachineState() throws Exception {
        //given
        vendingMachine.state = null;
        //when
        vendingMachine.turnOfMachineSetUpState();
        //then
        assertThat(vendingMachine.state).isEqualTo(VendingMachineState.WAITING_FOR_SELECT_PRODUCT);
    }

    @Test
    public void shouldReturnRestOfMoneyForCurrentRequest() throws Exception {
        //given
        when(vendingMachine.getCurrentRequestPrice()).thenReturn(2.0);
        Coin coin = new Coin(2.0);
        when(cashBox.withdrawCoinsFor(2.0)).thenReturn(Arrays.asList(coin));
        //when
        vendingMachine.returnRestOfMoney(2.0);
        //then
        verify(vendingMachine).returnCoin(coin);
    }

    @Test
    public void shouldDisplayMachineShelfsInformationOnDisplay() throws Exception {
        //given
        IShelf shelf = new Shelf(5);
        when(shelfs.values()).thenReturn(Arrays.asList(shelf));
        //when
        vendingMachine.displayMachineShelfsInformation();
        //then
        verify(display).showShelfInformation(shelf);
    }
}
