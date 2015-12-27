package tdd.vendingMachine;

import tdd.vendingMachine.domain.Coins;
import tdd.vendingMachine.domain.Money;

import static tdd.vendingMachine.VendingMachineStateFactory.dispenseProductWithChangeState;
import static tdd.vendingMachine.VendingMachineStateFactory.notEnoughMoneyToChangeState;

public class ProductSelectedState extends VendingMachineState {

    private static final String INSERT_COINS_MESSAGE = "Wrzuć monety...";

    public ProductSelectedState(VendingMachine vendingMachine) {
        super(vendingMachine);
        this.vendingMachine.display.displayText(INSERT_COINS_MESSAGE);
    }

    @Override
    protected Coins returnCoins() {
        Coins credit = vendingMachine.credit;
        vendingMachine.coinBin.take(credit);
        vendingMachine.credit = Coins.empty();
        return credit;
    }

    @Override
    public void insertCoin(Coins coins) {
        acceptCoins(coins);

        Money moneyOfCreditCoins = vendingMachine.credit.asMoney();
        Money productPrice = vendingMachine.shelfs.getProductPrice();

        displayRestOfCreditToBuyAProduct(moneyOfCreditCoins, productPrice);
        checkIfAreSufficientNumberOfCoinsToChange(moneyOfCreditCoins, productPrice);
    }

    private void checkIfAreSufficientNumberOfCoinsToChange(Money moneyOfCreditCoins, Money productPrice) {
        if (isCreditEnoughToBuyAProduct(moneyOfCreditCoins, productPrice)) {
            Money moneyToChange = vendingMachine.calculator.calculateMoneyToChange(vendingMachine.credit, productPrice);
            boolean isSufficientNumberCoinsToChange = vendingMachine.calculator.isSufficientNumberCoinsToChange(vendingMachine.coinBin, moneyToChange);
            vendingMachine.currentState = (isSufficientNumberCoinsToChange) ? dispenseProductWithChangeState(vendingMachine) : notEnoughMoneyToChangeState(vendingMachine);
        }
    }

    private void acceptCoins(Coins coins) {
        vendingMachine.credit = vendingMachine.credit.collect(coins);
        vendingMachine.coinBin.accept(coins);
    }

    private void displayRestOfCreditToBuyAProduct(Money moneyOfCreditCoins, Money productPrice) {
        Money restOfCreditForBuyAProductToDisplay =
            vendingMachine.calculator.calculateRestOfCreditForBuyAProductToDisplay(moneyOfCreditCoins, productPrice);
        vendingMachine.display.displayMoney(restOfCreditForBuyAProductToDisplay);
    }

    private boolean isCreditEnoughToBuyAProduct(Money moneyOfCreditCoins, Money productPrice) {
        return moneyOfCreditCoins.compareTo(productPrice) >= 0;
    }

}
