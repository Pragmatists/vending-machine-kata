package tdd.vendingMachine.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import tdd.vendingMachine.service.exception.CoinNotSupportedException;

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
        } catch (CoinNotSupportedException e) {}

        try {
            moneyService.putCoin(11f);
        } catch (CoinNotSupportedException e) {}
        
        try {
            moneyService.putCoin(2f);
        } catch (CoinNotSupportedException e) {}
        
        try {
            moneyService.putCoin(9f);
        } catch (CoinNotSupportedException e) {}
        
        
        try {
            moneyService.putCoin(0.2f);
        } catch (CoinNotSupportedException e) {}
        
        // then
        assertThat(moneyService.getPuttedSum()).isEqualTo(new BigDecimal("7.20"));
    }

}
