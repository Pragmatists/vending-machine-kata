package tdd.vendingMachine.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class CoinTest {

    @Test
    public void should_return_false_since_label_not_belongs_to_enum() {
        Assert.assertFalse(Coin.validCoin("some_label"));
    }

    @Test
    public void should_return_true_since_label_belongs_to_enum() {
        Assert.assertTrue(Coin.validCoin(Coin.FIFTY_CENTS.label));
        Assert.assertTrue(Coin.validCoin(Coin.FIFTY_CENTS.provideType()));
    }

    @Test
    public void should_retrieve_coin_since_label_belongs_to_enum() {
        Assert.assertEquals(Coin.FIFTY_CENTS, Coin.retrieveCoinByLabel(Coin.FIFTY_CENTS.label));
        Assert.assertEquals(Coin.FIFTY_CENTS, Coin.retrieveCoinByLabel(Coin.FIFTY_CENTS.provideType()));
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_since_label_sent_null() {
        Coin.validCoin(null);
    }

    @Test(expected = NullPointerException.class)
    public void should_fail_retrieving_coin_since_label_sent_null() {
        Coin.retrieveCoinByLabel(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void should_fail_retrieving_coin_since_label_sent_does_not_belong() {
        Coin.retrieveCoinByLabel("some_label");
    }

    @Test
    public void should_iterate_over_coins_ascending_order() {
        List<Coin> coinsInAscendingDenominationOrder = Arrays.asList( Coin.TEN_CENTS,
            Coin.TWENTY_CENTS, Coin.FIFTY_CENTS, Coin.ONE, Coin.TWO, Coin.FIVE);
        Iterator<Coin> coinIterator = Coin.retrieveOrderAscendingIterator();
        for(int i = 0; i < coinsInAscendingDenominationOrder.size() && coinIterator.hasNext(); i++) {
            Assert.assertEquals(coinsInAscendingDenominationOrder.get(i), coinIterator.next());
        }
        Assert.assertFalse(coinIterator.hasNext());
    }

    @Test
    public void should_iterate_over_coins_in_descending_order() {
        List<Coin> coinsInAscendingDenominationOrder = Arrays.asList( Coin.TEN_CENTS,
            Coin.TWENTY_CENTS, Coin.FIFTY_CENTS, Coin.ONE, Coin.TWO, Coin.FIVE);
        Iterator<Coin> coinIterator = Coin.retrieveOrderDescendingIterator();
        for(int i = coinsInAscendingDenominationOrder.size() - 1; i >= 0 && coinIterator.hasNext(); i--) {
            Assert.assertEquals(coinsInAscendingDenominationOrder.get(i), coinIterator.next());
        }
        Assert.assertFalse(coinIterator.hasNext());
    }
}
