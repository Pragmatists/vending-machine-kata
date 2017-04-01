package tdd.vendingMachine.domain.display.screen.impl;

import tdd.vendingMachine.domain.display.screen.Screen;

/**
 * @author kdkz
 */
public class CancelScreen implements Screen {

    @Override
    public String getScreenContent() {
        return "Transaction canceled. Already inserted coins returned.";
    }
}
