package tdd.vendingMachine.domain;


import tdd.vendingMachine.domain.data.TransactionData;
import tdd.vendingMachine.domain.parts.ChangeCalculator;
import tdd.vendingMachine.domain.parts.CoinDispenser;
import tdd.vendingMachine.domain.parts.money.Coin;
import tdd.vendingMachine.domain.parts.money.Money;
import tdd.vendingMachine.external_interface.HardwareInterface;

import java.util.Map;
import java.util.Optional;

import static tdd.vendingMachine.domain.parts.money.Money.createMoney;

public class VendingMachine {

    private static final String WELCOME_MESSAGE = "Welcome! Please choose product:";

    private final HardwareInterface hardwareInterface;

    private final Money[] pricesPerShelves;

    private final CoinDispenser coinDispenser;

    private final ChangeCalculator changeCalculator;

    private TransactionData transactionData;

    public VendingMachine(HardwareInterface hardwareInterface, Money[] pricesPerShelves, CoinDispenser coinDispenser, ChangeCalculator changeCalculator) {
        this.hardwareInterface = hardwareInterface;
        this.pricesPerShelves = pricesPerShelves;
        this.coinDispenser = coinDispenser;
        this.changeCalculator = changeCalculator;
        this.transactionData = new TransactionData();

        this.hardwareInterface.displayMessage(WELCOME_MESSAGE);
    }

    public void acceptChoice(int shelfNumber) {
        if (shelfNumber == 0 || shelfNumber > pricesPerShelves.length) {
            hardwareInterface.displayMessage("Invalid shelf choice. Please try again.");
        } else {
            transactionData.setChosenShelfNumber(shelfNumber);
            Money productPrice = pricesPerShelves[shelfNumber - 1];
            hardwareInterface.displayMessage("Price: " + productPrice);
            transactionData.setAmountToBeCollected(productPrice);

            finalizeTransaction();
        }
    }

    public void acceptCoin(Coin coin) {
        transactionData.addCoin(coin);

        if (transactionData.isShelfChosen()) {
            hardwareInterface.displayMessage("Remaining: " + tellHowMuchMoreNeedsToBeCollected().toString());
        }

        finalizeTransaction();
    }

    private void finalizeTransaction() {
        if (hasSufficientMoneyBeenCollected() && transactionData.isShelfChosen()) {
            Optional<Map<Coin, Integer>> change = getNeededChange();

            if (change.isPresent()) {
                hardwareInterface.disposeProduct(transactionData.getChosenShelfNumber().get());
                returnChange(change.get());
                coinDispenser.takeCoins(transactionData.getAcceptedCoins());
            } else {
                giveMoneyBack();
            }
            resetMachineState();
        }
    }

    private Money tellHowMuchMoreNeedsToBeCollected() {
        if (hasSufficientMoneyBeenCollected()) {
            return createMoney("0");
        } else {
            return transactionData.getAmountToBeCollected().subtract(transactionData.getCollectedAmount());
        }
    }

    private boolean hasSufficientMoneyBeenCollected() {
        return transactionData.getCollectedAmount().isGreaterOrEqualTo(transactionData.getAmountToBeCollected());
    }

    private Optional<Map<Coin, Integer>> getNeededChange() {
        Money changeNeeded = transactionData.getCollectedAmount().subtract(transactionData.getAmountToBeCollected());
        return changeCalculator.calculateChange(changeNeeded, coinDispenser.getCoinsInside());
    }

    private void giveMoneyBack() {
        hardwareInterface.displayMessage("Cannot give the change. Returning money.");
        hardwareInterface.disposeInsertedCoins();
    }

    private void returnChange(Map<Coin, Integer> change) {
        coinDispenser.decreaseCoinCountersAccordingToChange(change);
        if (!change.isEmpty()) {
            hardwareInterface.disposeChange(change);
        }
    }

    public void cancel() {
        hardwareInterface.disposeInsertedCoins();
        resetMachineState();
    }

    private void resetMachineState() {
        transactionData = new TransactionData();
        hardwareInterface.displayMessage(WELCOME_MESSAGE);
    }
}
