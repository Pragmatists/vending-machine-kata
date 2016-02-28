package tdd.vendingMachine.domain;


import tdd.vendingMachine.external_interface.CoinTray;
import tdd.vendingMachine.external_interface.Display;
import tdd.vendingMachine.external_interface.ProductTray;

import java.util.ArrayList;
import java.util.List;

public class VendingMachine {

    public static final String WELCOME_MESSAGE = "Welcome! Please choose product:";

    private Display display;

    private CoinTray coinTray;

    private ProductTray productTray;

    private Money[] pricesPerShelves;

    private PaymentRegistrar paymentRegistrar;

    private List<Coin> acceptedCoins = new ArrayList<>();

    private Integer chosenShelfNumber;

    public VendingMachine(Display display, CoinTray coinTray, ProductTray productTray, Money[] pricesPerShelves) {
        this.display = display;
        this.coinTray = coinTray;
        this.productTray = productTray;
        this.pricesPerShelves = pricesPerShelves;
        this.paymentRegistrar = new PaymentRegistrar();
        this.display.displayMessage(WELCOME_MESSAGE);
    }

    public void acceptChoice(int shelfNumber) {
        this.chosenShelfNumber = shelfNumber;
        if (this.chosenShelfNumber == 0 || shelfNumber > pricesPerShelves.length) {
            display.displayMessage("Invalid shelf choice. Please try again.");
        } else {
            Money productPrice = pricesPerShelves[shelfNumber - 1];
            display.displayMessage("Price: " + productPrice);
            paymentRegistrar.setAmountToBeCollected(productPrice);
        }
    }

    public void acceptCoin(Coin coin) {
        if(chosenShelfNumber == null) return;

        acceptedCoins.add(coin);
        paymentRegistrar.register(coin.getDenomination());
        display.displayMessage("Remaining: " + paymentRegistrar.tellHowMuchMoreNeedsToBeCollected().toString());

        if (paymentRegistrar.hasSufficientMoneyBeenCollected()) {
            sellProduct();
        }
    }

    private void sellProduct() {
        productTray.disposeProduct(chosenShelfNumber);
        display.displayMessage(WELCOME_MESSAGE);
        paymentRegistrar.reset();
        acceptedCoins = new ArrayList<>();
    }

    public void cancel() {
        coinTray.disposeInsertedCoins(new ArrayList<>(acceptedCoins));
        acceptedCoins = new ArrayList<>();
        display.displayMessage(WELCOME_MESSAGE);
    }
}
