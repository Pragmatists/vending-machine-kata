package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Map;
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

    @Test
    public void should_remove_value_in_coins_from_wallet() {
        // given
        CoinWallet coinWallet = new CoinWallet()
            .putCoin(Coin.COIN_5)
            .putCoin(Coin.COIN_1, 3)
            .putCoin(Coin.COIN_0_2, 3)
            .putCoin(Coin.COIN_0_1, 2);
        BigDecimal walletValue = coinWallet.calculator().getCoinsValue();
        BigDecimal valueToRemove = new BigDecimal("2.50");
        // when
        Map<Coin, Integer> removedCoins = coinWallet.removeValueInCoins(valueToRemove);
        // then
        assertThat(coinWallet.calculator().getCoinsValue()).isEqualByComparingTo(walletValue.subtract(valueToRemove));
        assertThat(new CoinCalculator(removedCoins).getCoinsValue()).isEqualByComparingTo(valueToRemove);
    }

    @Test
    public void should_throw_exception_when_not_enough_coins_in_wallet() {
        // given
        CoinWallet coinWallet = new CoinWallet()
            .putCoin(Coin.COIN_5)
            .putCoin(Coin.COIN_1, 3)
            .putCoin(Coin.COIN_0_2)
            .putCoin(Coin.COIN_0_1, 2);
        // expect
        assertThatThrownBy(() -> coinWallet.removeValueInCoins(new BigDecimal("2.50")))
            .isExactlyInstanceOf(IllegalStateException.class)
            .hasMessageContaining("2.50");
    }
}
