package tdd.vendingMachine.domain.display.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.display.Screen;

/**
 * @author kdkz
 */
public class UnableToCountRestScreen implements Screen {

    private final static Logger log = LoggerFactory.getLogger(UnableToCountRestScreen.class);

    @Override
    public void show() {
        log.info("Cant sell product. Vending Machine cant have enough coins to count rest. Try exact amount");
    }
}
