package tdd.vendingMachine.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import tdd.vendingMachine.service.exception.CoinNotSupportedException;
import tdd.vendingMachine.service.exception.NoMoneyForChangeException;

@Service
public class MoneyService implements IMoneyService {

    private Map<SupportedCoins, Integer> avaiableCoins = new HashMap<>();
    private Map<SupportedCoins, Integer> puttedCoins = new HashMap<>();

    private List<SupportedCoins> change = new ArrayList<>();

    @Override
    public void putCoin(float denomination) throws CoinNotSupportedException {
        SupportedCoins supportedCoins = SupportedCoins.createSupportedCoins(denomination);

        addToPutCoins(supportedCoins);
    }

    private void addToPutCoins(SupportedCoins supportedCoins) {
        Integer currentCoinsCount = puttedCoins.getOrDefault(supportedCoins, 0);
        puttedCoins.put(supportedCoins, ++currentCoinsCount);
    }

    // TODO: precision&rounding in 1 place for all VendMach
    @Override
    public BigDecimal getPuttedSum() {
        return getSum(puttedCoins);
    }

    private BigDecimal getSum(Map<SupportedCoins, Integer> coins) {
        return new BigDecimal(coins.entrySet().stream().mapToDouble(e -> e.getValue() * e.getKey().denomination).sum())
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean hasChange(BigDecimal toPrice) {
        try {
            calculateChange(toPrice);
        } catch (NoMoneyForChangeException e) {
            return false;
        }

        return true;
    }

    @Override
    public List<SupportedCoins> releasePuttedCoins() {
        List<SupportedCoins> releasedCoins = new ArrayList<>();
        this.puttedCoins.entrySet().stream()
                .forEach(e -> IntStream.range(0, e.getValue()).forEach(idx -> releasedCoins.add(e.getKey())));

        this.puttedCoins.clear();
        return releasedCoins;
    }

    @Override
    public List<SupportedCoins> getChange(BigDecimal productPrice) throws NoMoneyForChangeException {
        BigDecimal difference = this.getPuttedSum().subtract(productPrice);

        if (difference.signum() == 0) {
            return change;
        }

        this.change = calculateChange(difference);

        return change;
    }

    protected List<SupportedCoins> calculateChange(BigDecimal difference) throws NoMoneyForChangeException {
        List<SupportedCoins> changeToReturn = new ArrayList<>();

        List<SupportedCoins> sortedCoins = Arrays.asList(SupportedCoins.values());
        sortedCoins.sort((c1, c2) -> c1.denomination.compareTo(c2.denomination) * -1);

        for (SupportedCoins c : sortedCoins) {
            BigDecimal denomination = new BigDecimal(c.denomination).setScale(2, RoundingMode.HALF_UP);

            if (difference.compareTo(denomination) >= 0
                    && (avaiableCoins.containsKey(c) || puttedCoins.containsKey(c))) {
                int bestChangeCoinNo = difference.divide(denomination).intValue();
                int avaiableCouns = Math.min(bestChangeCoinNo, avaiableCoins.getOrDefault(c, 0) + puttedCoins.getOrDefault(c, 0));
                
                for (int i = 0; i < avaiableCouns; i++) {
                    changeToReturn.add(c);
                    difference = difference.subtract(denomination);
                }
            }
        }

        if (difference.signum() != 0) {
            throw new NoMoneyForChangeException();
        }

        return changeToReturn;
    }

    // Transaction
    @Override
    public synchronized void confirm() {
        this.puttedCoins.entrySet().stream().forEach(e -> {
            this.avaiableCoins.put(e.getKey(), e.getValue() + avaiableCoins.getOrDefault(e.getKey(), 0));
        });

        this.puttedCoins.clear();

        this.change.stream().forEach(c -> avaiableCoins.put(c, avaiableCoins.get(c) - 1));
    }
}