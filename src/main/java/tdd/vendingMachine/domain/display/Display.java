package tdd.vendingMachine.domain.display;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.display.screen.Screen;

/**
 * @author kdkz
 */
public class Display {

    private final static Logger log = LoggerFactory.getLogger(Display.class);

    public void show(Screen screen) {
        log.info("Display: {}", screen.getScreenContent());
    }

}
