package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Coins;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.domain.ProductWithChange;

import static org.assertj.core.api.Assertions.assertThat;

public class VendingMachineDispenseTest extends AbstractVendingMachineTest {

    @Test
    public void should_dispense_product_and_change() {
        // when
        vendingMachine.selectShelf(1);
        vendingMachine.insertCoin(Coin.FIVE.nth(3));
        ProductWithChange productWithChange = vendingMachine.dispense();

        //then
        assertThat(productWithChange.getProduct().get()).isEqualTo(Product.COLA);
        assertThat(productWithChange.getChange().get()).containsExactly(Coin.TWO, Coin.HALF);
    }

    @Test
    public void should_remove_product_from_shelf_after_dispense_product() {
        // when
        vendingMachine.selectShelf(1);
        vendingMachine.insertCoin(Coin.FIVE.nth(3));
        vendingMachine.dispense();

        //then
        assertThat(vendingMachine.hasProduct(Product.COLA)).isFalse();
    }

    @Test
    public void should_take_coins_from_bin_after_dispense_change() {
        // when
        vendingMachine.selectShelf(1);
        vendingMachine.insertCoin(Coin.FIVE.nth(3));
        vendingMachine.dispense();

        //then
        assertThat(vendingMachine.getAvailableCoinsAtBin().get()).containsExactly(
            Coin.FIVE, Coin.FIVE, Coin.FIVE, Coin.FIVE, // 4 => 1 old + 3 inserted coins
            Coin.HALF // 1 => 2 old - 1 returned as change
        );
    }

    @Test
    public void should_not_dispense_product_because_of_enough_inserted_coins() {
        // when
        vendingMachine.selectShelf(1);
        vendingMachine.insertCoin(Coin.FIVE.nth(1));
        ProductWithChange productWithChange = vendingMachine.dispense();

        //then
        assertThat(productWithChange.getProduct().isPresent()).isFalse();
        assertThat(vendingMachine.hasProduct(Product.COLA)).isTrue();
    }

    @Test
    public void should_not_dispense_change_because_of_enough_inserted_coins() {
        // when
        vendingMachine.selectShelf(1);
        vendingMachine.insertCoin(Coin.FIVE.nth(1));
        ProductWithChange productWithChange = vendingMachine.dispense();

        //then
        assertThat(productWithChange.getChange().get()).isEmpty();
    }

    @Test
    public void should_return_coins() {
        // when
        vendingMachine.selectShelf(1);
        vendingMachine.insertCoin(Coin.FIVE.nth(1));

        //then
        assertThat(vendingMachine.returnCoins().get()).containsOnly(Coin.FIVE);
        assertThat(vendingMachine.getAvailableCoinsAtBin().get()).containsExactly(
            // initial coins
            Coin.FIVE,
            Coin.TWO,
            Coin.HALF, Coin.HALF
        );
    }

    @Test
    public void should_not_dispense_product_and_return_change_because_not_have_enough_money() {
        // when
        vendingMachine.selectShelf(3);
        vendingMachine.insertCoin(Coins.of(Coin.FIVE, Coin.ONE));
        ProductWithChange productWithChange = vendingMachine.dispense();

        //then
        assertThat(productWithChange.getProduct().isPresent()).isFalse();
        assertThat(productWithChange.getChange().get()).containsExactly(Coin.FIVE, Coin.ONE);
    }
}
