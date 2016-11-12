package tdd.vendingMachine.domain.display;

import tdd.vendingMachine.domain.display.strategy.ConsoleDisplayStrategy;
import tdd.vendingMachine.domain.display.strategy.DisplayStrategy;

public class Display {

    private String message;

    private DisplayStrategy displayStrategy;

    public String getMessage() {
        return message;
    }

    public Display setMessage(String message) {
        this.message = message;

        return this;
    }

    public void display() {
        if (null == displayStrategy) {
            displayStrategy = new ConsoleDisplayStrategy();
        }

        displayStrategy.display(getMessage());
    }

    public DisplayStrategy getDisplayStrategy() {
        return displayStrategy;
    }

    public Display setDisplayStrategy(DisplayStrategy displayStrategy) {
        this.displayStrategy = displayStrategy;
        return this;
    }
}
