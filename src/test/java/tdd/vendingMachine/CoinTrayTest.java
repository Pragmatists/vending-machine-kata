package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;

/**
 * @author macko
 * @since 2015-08-19
 */
public class CoinTrayTest {

    private CoinTray coinTray;

    @Before
    public void setUp() throws Exception {
        coinTray = new CoinTray();
    }

    @Test
    public void initialAmountShouldBeZero() throws Exception {
        assertEquals(BigDecimal.ZERO, coinTray.getInsertedAmount());
    }

    @Test
    public void amountShouldBeFive() {
        for (int i = 0; i < 50; ++i) {
            coinTray.putCoin(Coin.ONE_TENTH);
        }

        assertEquals(BigDecimal.valueOf(5.0), coinTray.getInsertedAmount());
    }

    @Test
    public void shouldReturnInsertedCoinsAndAfterThatAmountShouldBeZero() {
        coinTray.putCoin(Coin.ONE);
        coinTray.putCoin(Coin.ONE_FIFTH);

        List<Coin> returnedCoins = coinTray.returnInsertedCoins();

        assertThat(returnedCoins, hasItems(Coin.ONE, Coin.ONE_FIFTH));
        assertThat(coinTray.getInsertedAmount(), is(BigDecimal.ZERO));
    }
}
