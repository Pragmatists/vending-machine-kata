package tdd.vendingMachine.machine.cli.util;

public class CommandLabelDecorator {

	public static String keyLegend(String key, String legend) {
		return "[ " + AnsiColorDecorator.green(key) + " ] - " + legend;
	}

	public static String keyLegendInvalid(String key, String legend) {
		return "[ " + AnsiColorDecorator.red(key) + " ] - " + legend;
	}

}
