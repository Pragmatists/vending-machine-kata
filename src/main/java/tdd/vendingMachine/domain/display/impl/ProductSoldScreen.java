package tdd.vendingMachine.domain.display.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdd.vendingMachine.domain.Denomination;
import tdd.vendingMachine.domain.display.Screen;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author kdkz
 */
public class ProductSoldScreen implements Screen {

    private final static Logger log = LoggerFactory.getLogger(ProductSoldScreen.class);

    private Map<Denomination, Integer> restCoins;

    private BigDecimal restAmount;

    public ProductSoldScreen(Map<Denomination, Integer> rest, BigDecimal amountFromCoins) {
        restCoins = rest;
        restAmount = amountFromCoins;
    }

    @Override
    public void show() {
        log.info("Display: Product sold. Rest {} returned in denominations {}.", restAmount, restCoins);
    }
}
