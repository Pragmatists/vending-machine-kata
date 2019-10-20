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
import tdd.vendingMachine.strategy.IVendingMachineStrategies;
import tdd.vendingMachine.strategy.IVendingMachineStrategy;

import java.util.Arrays;

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
    IVendingMachineStrategy currentStateStrategy;
    @Mock
    IVendingMachineStrategies vendingMachineStrategies;
    @Mock
    IShelf shelf;

    Product sampleProduct = new Product(ProductType.CHIPS);
    VendingMachineState currentState;

    @Before
    public void setUp() throws Exception {
        currentState = vendingMachine.state;
        when(vendingMachineStrategies.get(currentState)).thenReturn(currentStateStrategy);
        vendingMachine.currentRequest = new Request(5, sampleProduct);
    }

    @Test
    public void shouldChangeToProductSelectedStateWhenRequestWasCreatedWell() throws Exception {
        //given
        int validShelfNumber = 5;
        when(shelfs.get(validShelfNumber)).thenReturn(shelf);
        Request createdRequest = new Request(validShelfNumber, sampleProduct);
        when(currentStateStrategy.selectProduct(display, shelf)).thenReturn(createdRequest);
        //when
        vendingMachine.selectProduct(validShelfNumber);
        //then
        assertThat(vendingMachine.state).isEqualTo(VendingMachineState.PRODUCT_SELECTED);
        assertThat(vendingMachine.currentRequest).isEqualTo(createdRequest);
    }

    @Test
    public void shouldNotChangeStateIfRequestDidNotCreatedWell() throws Exception {
        //given
        int invalidShelfNumber = 5;
        when(shelfs.get(invalidShelfNumber)).thenReturn(shelf);
        when(currentStateStrategy.selectProduct(display, shelf)).thenReturn(null);
        vendingMachine.state = VendingMachineState.WAITING_FOR_SELECT_PRODUCT;
        //when
        vendingMachine.selectProduct(invalidShelfNumber);
        //then
        assertThat(vendingMachine.currentRequest).isEqualTo(vendingMachine.currentRequest);
        assertThat(vendingMachine.state).isEqualTo(VendingMachineState.WAITING_FOR_SELECT_PRODUCT);
    }

    @Test
    public void shouldFinalizeRequestIfCoinIsAddedWell() throws Exception {
        //given
        Coin coin = new Coin(5.0);
        doNothing().when(vendingMachine).tryToFinalizeRequest();
        when(currentStateStrategy.insertCoinForCurrentRequest(display, cashBox, coin)).thenReturn(true);
        //when
        vendingMachine.insertCoinForCurrentRequest(coin);
        //then
        verify(vendingMachine).tryToFinalizeRequest();
    }

    @Test
    public void shouldReturnCoinIfCoinValueIsNotValid() throws Exception {
        //given
        Coin coin = new Coin(0.05);
        when(currentStateStrategy.insertCoinForCurrentRequest(display, cashBox, coin)).thenReturn(false);
        //when
        vendingMachine.insertCoinForCurrentRequest(coin);
        //then
        verify(vendingMachine).returnCoin(coin);
    }

    @Test
    public void shouldDisplayMessageAboutReamingValueForSelectedProduct() throws Exception {
        //given
        double reamingValueForCurrentRequest = 2.0;
        when(vendingMachine.countReamingValueForCurrentRequest()).thenReturn(reamingValueForCurrentRequest);
        //when
        vendingMachine.tryToFinalizeRequest();
        //then
        verify(display).showRemainingValueForSelectedProductMessage(reamingValueForCurrentRequest);
    }

    @Test
    public void shouldReturnProductIfCashBoxCanReturnValueForCurrentRequest() throws Exception {
        //given
        double changeValue = 1.0;
        when(cashBox.isAbleToReturnChangeFor(changeValue)).thenReturn(true);
        //when
        vendingMachine.finalizeRequest(changeValue);
        //then
        verify(vendingMachine).returnProduct();
    }

    @Test
    public void shouldDisplayCantReturnChangeMessageWhenCashBoxCantReturnChange() throws Exception {
        //given
        double changeValue = 1.0;
        when(cashBox.isAbleToReturnChangeFor(changeValue)).thenReturn(false);
        //when
        vendingMachine.finalizeRequest(changeValue);
        //then
        verify(display).showCantReturnChangeMessage();
    }

    @Test
    public void shouldCancelRequestIfCashBoxCantReturnChange() throws Exception {
        //given
        double changeValue = 1.0;
        when(cashBox.isAbleToReturnChangeFor(changeValue)).thenReturn(false);
        //when
        vendingMachine.finalizeRequest(changeValue);
        //then
        verify(currentStateStrategy).cancelRequest(display, cashBox);
    }

    @Test
    public void shouldFinalizeRequestIfReamingValueForCurrentRequestIsLessOrEqualsZero() throws Exception {
        //given
        double reamingValueForCurrentRequest = -1.0;
        double valueToReturnToRequester = 1.0;
        when(vendingMachine.countReamingValueForCurrentRequest()).thenReturn(reamingValueForCurrentRequest);
        //when
        vendingMachine.tryToFinalizeRequest();
        //then
        verify(vendingMachine).finalizeRequest(valueToReturnToRequester);
    }

    @Test
    public void shouldSetNullAsCurrentRequestAfterFinalizeRequest() throws Exception {
        //given
        double changeValue = 1.0;
        //when
        vendingMachine.finalizeRequest(changeValue);
        //then
        assertThat(vendingMachine.currentRequest).isNull();
    }

    @Test
    public void afterFinalizeRequestCurrentStateShouldBeSetToWaitingForSelectProduct() throws Exception {
        //given
        vendingMachine.state = VendingMachineState.SET_UP_MACHINE;
        double changeValue = 1.0;
        when(cashBox.isAbleToReturnChangeFor(changeValue)).thenReturn(true);
        //when
        vendingMachine.finalizeRequest(changeValue);
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
    public void shouldCountReamingValueForCurrentRequest() throws Exception {
        //given
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(4.5);
        when(cashBox.getInsertedCoinsValueForCurrentRequest()).thenReturn(2.2);
        vendingMachine.currentRequest = new Request(5, product);
        //when
        Double result = vendingMachine.countReamingValueForCurrentRequest();
        //then
        assertThat(result).isEqualTo(2.3);
    }

    @Test
    public void shouldUserStrategyToInsertProduct() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        Product product = new Product(ProductType.CHIPS);
        Integer shelfNumber = 5;
        when(shelfs.get(shelfNumber)).thenReturn(shelf);
        //when
        vendingMachine.insertProduct(shelfNumber, product);
        //then
        verify(currentStateStrategy).insertProduct(display, shelf, product);
    }

    @Test()
    public void shouldCancelRequestUsingCurrentStateStrategy() throws Exception, CannotChangeShelfProductsTypeException {
        //given
        //when
        vendingMachine.cancelRequest();
        //then
        assertThat(vendingMachine.state).isEqualTo(VendingMachineState.WAITING_FOR_SELECT_PRODUCT);
        verify(currentStateStrategy).cancelRequest(display, cashBox);
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
    public void shouldInsertCoinToCashBox() throws Exception {
        //given
        Coin coin = new Coin(5.0);
        //when
        vendingMachine.insertCoinToCashBox(coin);
        //then
        verify(currentStateStrategy).insertCoinToCashBox(display, cashBox, coin);
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
