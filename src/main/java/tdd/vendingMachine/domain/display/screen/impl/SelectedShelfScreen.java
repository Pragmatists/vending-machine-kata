package tdd.vendingMachine.domain.display.screen.impl;

import tdd.vendingMachine.domain.display.screen.Screen;

import java.util.List;

/**
 * @author kdkz
 */
public class SelectedShelfScreen implements Screen {

    private List<String> placeholders;

    public SelectedShelfScreen(List<String> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public String getScreenContent() {
        return "Shelf number " + placeholders.get(0) + " selected. Product price is " + placeholders.get(1) + ".";
    }
}
