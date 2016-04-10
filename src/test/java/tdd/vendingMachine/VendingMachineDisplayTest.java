package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Coins;

import static org.assertj.core.api.Assertions.assertThat;

public class VendingMachineDisplayTest extends AbstractVendingMachineTest {

    @Test
    public void should_display_to_choose_a_product_after_select_wrong_shelf_number() {
        // when
        vendingMachine.selectShelf(5);

        //then
        assertThat(vendingMachine.showDisplay()).isEqualTo("Wybierz produkt...");
    }

    @Test
    public void should_display_rest_of_credit_message_after_insert_coin() {
        // when
        vendingMachine.selectShelf(1);
        // and coins of: 9
        vendingMachine.insertCoin(Coins.of(Coin.FIVE, Coin.HALF, Coin.TWO, Coin.HALF, Coin.ONE));

        //then
        assertThat(vendingMachine.showDisplay()).isEqualTo("3.5");
    }

    @Test
    public void should_display_finish_message_after_enough_coins_was_inserted_for_product_price() {
        // when
        vendingMachine.selectShelf(1);
        // and coins of: 15
        vendingMachine.insertCoin(Coin.FIVE.nth(3));

        //then
        assertThat(vendingMachine.showDisplay()).isEqualTo("Zakończono pracę, odbierz produkt.");
    }

    @Test
    public void should_display_insufficient_number_of_coin_to_change() {
        // when
        vendingMachine.selectShelf(3);
        // and coins of: 6
        vendingMachine.insertCoin(Coins.of(Coin.FIVE, Coin.ONE));

        //then
        assertThat(vendingMachine.showDisplay()).isEqualTo("Brakuje monet do wydania reszty.");
    }

    @Test
    public void should_display_not_selected_product_message_after_insert_first_coin() {
        // when
        vendingMachine.insertCoin(Coin.HALF.nth(1));

        //then
        assertThat(vendingMachine.showDisplay()).isEqualTo("Nie obsługiwana operacja...");
    }

    @Test
    public void should_display_to_choose_a_product_at_first() {
        // when
        // no operations was triggered

        //then
        assertThat(vendingMachine.showDisplay()).isEqualTo("Wybierz produkt...");
    }

    @Test
    public void should_display_to_insert_a_coin_after_choosen_a_product() {
        // when
        vendingMachine.selectShelf(1);

        //then
        assertThat(vendingMachine.showDisplay()).isEqualTo("Wrzuć monety...");
    }
}
