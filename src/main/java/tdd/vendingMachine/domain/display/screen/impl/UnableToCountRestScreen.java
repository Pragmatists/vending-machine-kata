package tdd.vendingMachine.domain.display.screen.impl;

import tdd.vendingMachine.domain.display.screen.Screen;

/**
 * @author kdkz
 */
public class UnableToCountRestScreen implements Screen {

    @Override
    public String getScreenContent() {
        return "Cant sell product. Vending Machine cant have enough coins to count rest. Try exact amount";
    }
}
