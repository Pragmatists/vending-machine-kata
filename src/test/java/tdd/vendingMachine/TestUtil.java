package tdd.vendingMachine;

import static tdd.vendingMachine.machine.cli.util.DisplayDecorator.COLORS_REGEX;

public class TestUtil {

    public static String stripColors(String input) {
        return input.replaceAll(COLORS_REGEX, "");
    }
}
