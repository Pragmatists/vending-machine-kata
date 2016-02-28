package tdd.vendingMachine.domain;


import tdd.vendingMachine.external_interface.CoinTray;
import tdd.vendingMachine.external_interface.Display;

import java.util.ArrayList;
import java.util.List;

import static tdd.vendingMachine.domain.Money.createMoney;

public class VendingMachine {

    private Display display;

    private CoinTray coinTray;

    private Money[] pricesPerShelves;

    private Money chosenProductPrice;

    private Money remainingCharge;

    private List<Coin> acceptedCoins = new ArrayList<>();

    public VendingMachine(Display display, CoinTray coinTray, Money[] pricesPerShelves) {
        this.display = display;
        this.coinTray = coinTray;
        this.pricesPerShelves = pricesPerShelves;
        this.display.displayMessage("Welcome! Please choose product:");
    }

    public void acceptChoice(int shelfNumber) {
        if (shelfNumber == 0 || shelfNumber > pricesPerShelves.length) {
            display.displayMessage("Invalid shelf choice. Please try again.");
        } else {
            display.displayMessage("Price: " + pricesPerShelves[shelfNumber - 1]);
            chosenProductPrice = pricesPerShelves[shelfNumber - 1];
        }
    }

    public void acceptCoin(Coin coin) {
        acceptedCoins.add(coin);

        if (chosenProductPrice == null) return;

        if (remainingCharge == null) {
            remainingCharge = chosenProductPrice;
        }
        recalculateRemainingCharge(coin.getDenomination());
        display.displayMessage("Remaining: " + remainingCharge.toString());
    }

    private void recalculateRemainingCharge(Money coinValue) {
        remainingCharge = remainingCharge.subtract(coinValue);
        if (remainingCharge.isLessThan(createMoney("0"))) {
            remainingCharge = createMoney("0");
        }
    }

    public void cancel() {
        coinTray.disposeInsertedCoins(new ArrayList<>(acceptedCoins));
        acceptedCoins = new ArrayList<>();
        display.displayMessage("Welcome! Please choose product:");
    }
}
