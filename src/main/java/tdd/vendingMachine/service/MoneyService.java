package tdd.vendingMachine.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import tdd.vendingMachine.service.exception.CoinNotSupportedException;

@Service
public class MoneyService implements IMoneyService {
    
    private Map<SupportedCoins, Integer> avaiableCoins = new HashMap<>();
    private Map<SupportedCoins, Integer> putCoins = new HashMap<>();

    @Override
    public void putCoin(float denomination) throws CoinNotSupportedException {
        SupportedCoins supportedCoins = SupportedCoins.createSupportedCoins(denomination);
        
        addToPutCoins(supportedCoins);
    }

    private void addToPutCoins(SupportedCoins supportedCoins) {
        Integer currentCoinsCount = putCoins.containsKey(supportedCoins) ? putCoins.get(supportedCoins) : 1;  
        putCoins.put(supportedCoins, currentCoinsCount);
    }

    //TODO: precision&rounding in 1 place for all VendMach
    @Override
    public BigDecimal getPuttedSum() {
        return new BigDecimal(putCoins.keySet().stream().mapToDouble(key -> putCoins.get(key) * key.denomination).sum()).setScale(2, RoundingMode.HALF_UP);
    }
    
    
}
