package tdd.vendingMachine;

import java.math.BigDecimal;
import java.util.EnumSet;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
@RunWith(JUnitParamsRunner.class)
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
    public void should_sum_all_coins_in_wallet() {
        // given
        CoinWallet coinWallet = new CoinWallet()
            .putCoin(Coin.COIN_2)
            .putCoin(Coin.COIN_1, 5)
            .putCoin(Coin.COIN_0_5, 7)
            .putCoin(Coin.COIN_0_1, 10);
        // when
        BigDecimal value = coinWallet.getValue();
        // then
        assertThat(value).isEqualByComparingTo("11.50");
    }

    @Test
    public void should_not_have_change_if_it_is_greater_than_wallet_value() {
        // given
        CoinWallet coinWallet = new CoinWallet()
            .putCoin(Coin.COIN_2)
            .putCoin(Coin.COIN_1)
            .putCoin(Coin.COIN_0_5);
        // expect
        assertThat(coinWallet.hasChange(new BigDecimal("5"))).isFalse();
    }

    @Test
    @Parameters({"1.20", "3.40", "0.80"})
    @TestCaseName("should not have enough coins for change \"{0}\"")
    public void should_not_have_change_is_there_is_no_enough_coins_in_wallet(String change) {
        // given
        CoinWallet coinWallet = new CoinWallet()
            .putCoin(Coin.COIN_2)
            .putCoin(Coin.COIN_1)
            .putCoin(Coin.COIN_0_5);
        // expect
        assertThat(coinWallet.hasChange(new BigDecimal(change))).isFalse();
    }

    @Test
    @Parameters({"11.50", "11.10", "0.80", "1.70"})
    @TestCaseName("should have enough coins for change \"{0}\"")
    public void should_have_change_if_there_is_enough_coins(String change) {
        // given
        CoinWallet coinWallet = new CoinWallet()
            .putCoin(Coin.COIN_2)
            .putCoin(Coin.COIN_1, 5)
            .putCoin(Coin.COIN_0_5, 7)
            .putCoin(Coin.COIN_0_1, 10);
        // expect
        assertThat(coinWallet.hasChange(new BigDecimal(change))).isTrue();
    }
}
