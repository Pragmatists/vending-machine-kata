package tdd.vendingMachine.calculator;

import com.google.common.collect.Lists;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Coins;
import tdd.vendingMachine.domain.Money;
import tdd.vendingMachine.inventory.CoinBin;

import java.util.Iterator;
import java.util.List;

public class DefaultVendingMachineCalculator implements VendingMachineCalculator {

    @Override
    public Money calculateRestOfCreditForBuyAProductToDisplay(Money credit, Money productPrice) {
        Money restOfCreditToBuyAProduct = productPrice.minus(credit);
        return nonNegativeMoney(restOfCreditToBuyAProduct);
    }

    @Override
    public Money calculateMoneyToChange(Coins credit, Money productPrice) {
        Money moneyToChange = credit.asMoney().minus(productPrice);
        return nonNegativeMoney(moneyToChange);
    }

    @Override
    public Coins calculateBestFitOfCoinsToChange(CoinBin coinBin, Money moneyToChange) {
        List<Coin> resultCoins = Lists.newArrayList();
        List<Coin> coinBinCoins = coinBin.getCoins();
        for (Coin coin : Coin.values()) {
            if (isZeroMoney(moneyToChange)) {
                break;
            }
            int numberOfCoinsNeedsForMoney = coin.nth(moneyToChange).size();
            Coins coinsForChange = tryGetMaximumNumberOfCoinsFromBin(coin, numberOfCoinsNeedsForMoney, coinBinCoins);
            moneyToChange = moneyToChange.minus(coinsForChange.asMoney());
            resultCoins.addAll(coinsForChange.get());
        }
        return Coins.of(resultCoins);
    }

    @Override
    public boolean isSufficientNumberCoinsToChange(CoinBin coinBin, Money moneyToChange) {
        return calculateBestFitOfCoinsToChange(coinBin, moneyToChange).asMoney().compareTo(moneyToChange) == 0;
    }

    private boolean isZeroMoney(Money money) {
        return Money.ZERO.compareTo(money) == 0;
    }

    private Money nonNegativeMoney(Money money) {
        return (money.compareTo(Money.ZERO) < 0) ? Money.ZERO : money;
    }

    private Coins tryGetMaximumNumberOfCoinsFromBin(Coin coin, int numberOfCoins, List<Coin> coinBinCoins) {
        List<Coin> resultCoins = Lists.newArrayList();
        Iterator<Coin> coinsIterator = coinBinCoins.iterator();
        while (numberOfCoins > 0 && coinsIterator.hasNext()) {
            Coin coinFromBinToRemove = coinsIterator.next();
            if (coinFromBinToRemove.equals(coin)) {
                resultCoins.add(coinFromBinToRemove);
                coinsIterator.remove();
                numberOfCoins--;
            }
        }
        return Coins.of(resultCoins);
    }

}
