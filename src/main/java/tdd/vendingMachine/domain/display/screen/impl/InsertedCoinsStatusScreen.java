package tdd.vendingMachine.domain.display.screen.impl;

import tdd.vendingMachine.domain.display.screen.Screen;

import java.util.List;

/**
 * @author kdkz
 */
public class InsertedCoinsStatusScreen implements Screen {

    private List<String> placeholders;

    public InsertedCoinsStatusScreen(List<String> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public String getScreenContent() {
        return "Coins inserted. " + placeholders.get(0) + " left.";
    }
}
