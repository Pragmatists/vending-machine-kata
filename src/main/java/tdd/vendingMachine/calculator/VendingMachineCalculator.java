package tdd.vendingMachine.calculator;

import tdd.vendingMachine.domain.Coins;
import tdd.vendingMachine.domain.Money;
import tdd.vendingMachine.inventory.CoinBin;

public interface VendingMachineCalculator {

    Money calculateRestOfCreditForBuyAProductToDisplay(Money credit, Money productPrice);

    Money calculateMoneyToChange(Coins credit, Money productPrice);

    Coins calculateBestFitOfCoinsToChange(CoinBin coinBin, Money moneyToChange);

    boolean isSufficientNumberCoinsToChange(CoinBin coinBin, Money moneyToChange);
}
