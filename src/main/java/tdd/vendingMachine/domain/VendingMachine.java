package tdd.vendingMachine.domain;


import tdd.vendingMachine.external_interface.Display;

public class VendingMachine {

    private Display display;

    public VendingMachine(Display display) {
        this.display = display;
        this.display.displayMessage("Welcome! Please choose product:");
    }
}
