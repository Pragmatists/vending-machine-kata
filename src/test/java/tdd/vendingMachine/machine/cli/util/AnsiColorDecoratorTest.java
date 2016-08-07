package tdd.vendingMachine.machine.cli.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class AnsiColorDecoratorTest {

	@Test
	public void decorates_with_green_color() {
		String greenMessage = AnsiColorDecorator.green("message");

		Assertions.assertThat(greenMessage).isEqualTo("\u001B[32mmessage\u001B[0m");
	}

	@Test
	public void decorates_with_red_color() {
		String greenMessage = AnsiColorDecorator.red("message");

		Assertions.assertThat(greenMessage).isEqualTo("\u001B[31mmessage\u001B[0m");
	}

}
