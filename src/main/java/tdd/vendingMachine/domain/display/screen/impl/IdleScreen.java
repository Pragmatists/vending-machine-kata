package tdd.vendingMachine.domain.display.screen.impl;

import tdd.vendingMachine.domain.display.screen.Screen;

/**
 * @author kdkz
 */
public class IdleScreen implements Screen {

    @Override
    public String getScreenContent() {
        return "Select shelf and then insert coins.";
    }
}
