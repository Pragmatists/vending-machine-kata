package tdd.vendingmachine.domain;

import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ToString
class Display {

    private static final int MONEY_SCALE = 2;
    private static final RoundingMode MONEY_ROUNDING_MODE = RoundingMode.HALF_UP;

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

    static Display money(Money money) {
        return new Display(format(money));
    }

    static Display selectProductFirst() {
        return new Display("Select product first");
    }

    static Display coinNotAcceptable() {
        return new Display("Coin not acceptable");
    }

    static Display changeCannotBeGiven() {
        return new Display("No change");
    }

    private static String format(Money money) {
        BigDecimal moneyValue = money.value();
        return moneyValue.setScale(MONEY_SCALE, MONEY_ROUNDING_MODE)
                         .toString();
    }

    private static String format(ShelfNumber shelfNumber) {
        return String.format("#%s", shelfNumber.print());
    }
}
