package tdd.vendingMachine.domain;


import tdd.vendingMachine.external_interface.Display;

import static tdd.vendingMachine.domain.Money.createMoney;

public class VendingMachine {

    private Display display;

    private Money[] pricesPerShelves;

    private Money chosenProductPrice;
    private Money remainingCharge;

    public VendingMachine(Display display, Money[] pricesPerShelves) {
        this.display = display;
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

    public void acceptCoin(Money coinValue) {
        if (chosenProductPrice == null) return;

        if (remainingCharge == null) {
            remainingCharge = chosenProductPrice;
        }
        recalculateRemainingCharge(coinValue);
        display.displayMessage("Remaining: " + remainingCharge.toString());
    }

    private void recalculateRemainingCharge(Money coinValue) {
        remainingCharge = remainingCharge.subtract(coinValue);
        if (remainingCharge.isLessThan(createMoney("0"))) {
            remainingCharge = createMoney("0");
        }
    }
}
