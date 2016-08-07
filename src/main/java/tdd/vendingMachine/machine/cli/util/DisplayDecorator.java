package tdd.vendingMachine.machine.cli.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DisplayDecorator {

	public static final String COLORS_REGEX = "\u001B\\[[;\\d]*m";

	private static final String PIPE = "|";
	private static final String SINGLE_SPACE = " ";
	private static final Pattern COLORS_PATTERN = Pattern.compile(COLORS_REGEX);

	public static List<String> decorate(List<String> messages) {
		int longestString = findLongestString(messages);
		final String horizontalBorder = createHorizontalBorder(longestString);
		List<String> result = Lists.newArrayList(horizontalBorder);
		result.addAll(putInDisplay(messages, longestString));
		result.add(horizontalBorder);
		return result;
	}

	private static int findLongestString(List<String> messages) {
		return messages.stream().map(DisplayDecorator::stripColors).mapToInt(String::length).max().orElse(0);
	}

	private static List<String> putInDisplay(List<String> input, int size) {
		return input
			.stream()
			.map(message -> putInDisplay(message, size))
			.collect(Collectors.toList());
	}

	private static String putInDisplay(String message, int size) {
		return PIPE + SINGLE_SPACE + message + StringUtils.repeat(" ", size - stripColors(message).length()) +
			SINGLE_SPACE + PIPE;
	}

	private static String stripColors(String input) {
		return COLORS_PATTERN.matcher(input).replaceAll("");
	}

	private static String createHorizontalBorder(int size) {
		return "+" + StringUtils.repeat("-", size + 2) + "+";
	}



}
