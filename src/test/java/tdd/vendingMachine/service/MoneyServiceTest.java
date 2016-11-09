package tdd.vendingMachine.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import tdd.vendingMachine.service.IMoneyService.SupportedCoins;
import tdd.vendingMachine.service.exception.CoinNotSupportedException;

@RunWith(MockitoJUnitRunner.class)
public class MoneyServiceTest {
    
    @Autowired
    private MoneyService moneyService;

    @Test
    public void get_coin_type_denomination_valid_test() throws CoinNotSupportedException {
        assertThat(moneyService.getCoinType(5f)).isEqualTo(SupportedCoins.FIVE);
    }
    
    
    @Test
    public void all_supported_coins_test() throws CoinNotSupportedException {
        for(SupportedCoins coin : SupportedCoins.values()) {
            assertThat(moneyService.getCoinType(coin.denomination)).isEqualTo(coin);
        }
    }
    
    @Test(expected = CoinNotSupportedException.class)
    public void get_coin_type_denomination_invalid_test() throws CoinNotSupportedException {
        moneyService.getCoinType(11f);
    }
}
