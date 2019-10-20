package tdd.vendingMachine.cash.register;

import lombok.Getter;
import tdd.vendingMachine.cash.coin.Coin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static tdd.vendingMachine.util.BigDecimalUtil.createBigDecimalWithPrecision;

public class CashBox extends HashMap<Double, CashBoxPocket> implements ICashBox {
    public static final int REQUIRED_VALUE_PRECISION = 2;
    List<Coin> supportedCoinsValues =
        Arrays.asList(
            new Coin(5.0),
            new Coin(2.0),
            new Coin(1.0),
            new Coin(0.5),
            new Coin(0.2),
            new Coin(0.1)
        );
    @Getter
    CashBoxPocket currentRequestPocket = new CashBoxPocket();

    public CashBox() {
        for (Coin coin : supportedCoinsValues) {
            put(coin.getValue(), new CashBoxPocket());
        }
    }

    public boolean isValidCoin(Coin coin) {
        return supportedCoinsValues.contains(coin);
    }

    public void addToCurrentRequestPocket(Coin coin) {
        currentRequestPocket.add(coin);
    }

    public void addToCashBoxPocket(Coin coin) {
        CashBoxPocket pocket = getCashBoxPocketFor(coin);
        pocket.add(coin);
    }

    public Double getInsertedCoinsValueForCurrentRequest() {
        double sum = currentRequestPocket
            .stream()
            .mapToDouble(Coin::getValue)
            .sum();
        return createBigDecimalWithPrecision(sum).doubleValue();
    }

    @Override
    public boolean isAbleToReturnChangeFor(Double valueToReturn) {
        return valueToReturn >= 0 && hasCoinsToReturnTheChange(valueToReturn);
    }

    @Override
    public List<Coin> withdrawCoinsFor(double value) {
        ArrayList<Coin> coinsToReturn = new ArrayList<>();
        BigDecimal stillNeedToReturn = createBigDecimalWithPrecision(value);
        for (Coin supportedCoinsValue : supportedCoinsValues) {
            Double coinValue = supportedCoinsValue.getValue();
            int neededCoinsForCurrentDenomination = stillNeedToReturn.divide(createBigDecimalWithPrecision(coinValue)).intValue();
            Integer availableCoins = getMachineCoinsCountFor(coinValue);
            int coinsToWithdraw = neededCoinsForCurrentDenomination;
            if (neededCoinsForCurrentDenomination >= availableCoins) {
                coinsToWithdraw = availableCoins;
            }
            stillNeedToReturn = stillNeedToReturn.subtract(getMaxValueWhichIsPossibleInCoinDenomination(neededCoinsForCurrentDenomination, coinValue));
            for (int i = 0; i < coinsToWithdraw; i++) {
                coinsToReturn.add(getCashBoxPocketFor(supportedCoinsValue).pop());
            }
        }
        return coinsToReturn;
    }

    @Override
    public void depositCurrentRequestCoins() {
        while (!currentRequestPocket.isEmpty()) {
            Coin coin = currentRequestPocket.pop();
            getCashBoxPocketFor(coin).push(coin);
        }
    }

    private CashBoxPocket getCashBoxPocketFor(Coin supportedCoinsValue) {
        return get(supportedCoinsValue.getValue());
    }

    private boolean hasCoinsToReturnTheChange(Double valueToReturn) {
        BigDecimal stillNeedToReturn = createBigDecimalWithPrecision(valueToReturn);
        for (Coin supportedCoinsValue : supportedCoinsValues) {
            Double coinValue = supportedCoinsValue.getValue();
            System.out.println("sdajdasd");
            System.out.println("sdajdasd");
            System.out.println("sdajdasd");
            int neededCoinsForCurrentDenomination = stillNeedToReturn.divide(createBigDecimalWithPrecision(coinValue)).intValue();
            stillNeedToReturn = stillNeedToReturn.subtract(getMaxValueWhichIsPossibleInCoinDenomination(neededCoinsForCurrentDenomination, coinValue));
            if (stillNeedToReturn.doubleValue() <= 0) {
                return true;
            }
        }
        return false;
    }

    private BigDecimal getMaxValueWhichIsPossibleInCoinDenomination(Integer neededCoinsForCurrentDenomination, Double value) {
        BigDecimal coinValue = createBigDecimalWithPrecision(value);
        Integer availableCoins = getMachineCoinsCountFor(value);
        if (neededCoinsForCurrentDenomination >= availableCoins) {
            return coinValue.multiply(new BigDecimal(availableCoins));
        }
        return coinValue.multiply(new BigDecimal(neededCoinsForCurrentDenomination));
    }

    private int getMachineCoinsCountFor(Double coinValue) {
        int currentRequestCoinCount = 0;
        for (Coin currentRequestPocketCoin : currentRequestPocket) {
            if (currentRequestPocketCoin.getValue().equals(coinValue)) {
                currentRequestCoinCount++;
            }
        }
        return get(coinValue).size() + currentRequestCoinCount;
    }
}
