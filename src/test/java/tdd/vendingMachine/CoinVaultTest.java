package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

/**
 * @author macko
 * @since 2015-08-20
 */
public class CoinVaultTest {

    private CoinVault coinVault;

    @Before
    public void setUp() throws Exception {
        this.coinVault = new CoinVault();
    }

    @Test
    public void shouldCorrectlyFindChangeCoinsWhenHavingNeededCoins() throws Exception {
        coinVault.add(Arrays.asList(Coin.FIVE, Coin.TWO, Coin.ONE, Coin.HALF, Coin.ONE_FIFTH, Coin.ONE_TENTH));

        List<Coin> coins = coinVault.getCoinsToChange(BigDecimal.valueOf(0.6));

        assertThat(coins, hasItems(Coin.ONE_TENTH, Coin.ONE_FIFTH));
    }


    @Test
    public void shouldCorrectlyFindChangeCoinsWhenNotHavingNeededCoins() throws Exception {
        coinVault.add(Arrays.asList(Coin.HALF, Coin.ONE_TENTH, Coin.ONE_TENTH));

        List<Coin> coins = coinVault.getCoinsToChange(BigDecimal.valueOf(1.0));

        assertThat(coins, hasItems(Coin.HALF, Coin.ONE_TENTH, Coin.ONE_TENTH));
    }
}
