package tdd.vendingMachine.domain.display.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.display.Screen;

import java.math.BigDecimal;

/**
 * @author kdkz
 */
public class SelectedShelfScreen implements Screen {

    private final static Logger log = LoggerFactory.getLogger(SelectedShelfScreen.class);

    private int selectedShelfNumber;
    private BigDecimal selectedProductPrice;

    public SelectedShelfScreen(int shelfNumber, BigDecimal productPrice) {
        selectedShelfNumber = shelfNumber;
        selectedProductPrice = productPrice;
    }

    @Override
    public void show() {
        log.info("Display: Shelf number {} selected. Product price is {}.", selectedShelfNumber, selectedProductPrice);
    }
}
