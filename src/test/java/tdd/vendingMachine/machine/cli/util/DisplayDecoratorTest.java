package tdd.vendingMachine.machine.cli.util;

import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import tdd.vendingMachine.TestUtil;

import java.util.List;

public class DisplayDecoratorTest {

	@Test
	public void decorates_with_display_wrapper() {
		final List<String> input = Lists.newArrayList(
			"Hello, world!",
			"What a long strings we have here.",
			"This last one is definitely the longest."
		);

		final List<String> output = DisplayDecorator.decorate(input);

		Assertions.assertThat(output).hasSize(5);
		Assertions.assertThat(output.get(0)).isEqualTo("+------------------------------------------+");
		Assertions.assertThat(output.get(1)).isEqualTo("| Hello, world!                            |");
		Assertions.assertThat(output.get(2)).isEqualTo("| What a long strings we have here.        |");
		Assertions.assertThat(output.get(3)).isEqualTo("| This last one is definitely the longest. |");
		Assertions.assertThat(output.get(4)).isEqualTo("+------------------------------------------+");
	}

	@Test
	public void colored_input_is_decorated_with_display_wrapper() {
		final List<String> input = Lists.newArrayList(
			"Hello, " + AnsiColorDecorator.green("world") + "!",
			AnsiColorDecorator.green("What") + " a " + AnsiColorDecorator.green("long") + " strings " +
				AnsiColorDecorator.green("we") + " have " + AnsiColorDecorator.green("here") + ".",
			"This last one is definitely the longest."
		);

		final List<String> output = DisplayDecorator.decorate(input);

		Assertions.assertThat(output).hasSize(5);
		Assertions.assertThat(TestUtil.stripColors(output.get(0))).isEqualTo("+------------------------------------------+");
		Assertions.assertThat(TestUtil.stripColors(output.get(1))).isEqualTo("| Hello, world!                            |");
		Assertions.assertThat(TestUtil.stripColors(output.get(2))).isEqualTo("| What a long strings we have here.        |");
		Assertions.assertThat(TestUtil.stripColors(output.get(3))).isEqualTo("| This last one is definitely the longest. |");
		Assertions.assertThat(TestUtil.stripColors(output.get(4))).isEqualTo("+------------------------------------------+");
	}
}
