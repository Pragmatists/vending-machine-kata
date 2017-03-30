package tdd.vendingMachine.domain.display.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.display.Screen;

/**
 * @author kdkz
 */
public class CancelScreen implements Screen {

    private final static Logger log = LoggerFactory.getLogger(CancelScreen.class);

    @Override
    public void show() {
        log.info("Display: Transaction canceled. Already inserted coins returned.");
    }
}
