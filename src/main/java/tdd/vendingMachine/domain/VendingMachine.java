package tdd.vendingMachine.domain;


import tdd.vendingMachine.external_interface.HardwareInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VendingMachine {

    private static final String WELCOME_MESSAGE = "Welcome! Please choose product:";

    private Money[] pricesPerShelves;

    private Integer chosenShelfNumber;

    private List<Coin> acceptedCoins;

    private HardwareInterface hardwareInterface;

    private PaymentRegistrar paymentRegistrar;

    private CoinDispenser coinDispenser;

    private ChangeCalculator changeCalculator;

    public VendingMachine(HardwareInterface hardwareInterface, Money[] pricesPerShelves, CoinDispenser coinDispenser, ChangeCalculator changeCalculator) {
        this.pricesPerShelves = pricesPerShelves;
        this.hardwareInterface = hardwareInterface;
        this.paymentRegistrar = new PaymentRegistrar();
        this.acceptedCoins = new ArrayList<>();
        this.coinDispenser = coinDispenser;
        this.changeCalculator = changeCalculator;

        this.hardwareInterface.displayMessage(WELCOME_MESSAGE);
    }

    public void acceptChoice(int shelfNumber) {
        this.chosenShelfNumber = shelfNumber;
        if (this.chosenShelfNumber == 0 || shelfNumber > pricesPerShelves.length) {
            hardwareInterface.displayMessage("Invalid shelf choice. Please try again.");
        } else {
            Money productPrice = pricesPerShelves[shelfNumber - 1];
            hardwareInterface.displayMessage("Price: " + productPrice);
            paymentRegistrar.setAmountToBeCollected(productPrice);
        }
    }

    public void acceptCoin(Coin coin) {
        if (chosenShelfNumber == null) return;
        acceptedCoins.add(coin);

        paymentRegistrar.register(coin.getDenomination());
        hardwareInterface.displayMessage("Remaining: " + paymentRegistrar.tellHowMuchMoreNeedsToBeCollected().toString());

        if (paymentRegistrar.hasSufficientMoneyBeenCollected()) {
            finalizeTransaction();
        }
    }

    private void finalizeTransaction() {
        Money collectedMoney = paymentRegistrar.getCollectedAmount();
        Money productPrice = paymentRegistrar.getAmountToBeCollected();

        Map<Coin, Integer> coinsInsideDispenser = coinDispenser.getCoinsInside();
        Optional<Map<Coin, Integer>> change = changeCalculator.calculateChange(collectedMoney.subtract(productPrice), coinsInsideDispenser);

        if (change.isPresent()) {
            sellProduct();
            returnChange(change.get());
            coinDispenser.takeCoins(acceptedCoins);
        } else {
            giveBackMoney();
        }

        paymentRegistrar.reset();
    }

    private void giveBackMoney() {
        hardwareInterface.displayMessage("Cannot give the change. Returning money.");
        hardwareInterface.disposeInsertedCoins();
        acceptedCoins = new ArrayList<>();
        hardwareInterface.displayMessage(WELCOME_MESSAGE);
    }

    private void sellProduct() {
        hardwareInterface.disposeProduct(chosenShelfNumber);
        hardwareInterface.displayMessage(WELCOME_MESSAGE);
    }

    private void returnChange(Map<Coin, Integer> change) {
        coinDispenser.decreaseCoinCountersAccordingToChange(change);
        if(!change.isEmpty()) {
            hardwareInterface.disposeChange(change);
        }
    }

    public void cancel() {
        hardwareInterface.disposeInsertedCoins();
        hardwareInterface.displayMessage(WELCOME_MESSAGE);
        paymentRegistrar.reset();
        acceptedCoins = new ArrayList<>();
    }
}
