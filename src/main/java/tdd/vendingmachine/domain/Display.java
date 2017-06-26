package tdd.vendingmachine.domain;

import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ToString
class Display {

    private static final int PRICE_SCALE = 2;
    private static final RoundingMode PRICE_ROUNDING_MODE = RoundingMode.HALF_UP;

    private final String text;

    private Display(String text) {
        this.text = text;
    }

    String show() {
        return text;
    }

    static Display empty() {
        return new Display("");
    }

    static Display shelfNotFound(ShelfNumber shelfNumber) {
        return new Display("No shelf: " + format(shelfNumber));
    }

    static Display emptyShelf(ShelfNumber shelfNumber) {
        return new Display("Empty shelf: " + format(shelfNumber));
    }

    static Display price(Price price) {
        return new Display(format(price));
    }

    private static String format(Price price) {
        BigDecimal priceValue = price.value();
        return priceValue.setScale(PRICE_SCALE, PRICE_ROUNDING_MODE)
                         .toString();
    }

    private static String format(ShelfNumber shelfNumber) {
        return String.format("#%s", shelfNumber.print());
    }
}
