package tdd.vendingMachine;

import java.util.EnumSet;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class CoinWalletTest implements WithAssertions {

    @Test
    public void should_be_empty_after_construct() {
        // given
        CoinWallet coinWallet = new CoinWallet();
        // expect
        EnumSet.allOf(Coin.class).forEach(c -> assertThat(coinWallet.hasCoin(c)).isFalse());
    }

    @Test
    public void should_have_coin_after_put() {
        // given
        CoinWallet coinWallet = new CoinWallet().putCoin(Coin.COIN_2);
        // expect
        assertThat(coinWallet.hasCoin(Coin.COIN_2)).isTrue();
    }

    @Test
    public void should_throw_when_trying_to_put_negative_quantity_of_coin() {
        // expect
        assertThatThrownBy(() -> new CoinWallet().putCoin(Coin.COIN_0_5, -2))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(String.valueOf(-2));
    }
}
