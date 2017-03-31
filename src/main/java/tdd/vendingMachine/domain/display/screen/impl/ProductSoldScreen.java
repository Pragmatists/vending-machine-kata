package tdd.vendingMachine.domain.display.screen.impl;

import tdd.vendingMachine.domain.display.screen.Screen;

import java.util.List;

/**
 * @author kdkz
 */
public class ProductSoldScreen implements Screen {

    private List<String> placeholders;

    public ProductSoldScreen(List<String> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public String getScreenContent() {
        return "Product sold. Rest " + placeholders.get(0) + " returned in denominations " + placeholders.get(1) + ".";
    }
}
