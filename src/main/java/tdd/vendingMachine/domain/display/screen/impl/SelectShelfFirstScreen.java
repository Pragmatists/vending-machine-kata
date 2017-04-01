package tdd.vendingMachine.domain.display.screen.impl;

import tdd.vendingMachine.domain.display.screen.Screen;

/**
 * @author kdkz
 */
public class SelectShelfFirstScreen implements Screen {

    @Override
    public String getScreenContent() {
        return "Shelf must be selected before starting inserting coins. Previous inserted coins returned.";
    }
}
