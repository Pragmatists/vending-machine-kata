package tdd.vendingMachine.domain;

import org.junit.Assert;
import org.junit.Test;

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
    }

    @Test
    public void should_retrieve_coin_since_label_belongs_to_enum() {
        Assert.assertEquals(Coin.FIFTY_CENTS, Coin.retrieveCoinByLabel(Coin.FIFTY_CENTS.label));
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
}
