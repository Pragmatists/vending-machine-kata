package tdd.vendingMachine.machine.cli.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CommandLabelDecoratorTest {

	@Test
	public void decorates_key_legend() {
		final String decoratedKeyLegend = CommandLabelDecorator.keyLegend("q", "quit");

		Assertions.assertThat(decoratedKeyLegend).isEqualTo("[ \u001B[32mq\u001B[0m ] - quit");
	}

}
