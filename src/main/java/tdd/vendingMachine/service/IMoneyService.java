package tdd.vendingMachine.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import tdd.vendingMachine.service.exception.CoinNotSupportedException;
import tdd.vendingMachine.service.exception.NoMoneyForChangeException;

@Service
public interface IMoneyService extends Confirmable {
    
    //TODO: enum as dictionary from DB/xml
    public enum SupportedCoins {
        FIVE(5f), TWO(2f), ONE(1f), HALF(0.5f), ONE_FIFTH(0.2f), ONE_TEN(0.1f);
        
        Float denomination;
        
        SupportedCoins(float denomination) {
            this.denomination = denomination;
        }
        
        public static SupportedCoins createSupportedCoins(float denomination) throws CoinNotSupportedException {
            for(SupportedCoins coin : SupportedCoins.values()) {
                if(coin.denomination.equals(denomination)) {
                    return coin;
                }
            }
            
            throw new CoinNotSupportedException("Denomination = " + denomination + " is not supported");
        }
    }

    void putCoin(float denomination) throws CoinNotSupportedException;

    BigDecimal getPuttedSum();

    boolean hasChange(BigDecimal productPrice);

    List<SupportedCoins> releasePuttedCoins();

    List<SupportedCoins> getChange(BigDecimal productPrice) throws NoMoneyForChangeException;

}
