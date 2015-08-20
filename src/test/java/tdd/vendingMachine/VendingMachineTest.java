package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineTest {

    private VendingMachine machine;
    private Display display;
    private Keyboard keyboard;
    private CoinTray coinTray;
    private CoinVault coinVault;
    private GivingTray givingTray;

    @Before
    public void setUp() throws Exception {
        display = new Display();
        keyboard = new Keyboard();
        coinVault = new CoinVault();
        coinTray = Mockito.spy(new CoinTray());
        givingTray = Mockito.spy(new GivingTray());

        machine = new VendingMachine(display, keyboard, coinTray, givingTray, coinVault);
    }

    @Test(expected = ManyProductsOnOneShelfException.class)
    public void onlyOneProductTypeCanBePlacedOnShelf() throws Exception {
        Product productA = new Product(ProductUtils.BANANA_TYPE);
        Product productB = new Product(ProductUtils.BEER_TYPE);

        machine.addProductToShelf(1, productA);
        machine.addProductToShelf(1, productB);
    }

    @Test
    public void fewProductsOfSameTypeCanBePlacedOnOneShelf() throws Exception {
        Product productA_0 = new Product(ProductUtils.BANANA_TYPE);
        Product productA_1 = new Product(ProductUtils.BANANA_TYPE);

        machine.addProductToShelf(1, productA_0);
        machine.addProductToShelf(1, productA_1);
    }

    @Test
    public void afterSelectingShelfNumberDisplayShouldShowPrice() throws Exception {
        Product product = new Product(ProductUtils.BANANA_TYPE);

        machine.addProductToShelf(1, product);
        keyboard.select(1);

        assertEquals(ProductUtils.BANANA_TYPE.getPrice(), new BigDecimal(display.getContent()));

    }

    @Test
    public void afterSelectingProductAndInsertingCoinsDisplayShouldShowCorrectAmountLeft() throws Exception {
        Product product = new Product(ProductUtils.BANANA_TYPE);

        machine.addProductToShelf(1, product);
        keyboard.select(1);

        coinTray.putCoin(Coin.ONE);

        assertEquals(ProductUtils.BANANA_TYPE.getPrice().subtract(Coin.ONE.getValue()), new BigDecimal(display.getContent()));
    }

    @Test
    public void afterSelectingProductAndInsertingFullAmountOfCoinsProductShouldBeGivenAndDisplayShouldBeEmpty() throws Exception {
        Product product = new Product(ProductUtils.BANANA_TYPE);

        machine.addProductToShelf(1, product);
        keyboard.select(1);

        coinTray.putCoin(Coin.ONE);
        coinTray.putCoin(Coin.HALF);
        coinTray.putCoin(Coin.ONE_FIFTH);

        verify(givingTray, times(1)).giveProduct(eq(product));

        assertEquals("", display.getContent());
    }

    @Test
    public void afterSelectingProductAndInsertingTooMuchMoneyCorrectChangeShouldBeGivenIfPossible() throws Exception {
        Product product = new Product(new Product.ProductType("test2.0", "2.0"));

        machine.addProductToShelf(1, product);
        keyboard.select(1);

        coinTray.putCoin(Coin.ONE);
        coinTray.putCoin(Coin.ONE_TENTH);
        coinTray.putCoin(Coin.HALF);
        coinTray.putCoin(Coin.ONE);

        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);

        verify(coinTray, times(1)).giveChange(argument.capture());

        assertThat((List<Coin>) argument.getValue(), hasItems(Coin.ONE_TENTH, Coin.HALF));
    }

    @Test
    public void afterSelectingProductAndInsertingTooMuchMoneyProductShouldBeNotGivenIfNotPossibleToGiveChange() throws Exception {
        Product product = new Product(new Product.ProductType("test1.6", "1.6"));

        machine.addProductToShelf(1, product);
        keyboard.select(1);

        coinTray.putCoin(Coin.ONE);
        coinTray.putCoin(Coin.TWO);

        verify(givingTray, never()).giveProduct(eq(product));

        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        verify(coinTray).giveChange(argument.capture());

        assertThat((List<Coin>) argument.getValue(), hasItems(Coin.ONE, Coin.TWO));
    }

    @Test
    public void afterSelectingProductInsertingMoneyAndNoChangePossibleDisplayShouldTellIt() throws Exception {
        Product product = new Product(new Product.ProductType("test1.5", "1.5"));

        machine.addProductToShelf(1, product);
        keyboard.select(1);

        coinTray.putCoin(Coin.ONE);
        coinTray.putCoin(Coin.TWO);

        assertEquals(new NoCoinsToChangeException().getMessage(), display.getContent());
    }

    @Test
    public void whenInsertedTooLittleCoinsAndPressedCancelButtonMoneyShouldBeReturned() throws Exception {
        Product product = new Product(new Product.ProductType("test0.8", "0.8"));

        machine.addProductToShelf(1, product);
        keyboard.select(1);

        coinTray.putCoin(Coin.ONE_TENTH);
        coinTray.putCoin(Coin.ONE_TENTH);
        coinTray.putCoin(Coin.ONE_FIFTH);

        keyboard.select(Keyboard.CANCEL_BUTTON);

        verify(coinTray, times(1)).returnInsertedCoins();
    }
}
