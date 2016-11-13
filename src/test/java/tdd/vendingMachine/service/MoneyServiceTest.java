package tdd.vendingMachine.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import tdd.vendingMachine.service.IMoneyService.SupportedCoins;
import tdd.vendingMachine.service.exception.CoinNotSupportedException;
import tdd.vendingMachine.service.exception.NoMoneyForChangeException;

public class MoneyServiceTest {

    private MoneyService moneyService;

    @Before
    public void setUp() {
        moneyService = new MoneyService();
    }

    @Test
    public void put_coin_type_denomination_valid_test() throws CoinNotSupportedException {
        // given
        // when
        moneyService.putCoin(5f);
        // then
        assertThat(moneyService.getPuttedSum()).isEqualTo(new BigDecimal("5.00"));
    }

    @Test
    public void put_coin_one_type_test() throws CoinNotSupportedException {
        // given
        // when
        moneyService.putCoin(5f);
        moneyService.putCoin(5f);
        moneyService.putCoin(5f);
        moneyService.putCoin(5f);
        // then
        assertThat(moneyService.getPuttedSum()).isEqualTo(new BigDecimal("20.00"));
    }

    @Test(expected = CoinNotSupportedException.class)
    public void put_coin_type_denomination_invalid_test() throws CoinNotSupportedException {
        // given
        // when
        moneyService.putCoin(11f);
        // then - expect exc
    }

    @Test
    public void put_multiple_coins_test() {
        // given
        // when
        try {
            moneyService.putCoin(5f);
        } catch (CoinNotSupportedException e) {
        }

        try {
            moneyService.putCoin(11f);
        } catch (CoinNotSupportedException e) {
        }

        try {
            moneyService.putCoin(2f);
        } catch (CoinNotSupportedException e) {
        }

        try {
            moneyService.putCoin(9f);
        } catch (CoinNotSupportedException e) {
        }

        try {
            moneyService.putCoin(0.2f);
        } catch (CoinNotSupportedException e) {
        }

        // then
        assertThat(moneyService.getPuttedSum()).isEqualTo(new BigDecimal("7.20"));
    }

    @Test
    public void has_no_change_zero_coins_test() {
        assertThat(moneyService.hasChange(new BigDecimal("2.00"))).isFalse();
    }

    @Test
    public void has_no_change_no_all_coins_test() throws CoinNotSupportedException {
        // given
        moneyService.putCoin(2f);
        moneyService.putCoin(5f);
        moneyService.putCoin(0.2f);

        assertThat(moneyService.hasChange(new BigDecimal("7.30"))).isFalse();
    }

    @Test
    public void has_change_test() throws CoinNotSupportedException {
        // given
        moneyService.putCoin(2f);
        moneyService.putCoin(5f);
        moneyService.putCoin(0.2f);
        moneyService.putCoin(0.1f);
        // when
        // then
        assertThat(moneyService.hasChange(new BigDecimal("7.20"))).isTrue();
    }

    @Test
    public void has_change_only_ones_test() {
        // given
        IntStream.range(0, 10).forEach(idx -> {
            try {
                moneyService.putCoin(1f);
            } catch (Exception e) {
            }
        });
        // when
        // then
        assertThat(moneyService.hasChange(new BigDecimal("7.00"))).isTrue();
    }

    @Test
    public void release_putted_coins() throws CoinNotSupportedException {
        // given
        moneyService.putCoin(2f);
        moneyService.putCoin(5f);

        // when
        moneyService.releasePuttedCoins();

        // then
        assertThat(moneyService.getPuttedSum()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    public void get_change_no_change_needed_test() throws CoinNotSupportedException, NoMoneyForChangeException {
        // given
        moneyService.putCoin(2f);
        moneyService.putCoin(5f);
        moneyService.putCoin(0.2f);

        // when
        // then
        assertThat(moneyService.getChange(new BigDecimal("7.20"))).isEmpty();
    }

    @Test
    public void get_change_test() throws CoinNotSupportedException, NoMoneyForChangeException {
        // given
        moneyService.putCoin(2f);
        moneyService.putCoin(5f);
        moneyService.putCoin(0.2f);
        moneyService.putCoin(0.1f);

        // when
        List<SupportedCoins> change = moneyService.getChange(new BigDecimal("7.20"));

        // then
        assertThat(change).hasSize(1);
        assertThat(change).containsExactly(SupportedCoins.ONE_TEN);
    }

    @Test
    public void confirm_test() throws CoinNotSupportedException, NoMoneyForChangeException {
        // given
        moneyService.putCoin(2f);
        moneyService.putCoin(5f);
        moneyService.putCoin(0.2f);
        moneyService.putCoin(0.1f);

        // when
        moneyService.confirm();

        // then
        assertThat(moneyService.getPuttedSum()).isEqualTo(new BigDecimal("0.00"));
        assertThat(moneyService.hasChange(new BigDecimal("7.20"))).isTrue();
    }

}
