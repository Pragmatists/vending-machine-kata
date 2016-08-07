package tdd.vendingMachine.machine.state;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import tdd.vendingMachine.machine.cli.util.CommandLinePrinter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UnknownCommandStateTest {

	private CommandLinePrinter commandLinePrinter;

	private UnknownCommandState unknownCommandState;

	@Before
	public void setup() {
		commandLinePrinter = mock(CommandLinePrinter.class);
		unknownCommandState = new UnknownCommandState(commandLinePrinter);
	}

	@Test
	public void has_empty_description() {
		Assertions.assertThat(unknownCommandState.getDescription()).isEmpty();
	}

	@Test
	public void shows_error_message_on_command_execution() {
		final String unknownCommand = "unknownCommand";
		unknownCommandState.executeCommand(unknownCommand, null);

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		verify(commandLinePrinter).print(argumentCaptor.capture());
		Assertions.assertThat(argumentCaptor.getValue()).contains("Unknown command: " + unknownCommand);
	}

}
