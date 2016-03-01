package tdd.vendingMachine.domain.data;

import tdd.vendingMachine.domain.parts.money.Coin;
import tdd.vendingMachine.domain.parts.money.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static tdd.vendingMachine.domain.parts.money.Money.createMoney;

public class TransactionData {

    private static final Money ZERO_MONEY = createMoney("0");

    private Optional<Integer> chosenShelfNumber;

    private List<Coin> acceptedCoins;

    private Money collectedAmount;

    private Money amountToBeCollected;

    public TransactionData() {
        this.chosenShelfNumber = Optional.empty();
        this.acceptedCoins = new ArrayList<>();
        this.collectedAmount = ZERO_MONEY;
        this.amountToBeCollected = ZERO_MONEY;
    }

    public boolean isShelfChosen(){
        return chosenShelfNumber.isPresent();
    }

    public void addCoin(Coin coin) {
        acceptedCoins.add(coin);
        collectedAmount = coin.getDenomination().add(collectedAmount);
    }

    public Optional<Integer> getChosenShelfNumber() {
        return chosenShelfNumber;
    }

    public List<Coin> getAcceptedCoins() {
        return acceptedCoins;
    }

    public Money getCollectedAmount() {
        return collectedAmount;
    }

    public Money getAmountToBeCollected() {
        return amountToBeCollected;
    }

    public void setAmountToBeCollected(Money amountToBeCollected) {
        this.amountToBeCollected = amountToBeCollected;
    }

    public void setChosenShelfNumber(int chosenShelfNumber) {
        this.chosenShelfNumber = Optional.of(chosenShelfNumber);
    }
}
