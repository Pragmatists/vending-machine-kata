package tdd.vendingMachine.domain;


import tdd.vendingMachine.external_interface.Display;

public class VendingMachine {

    private Display display;

    private String[] pricesPerShelves;

    public VendingMachine(Display display, String[] pricesPerShelves) {
        this.display = display;
        this.pricesPerShelves = pricesPerShelves;
        this.display.displayMessage("Welcome! Please choose product:");
    }

    public void acceptChoice(int shelfNumber) {
        display.displayMessage(shelfNumber == 0 || shelfNumber > pricesPerShelves.length ?
            "Invalid shelf choice. Please try again." : "Price: " + pricesPerShelves[shelfNumber - 1]);
    }
}
