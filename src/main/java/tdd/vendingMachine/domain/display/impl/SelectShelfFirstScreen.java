package tdd.vendingMachine.domain.display.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.display.Screen;

/**
 * @author kdkz
 */
public class SelectShelfFirstScreen implements Screen {

    private final static Logger log = LoggerFactory.getLogger(SelectShelfFirstScreen.class);

    @Override
    public void show() {
        log.info("Display: Shelf must be selected before starting inserting coins. Previous inserted coins returned.");
    }
}
