package tdd.vendingMachine.display;

import tdd.vendingMachine.domain.Money;

public class DefaultVendingMachineDisplay implements VendingMachineDisplay {

    private static final String EMPTY_MESSAGE = "";

    private String message;

    public DefaultVendingMachineDisplay() {
        clear();
    }

    @Override
    public String getDisplayableMessage() {
        return message;
    }

    @Override
    public void displayText(String text) {
        this.message = text;
    }

    @Override
    public void displayMoney(Money money) {
        this.message = money.toString();
    }

    @Override
    public void clear() {
        this.message = EMPTY_MESSAGE;
    }
}
