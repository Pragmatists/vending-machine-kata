package tdd.vendingMachine.domain;


import tdd.vendingMachine.external_interface.HardwareInterface;

import java.util.Map;
import java.util.Optional;

public class VendingMachine {

    private static final String WELCOME_MESSAGE = "Welcome! Please choose product:";

    private HardwareInterface hardwareInterface;

    private Money[] pricesPerShelves;

    private PaymentRegistrar paymentRegistrar;

    private Integer chosenShelfNumber;

    private CoinDispenser coinDispenser;

    public VendingMachine(HardwareInterface hardwareInterface, Money[] pricesPerShelves, CoinDispenser coinDispenser) {
        this.hardwareInterface = hardwareInterface;
        this.pricesPerShelves = pricesPerShelves;
        this.paymentRegistrar = new PaymentRegistrar();
        this.coinDispenser = coinDispenser;
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

        paymentRegistrar.register(coin.getDenomination());
        hardwareInterface.displayMessage("Remaining: " + paymentRegistrar.tellHowMuchMoreNeedsToBeCollected().toString());

        if (paymentRegistrar.hasSufficientMoneyBeenCollected()) {
            finalizeTransaction();
        }
    }

    private void finalizeTransaction() {
        Money collectedMoney = paymentRegistrar.getCollectedAmount();
        Money productPrice = paymentRegistrar.getAmountToBeCollected();
        Optional<Map<Coin, Integer>> change = coinDispenser.calculateChange(collectedMoney.subtract(productPrice));

        if (change.isPresent()) {
            sellProduct(change);
        } else {
            giveBackMoney();
        }

        paymentRegistrar.reset();
    }

    private void giveBackMoney() {
        hardwareInterface.displayMessage("Cannot give the change. Returning money.");
        hardwareInterface.disposeInsertedCoins();
        hardwareInterface.displayMessage(WELCOME_MESSAGE);
    }

    private void sellProduct(Optional<Map<Coin, Integer>> change) {
        hardwareInterface.disposeProduct(chosenShelfNumber);
        hardwareInterface.displayMessage(WELCOME_MESSAGE);
        returnChange(change.get());
    }

    private void returnChange(Map<Coin, Integer> change) {
        coinDispenser.decreaseCoinCountersAccordingToChange(change);
        hardwareInterface.disposeChange(change);
    }

    public void cancel() {
        hardwareInterface.disposeInsertedCoins();
        hardwareInterface.displayMessage(WELCOME_MESSAGE);
        paymentRegistrar.reset();
    }
}
