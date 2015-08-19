package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class VendingMachineTest {

    private VendingMachine machine;
    private Display display;
    private Keyboard keyboard;
    private CoinTray coinTray;

    @Before
    public void setUp() throws Exception {
        display = new Display();
        keyboard = new Keyboard();
        coinTray = new CoinTray();
        machine = new VendingMachine(display, keyboard, coinTray);
    }

    @Test
    public void productsCouldBePlacedOnShelves() throws Exception {
        Product product = new Product(ProductUtils.BANANA_TYPE);
        machine.addProductToShelf(1, product);

        assertSame(product, machine.getProductFromShelf(1));
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
}
