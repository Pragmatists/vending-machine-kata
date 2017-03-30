package tdd.vendingMachine.domain.display.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.display.Screen;

import java.math.BigDecimal;

/**
 * @author kdkz
 */
public class InsertedCoinsStatusScreen implements Screen {

    private final static Logger log = LoggerFactory.getLogger(InsertedCoinsStatusScreen.class);

    BigDecimal amountLeft;

    public InsertedCoinsStatusScreen(BigDecimal subtract) {
        amountLeft = subtract;
    }

    @Override
    public void show() {
        log.info("Display: Coins inserted. {} left", amountLeft);
    }
}
